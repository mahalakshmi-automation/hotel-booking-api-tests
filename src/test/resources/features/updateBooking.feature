@booking @update
Feature: Update an existing booking

  Background:
    Given an existing booking is available

  @positive @regression
  Scenario Outline: Administrator changes a booking last name
    Given I am authenticated as an administrator
    When I update the booking last name to "<lastName>"
    Then the response status should be 200

    Examples:
      | lastName |
      | Smith    |
      | Taylor   |

  @positive @regression
  Scenario: Administrator changes a booking first name
    Given I am authenticated as an administrator
    When I change the booking first name to "David"
    Then the response status should be 200

  @negative @regression
  Scenario: Guest cannot update a booking without authentication
    When I update the booking last name to "Smith"
    Then the response status should be 401
