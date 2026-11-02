@booking @delete
Feature: Delete an existing booking

  Background:
    Given an existing booking is available

  @positive @regression
  Scenario: Administrator deletes an existing booking
    Given I am authenticated as an administrator
    When I delete the existing booking
    Then the response status should be 201

  @negative @regression
  Scenario: Guest cannot delete a booking without authentication
    When I delete the existing booking
    Then the response status should be 401
