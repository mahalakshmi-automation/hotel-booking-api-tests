package com.booking.stepdefinitions;

import com.booking.utils.ApiUtils;
import com.booking.utils.ScenarioContext;
import io.cucumber.java.en.When;

import java.util.Map;

public class UpdateBookingSteps {
    private final ScenarioContext context;

    public UpdateBookingSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I update the booking last name to {string}")
    public void update(String lastName) {
        context.request().setLastname(lastName);
        context.response(ApiUtils.put("/booking/" + context.bookingId(), context.request(), context.token()));
    }

    @When("I change the booking first name to {string}")
    public void patch(String firstName) {
        context.response(ApiUtils.patch("/booking/" + context.bookingId(),
                Map.of("firstname", firstName), context.token()));
    }
}
