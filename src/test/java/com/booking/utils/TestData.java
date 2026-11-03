package com.booking.utils;

import com.booking.models.BookingRequest;
import java.util.UUID;

/** Builds independent bookings using an actually listed room and unoccupied dates. */
public final class TestData {
    private TestData() { }

    public static BookingRequest booking() {
        return booking("Guest", "Tester", 45, 2);
    }

    public static BookingRequest booking(String firstName, String lastName,
                                         int checkInAfterDays, int stayLengthDays) {
        if (checkInAfterDays < 1 || stayLengthDays < 1) {
            throw new IllegalArgumentException("Stay dates must be in the future and checkout after checkin");
        }
        BookingAvailability.Selection selection = BookingAvailability.find(checkInAfterDays, stayLengthDays);
        String unique = UUID.randomUUID().toString().substring(0, 8);
        return new BookingRequest(
                selection.roomId(), firstName + unique, lastName, true, selection.dates(),
                "guest." + unique + "@example.com", "07123456789");
    }
}
