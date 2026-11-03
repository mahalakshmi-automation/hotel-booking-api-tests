package com.booking.utils;

import com.booking.models.BookingDates;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Obtains a valid room and an unoccupied date range from the running demo service.
 * No booking is created here; the Cucumber step owns the POST and assertions.
 */
public final class BookingAvailability {
    private static final Logger LOG = LoggerFactory.getLogger(BookingAvailability.class);
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final List<OccupiedStay> RESERVED_IN_THIS_RUN = new ArrayList<>();

    private BookingAvailability() { }

    public static synchronized Selection find(int earliestDays, int nights) {
        Response roomsResponse = ApiUtils.get("/room/", null);
        if (roomsResponse.statusCode() != 200) {
            throw new IllegalStateException("Rooms API unavailable (HTTP " + roomsResponse.statusCode() + ")");
        }
        List<Integer> roomIds = roomsResponse.jsonPath().getList("rooms.roomid", Integer.class);
        if (roomIds == null || roomIds.isEmpty()) {
            throw new IllegalStateException("Rooms API returned no rooms");
        }
        // The live /booking/summary API requires a valid token cookie.
        String token = ApiUtils.loginToken();
        for (int roomId : roomIds.stream().distinct().sorted(Comparator.naturalOrder()).toList()) {
            Response summary = ApiUtils.get("/booking/summary?roomid=" + roomId, token);
            if (summary.statusCode() != 200) {
                LOG.warn("Skipping room {}: summary returned HTTP {}", roomId, summary.statusCode());
                continue;
            }
            List<OccupiedStay> existing;
            try {
                existing = extractStays(JSON.readTree(summary.asString()), roomId);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot parse availability response for room " + roomId, e);
            }
            for (int offset = Math.max(1, earliestDays); offset <= Math.max(365, earliestDays + 90); offset++) {
                LocalDate checkin = LocalDate.now().plusDays(offset);
                LocalDate checkout = checkin.plusDays(nights);
                if (existing.stream().noneMatch(stay -> stay.overlaps(roomId, checkin, checkout))
                        && RESERVED_IN_THIS_RUN.stream().noneMatch(stay -> stay.overlaps(roomId, checkin, checkout))) {
                    RESERVED_IN_THIS_RUN.add(new OccupiedStay(roomId, checkin, checkout));
                    LOG.info("Selected room {} with available stay {} to {}", roomId, checkin, checkout);
                    return new Selection(roomId, new BookingDates(checkin.toString(), checkout.toString()));
                }
            }
        }
        throw new IllegalStateException("No available room/date range found; check room summaries and authentication");
    }

    private static List<OccupiedStay> extractStays(JsonNode node, int roomId) {
        List<OccupiedStay> results = new ArrayList<>();
        collect(node, roomId, results);
        return results;
    }

    private static void collect(JsonNode node, int roomId, List<OccupiedStay> output) {
        if (node == null || node.isNull()) return;
        if (node.isObject()) {
            JsonNode checkin = node.get("checkin");
            JsonNode checkout = node.get("checkout");
            if (checkin != null && checkout != null && checkin.isTextual() && checkout.isTextual()) {
                try {
                    output.add(new OccupiedStay(roomId, LocalDate.parse(checkin.asText()), LocalDate.parse(checkout.asText())));
                } catch (java.time.format.DateTimeParseException e) {
                    throw new IllegalStateException("Unrecognized booking date in summary", e);
                }
            }
            node.elements().forEachRemaining(child -> collect(child, roomId, output));
        } else if (node.isArray()) {
            node.forEach(child -> collect(child, roomId, output));
        }
    }

    public record Selection(int roomId, BookingDates dates) { }
    private record OccupiedStay(int roomId, LocalDate checkin, LocalDate checkout) {
        boolean overlaps(int candidateRoom, LocalDate start, LocalDate end) {
            return roomId == candidateRoom && start.isBefore(checkout) && end.isAfter(checkin);
        }
    }
}
