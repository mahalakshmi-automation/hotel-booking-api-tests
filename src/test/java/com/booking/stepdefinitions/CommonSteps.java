package com.booking.stepdefinitions;

import com.booking.utils.ApiUtils;
import com.booking.utils.BookingFixtures;
import com.booking.utils.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonSteps {
    private final ScenarioContext context;

    public CommonSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("an existing booking is available")
    public void existingBooking() {
        BookingFixtures.createBooking(context);
    }

    @Given("I am authenticated as an administrator")
    public void authenticated() {
        context.token(ApiUtils.loginToken());
    }

    @Then("the response status should be {int}")
    public void responseStatus(int expected) {
        Response response = context.response();
        assertEquals(expected, response.statusCode(), "Unexpected response: " + response.asString());
    }

    @Then("the response should contain a booking ID")
    public void bookingId() {
        int id = context.response().jsonPath().getInt("bookingid");
        assertTrue(id > 0, "Booking ID must be positive");
        context.bookingId(id);
    }
}
