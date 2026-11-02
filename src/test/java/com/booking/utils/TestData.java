package com.booking.utils;

import com.booking.models.BookingDates;
import com.booking.models.BookingRequest;
import java.time.LocalDate;
import java.util.UUID;

/** Builds valid request POJOs; scenario outlines provide the meaningful variations. */
public final class TestData {
    private TestData() {
    }

    public static BookingRequest booking() {
        return booking("Guest", "Tester", 45, 2);
    }

    public static BookingRequest booking(String firstName, String lastName,
                                         int checkInAfterDays, int stayLengthDays) {
        if (checkInAfterDays < 1 || stayLengthDays < 1) {
            throw new IllegalArgumentException("Stay dates must be in the future and checkout after checkin");
        }
        String unique = UUID.randomUUID().toString().substring(0, 8);
        LocalDate checkin = LocalDate.now().plusDays(checkInAfterDays);
        return new BookingRequest(
                Config.getInt("booking.roomid"),
                firstName + unique,
                lastName,
                true,
                new BookingDates(checkin.toString(), checkin.plusDays(stayLengthDays).toString()),
                "guest." + unique + "@example.com",
                "07123456789"
        );
    }
}
