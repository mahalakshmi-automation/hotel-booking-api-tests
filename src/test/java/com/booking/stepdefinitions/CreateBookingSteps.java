package com.booking.stepdefinitions;

import com.booking.models.BookingRequest;
import com.booking.utils.ApiUtils;
import com.booking.utils.BookingPayloads;
import com.booking.utils.ScenarioContext;
import com.booking.utils.TestData;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateBookingSteps {
    private final ScenarioContext context;

    public CreateBookingSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I submit a valid booking request")
    public void createBooking() {
        submit(TestData.booking());
    }

    @When("I submit a booking for {string} {string} arriving in {int} days for {int} nights")
    public void createBookingWithExamples(String firstName, String lastName,
                                          int checkInAfterDays, int nights) {
        submit(TestData.booking(firstName, lastName, checkInAfterDays, nights));
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
