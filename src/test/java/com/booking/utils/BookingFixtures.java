package com.booking.utils;

import com.booking.models.BookingRequest;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Test setup: retries a genuine room/date conflict with a new availability selection. */
public final class BookingFixtures {
    private static final Logger LOG = LoggerFactory.getLogger(BookingFixtures.class);
    private static final int MAX_CONFLICT_ATTEMPTS = 5;

    private BookingFixtures() { }

    public static int createBooking(ScenarioContext context) {
        for (int attempt = 1; attempt <= MAX_CONFLICT_ATTEMPTS; attempt++) {
            BookingRequest request = TestData.booking("Guest", "Tester", 45 + (attempt - 1) * 14, 2);
            context.request(request);
            Response response = ApiUtils.post("/booking", request);
            context.response(response);
            if (response.statusCode() == 201) {
                int id = response.jsonPath().getInt("bookingid");
                if (id <= 0) throw new IllegalStateException("Booking setup response missing bookingid: " + response.asString());
                context.bookingId(id);
                return id;
            }
            if (response.statusCode() != 409) {
                throw new IllegalStateException("Booking setup failed (HTTP " + response.statusCode() + "): " + response.asString());
            }
            LOG.warn("Booking date collision on room {} (attempt {}/{}); trying new available dates",
                    request.getRoomid(), attempt, MAX_CONFLICT_ATTEMPTS);
        }
        throw new IllegalStateException("Booking setup received HTTP 409 on all " + MAX_CONFLICT_ATTEMPTS
                + " available-room/date attempts. Shared demo API may be changing concurrently.");
    }
}
