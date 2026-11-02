package com.booking.stepdefinitions;

import com.booking.utils.ApiUtils;
import com.booking.utils.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;

public class GetMessages {
    private final ScenarioContext context;

    public GetMessages(ScenarioContext context) {
        this.context = context;
    }

    @When("I retrieve guest messages")
    public void messages() {
        context.response(ApiUtils.get("/message", null));
    }

    @Then("each returned message should have an ID")
    public void verify() {
        context.response().then().body("messages.id", everyItem(notNullValue()));
    }
}
