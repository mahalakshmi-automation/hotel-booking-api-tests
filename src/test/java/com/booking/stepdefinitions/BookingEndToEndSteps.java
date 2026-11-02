package com.booking.stepdefinitions;

import com.booking.utils.ApiUtils;
import com.booking.utils.BookingFixtures;
import com.booking.utils.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingEndToEndSteps {
    private final ScenarioContext context;

    public BookingEndToEndSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("I have created a booking for a complete lifecycle")
    public void created() {
        BookingFixtures.createBooking(context);
    }

    @When("I complete the booking lifecycle")
    public void lifecycle() {
        String path = "/booking/" + context.bookingId();
        context.token(ApiUtils.loginToken());
        assertEquals(200, ApiUtils.get(path, context.token()).statusCode(), "Booking should be readable");
        context.request().setLastname("UpdatedGuest");
        assertEquals(200, ApiUtils.put(path, context.request(), context.token()).statusCode(),
                "Booking update should succeed");
        assertEquals("UpdatedGuest", ApiUtils.get(path, context.token()).jsonPath().getString("lastname"));
        context.response(ApiUtils.delete(path, context.token()));
    }

    @Then("the deleted booking should no longer be available")
    public void deleted() {
        assertEquals(404, ApiUtils.get("/booking/" + context.bookingId(), context.token()).statusCode());
    }
}
