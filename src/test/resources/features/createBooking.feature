@booking @create
Feature: Create a hotel booking
  Guests can request rooms using their personal details and stay dates.

  @positive @smoke
  Scenario: Guest creates a valid booking
    When I submit a valid booking request
    Then the response status should be 201
    And the response should contain a booking ID
    And the created booking should match the submitted details

  @negative @regression
  Scenario: Booking requires a request body
    When I submit a booking without a request body
    Then the response status should be 400

  @negative @regression
  Scenario Outline: Mandatory booking fields cannot be omitted
    When I submit a booking missing the "<field>" field
    Then the response status should be 400

    Examples:
      | field     |
      | firstname |
      | lastname  |
      | email     |
