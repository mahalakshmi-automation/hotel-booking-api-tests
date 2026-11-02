package com.booking.stepdefinitions;

import com.booking.utils.ApiUtils;
import com.booking.utils.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthCheckSteps {
    private final ScenarioContext context;

    public HealthCheckSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I check the booking service health")
    public void check() {
        context.response(ApiUtils.get("/booking/actuator/health", null));
    }

    @Then("the booking service should report UP")
    public void running() {
        assertEquals("UP", context.response().jsonPath().getString("status"));
    }
}
