@message @regression
Feature: Retrieve guest messages

  Scenario: Messages include identifiers
    When I retrieve guest messages
    Then the response status should be 200
    And each returned message should have an ID
