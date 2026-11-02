@booking @e2e @regression
Feature: Complete a hotel booking lifecycle

  Scenario: Create, read, update and delete a guest booking
    Given I have created a booking for a complete lifecycle
    When I complete the booking lifecycle
    Then the response status should be 201
    And the deleted booking should no longer be available
