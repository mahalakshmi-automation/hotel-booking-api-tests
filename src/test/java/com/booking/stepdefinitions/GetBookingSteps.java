package com.booking.stepdefinitions;

import com.booking.utils.ApiUtils;
import com.booking.utils.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetBookingSteps {
    private final ScenarioContext context;

    public GetBookingSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I retrieve the existing booking")
    public void getBooking() {
        context.response(ApiUtils.get("/booking/" + context.bookingId(), context.token()));
    }

    @When("I retrieve booking ID {int} without authentication")
    public void getById(int id) {
        context.response(ApiUtils.get("/booking/" + id, null));
    }

    @Then("the booking response should match its JSON schema")
    public void schema() {
        context.response().then().body(matchesJsonSchemaInClasspath("schemas/booking-response-schema.json"));
    }

    @Then("the retrieved booking should match the created booking")
    public void match() {
        assertEquals(context.request().getFirstname(), context.response().jsonPath().getString("firstname"));
    }
}
