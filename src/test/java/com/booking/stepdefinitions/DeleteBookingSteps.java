package com.booking.stepdefinitions;

import com.booking.utils.ApiUtils;
import com.booking.utils.ScenarioContext;
import io.cucumber.java.en.When;

public class DeleteBookingSteps {
    private final ScenarioContext context;

    public DeleteBookingSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I delete the existing booking")
    public void delete() {
        context.response(ApiUtils.delete("/booking/" + context.bookingId(), context.token()));
    }
}
