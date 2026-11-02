package com.booking.stepdefinitions;

import com.booking.models.LoginRequest;
import com.booking.utils.ApiUtils;
import com.booking.utils.Config;
import com.booking.utils.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthenticationSteps {
    private final ScenarioContext context;

    public AuthenticationSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I sign in using configured credentials")
    public void validLogin() {
        LoginRequest request = new LoginRequest(Config.get("auth.username"), Config.get("auth.password"));
        context.response(ApiUtils.post("/auth/login", request));
    }

    @When("I sign in with username {string} and password {string}")
    public void login(String username, String password) {
        context.response(ApiUtils.post("/auth/login", new LoginRequest(username, password)));
    }

    @Then("an authentication token should be returned")
    public void token() {
        String token = context.response().jsonPath().getString("token");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Then("the authentication error should be {string}")
    public void error(String expected) {
        assertEquals(expected, context.response().jsonPath().getString("error"));
    }
}
