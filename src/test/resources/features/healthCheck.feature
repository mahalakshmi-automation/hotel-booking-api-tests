@health @smoke
Feature: Booking service availability

  Scenario: Booking service is healthy
    When I check the booking service health
    Then the response status should be 200
    And the booking service should report UP
