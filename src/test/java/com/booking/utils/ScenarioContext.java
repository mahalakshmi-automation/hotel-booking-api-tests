package com.booking.utils;

import com.booking.models.BookingRequest;
import io.restassured.response.Response;

public class ScenarioContext {
    private Response response;
    private BookingRequest request;
    private Integer bookingId;
    private String token;

    public Response response() {
        if (response == null) {
            throw new IllegalStateException("No API response in this scenario");
        }
        return response;
    }

    public void response(Response value) {
        response = value;
    }

    public BookingRequest request() {
        return request;
    }

    public void request(BookingRequest value) {
        request = value;
    }

    public int bookingId() {
        if (bookingId == null) {
            throw new IllegalStateException("Booking ID has not been created");
        }
        return bookingId;
    }

    public void bookingId(int value) {
        bookingId = value;
    }

    public String token() {
        return token;
    }

    public void token(String value) {
        token = value;
    }
}
