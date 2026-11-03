package com.booking.stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.booking.models.BookingRequest;
import com.booking.utils.ApiUtils;
import com.booking.utils.BookingPayloads;
import com.booking.utils.ScenarioContext;
import com.booking.utils.TestData;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateBookingSteps {
	private final ScenarioContext context;

	public CreateBookingSteps(ScenarioContext context) {
		this.context = context;
	}

	@When("I submit a booking for {string} {string} arriving in {int} days for {int} nights")
	public void createBookingWithExamples(String firstName, String lastName, int checkInAfterDays, int nights) {
        for (int attempt = 1; attempt <= 5; attempt++) {
            BookingRequest request = TestData.booking(firstName, lastName,
                    checkInAfterDays + (attempt - 1) * 14, nights);
            submit(request);
            if (context.response().statusCode() != 409) return;
        }
	}

	@When("I submit a booking without a request body")
	public void emptyBooking() {
		context.response(ApiUtils.post("/booking", null));
	}

	@When("I submit a booking missing the {string} field")
	public void bookingMissingField(String field) {
		BookingRequest request = TestData.booking();
		context.request(request);
		context.response(ApiUtils.post("/booking", BookingPayloads.withoutField(request, field)));
	}


    @When("I submit a booking with an invalid {string} value of {string}")
    public void invalidBookingField(String field, String invalidValue) {
        BookingRequest request = TestData.booking();
        context.request(request);
        ObjectNode payload = new com.fasterxml.jackson.databind.ObjectMapper().valueToTree(request);
        if (!payload.has(field)) {
            throw new IllegalArgumentException("Unknown booking field: " + field);
        }
        payload.put(field, invalidValue);
        context.response(ApiUtils.post("/booking", payload));
    }

	@Then("the created booking should match the submitted details")
	public void verifyBooking() {
		BookingRequest request = context.request();
		assertEquals(request.getRoomid(), context.response().jsonPath().getInt("roomid"));
		assertEquals(request.getFirstname(), context.response().jsonPath().getString("firstname"));
		assertEquals(request.getLastname(), context.response().jsonPath().getString("lastname"));
		assertEquals(request.isDepositpaid(), context.response().jsonPath().getBoolean("depositpaid"));
		assertEquals(request.getBookingdates().getCheckin(),
				context.response().jsonPath().getString("bookingdates.checkin"));
		assertEquals(request.getBookingdates().getCheckout(),
				context.response().jsonPath().getString("bookingdates.checkout"));
	}

	private void submit(BookingRequest request) {
		context.request(request);
		context.response(ApiUtils.post("/booking", request));
	}
}
