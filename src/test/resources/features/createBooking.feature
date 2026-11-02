@booking @create
Feature: Create a hotel booking
  As a guest, I want to book a room with valid personal details and stay dates.

  @positive @smoke
  Scenario Outline: Guest creates a booking for a valid stay
    When I submit a booking for "<firstName>" "<lastName>" arriving in <daysUntilCheckIn> days for <nights> nights
    Then the response status should be 201
    And the response should contain a booking ID
    And the created booking should match the submitted details

    Examples:
      | firstName | lastName | daysUntilCheckIn | nights |
      | Alex      | Taylor   | 45               | 2      |
      | Sam       | Morgan   | 70               | 3      |

  @negative @regression @known-service-issue
  Scenario: Booking request has no body
    When I submit a booking without a request body
    Then the response status should be 400

  @negative @regression @known-service-issue
  Scenario Outline: Required fields cannot be omitted
    When I submit a booking missing the "<field>" field
    Then the response status should be 400

    Examples:
      | field     |
      | firstname |
      | lastname  |
      | email     |
