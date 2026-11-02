package com.booking.stepdefinitions;

import com.booking.models.BookingRequest;
import com.booking.utils.ApiUtils;
import com.booking.utils.ScenarioContext;
import com.booking.utils.TestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateBookingSteps {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final ScenarioContext context;

    public CreateBookingSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I submit a valid booking request")
    public void createBooking() {
        BookingRequest request = TestData.booking();
        context.request(request);
        context.response(ApiUtils.post("/booking", request));
    }

    @When("I submit a booking without a request body")
    public void emptyBooking() {
        context.response(ApiUtils.post("/booking", null));
    }

    @When("I submit a booking missing the {string} field")
    public void bookingMissingField(String field) throws JsonProcessingException {
        BookingRequest request = TestData.booking();
        ObjectNode payload = MAPPER.valueToTree(request);
        if (!payload.has(field)) {
            throw new IllegalArgumentException("Unknown booking field: " + field);
        }
        payload.remove(field);
        context.response(ApiUtils.post("/booking", payload));
    }

    @Then("the created booking should match the submitted details")
    public void verifyBooking() {
        BookingRequest request = context.request();
        assertEquals(request.getRoomid(), context.response().jsonPath().getInt("booking.roomid"));
        assertEquals(request.getFirstname(), context.response().jsonPath().getString("booking.firstname"));
        assertEquals(request.getLastname(), context.response().jsonPath().getString("booking.lastname"));
        assertEquals(request.getEmail(), context.response().jsonPath().getString("booking.email"));
        assertEquals(request.getBookingdates().getCheckin(),
                context.response().jsonPath().getString("booking.bookingdates.checkin"));
    }
}
