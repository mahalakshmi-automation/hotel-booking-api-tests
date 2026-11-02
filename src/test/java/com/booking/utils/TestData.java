
package com.booking.utils;

import com.booking.models.BookingDates;
import com.booking.models.BookingRequest;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class TestData {

    private TestData() {
    }

    public static BookingRequest booking() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        LocalDate checkin = LocalDate.now()
                .plusDays(45 + ThreadLocalRandom.current().nextInt(1, 180));

        int roomId = ThreadLocalRandom.current().nextInt(1, 4);

        return new BookingRequest(
                roomId,
                "Guest" + suffix,
                "Tester",
                true,
                new BookingDates(
                        checkin.toString(),
                        checkin.plusDays(2).toString()
                ),
                "guest." + suffix + "@example.com",
                "07123456789"
        );
    }
}
