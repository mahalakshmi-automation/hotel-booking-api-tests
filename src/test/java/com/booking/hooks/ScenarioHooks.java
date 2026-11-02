package com.booking.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScenarioHooks {
    private static final Logger LOG = LoggerFactory.getLogger(ScenarioHooks.class);

    @Before
    public void before(Scenario scenario) {
        LOG.info("Starting scenario: {}", scenario.getName());
    }

    @After
    public void after(Scenario scenario) {
        LOG.info("Finished scenario: {} ({})", scenario.getName(), scenario.getStatus());
    }
}
