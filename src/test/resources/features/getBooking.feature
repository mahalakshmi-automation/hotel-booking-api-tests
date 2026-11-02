@booking @get
Feature: Retrieve an existing booking

  Background:
    Given an existing booking is available

  @positive @regression
  Scenario: Guest retrieves their booking
    Given I am authenticated as an administrator
    When I retrieve the existing booking
    Then the response status should be 200
    And the retrieved booking should match the created booking
    And the booking response should match its JSON schema
