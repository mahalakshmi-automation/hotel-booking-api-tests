@authentication
Feature: Administrator authentication
  To manage bookings, an administrator must sign in successfully.

  @positive @smoke
  Scenario: Administrator signs in with valid credentials
    When I sign in using configured credentials
    Then the response status should be 200
    And an authentication token should be returned

  @negative @regression
  Scenario Outline: Invalid credentials cannot create a session
    When I sign in with username "<username>" and password "<password>"
    Then the response status should be 401
    And the authentication error should be "Invalid credentials"

    Examples:
      | username | password |
      | invalid  | wrong123 |
      | admin    | wrong123 |
