package com.booking.utils;

import com.booking.models.BookingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/** Centralizes deliberate invalid-payload construction outside Cucumber steps. */
public final class BookingPayloads {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private BookingPayloads() {
    }

    public static ObjectNode withoutField(BookingRequest request, String field) {
        ObjectNode body = MAPPER.valueToTree(request);
        if (!body.has(field)) {
            throw new IllegalArgumentException("Unknown booking field: " + field);
        }
        body.remove(field);
        return body;
    }
}
