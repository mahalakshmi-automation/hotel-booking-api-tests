package com.booking.utils;

import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class BookingFixtures {
    private BookingFixtures() {
    }

    public static int createBooking(ScenarioContext context) {
        context.request(TestData.booking());
        Response created = ApiUtils.post("/booking", context.request());
        assertEquals(201, created.statusCode(), "Could not prepare booking: " + created.asString());
        int id = created.jsonPath().getInt("bookingid");
        assertTrue(id > 0, "Booking ID must be positive");
        context.bookingId(id);
        context.response(created);
        return id;
    }
}
