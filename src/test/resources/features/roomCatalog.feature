@room @catalog
Feature: Explore hotel rooms before making a booking
  As a guest, I want room details to be accurate before selecting a room.

  @smoke @positive
  Scenario: Guests can view available room listings
    When I browse the hotel room catalogue
    Then the room catalogue request should succeed
    And at least one room should be listed

  @regression @positive
  Scenario: Room identifiers are unique
    When I browse the hotel room catalogue
    Then each listed room should have a unique positive identifier

  @regression @positive
  Scenario: Each room has a name
    When I browse the hotel room catalogue
    Then each listed room should have a nonblank room name

  @regression @positive
  Scenario: Each room has a type
    When I browse the hotel room catalogue
    Then each listed room should have a nonblank room type

  @regression @positive
  Scenario: Every room has a valid nightly price
    When I browse the hotel room catalogue
    Then each listed room should have a positive nightly price

  @regression @positive
  Scenario: Room images are identified
    When I browse the hotel room catalogue
    Then each listed room should have an image reference

  @regression @positive
  Scenario: Rooms describe their facilities
    When I browse the hotel room catalogue
    Then each listed room should have a facilities list

  @regression @positive
  Scenario: Accessibility information is supplied
    When I browse the hotel room catalogue
    Then each listed room should include accessibility information

  @regression @positive
  Scenario: Room descriptions are supplied
    When I browse the hotel room catalogue
    Then each listed room should have a nonblank description

  @regression @positive
  Scenario: Room names are not duplicated in the catalogue
    When I browse the hotel room catalogue
    Then room names should be unique

  @regression @positive
  Scenario: Room facilities have readable labels
    When I browse the hotel room catalogue
    Then every listed facility should have a name

  @regression @positive
  Scenario: Room images have usable references
    When I browse the hotel room catalogue
    Then every room image reference should have a valid path or URL

  @regression @positive @schema
  Scenario: Room catalogue matches its response schema
    When I browse the hotel room catalogue
    Then the room catalogue should match its JSON schema

  @regression @positive
  Scenario: Room catalogue is served as JSON
    When I browse the hotel room catalogue
    Then the room catalogue content type should be JSON

  # These examples validate the public demo catalogue observed with rooms 101-103.
  # They are intentionally kept separate from the booking-create flow, which may
  # fail on the shared service because of concurrent reservations.
  @regression @positive @catalogue-seed
  Scenario Outline: Published rooms can be discovered by their identifiers
    When I browse the hotel room catalogue
    Then room ID <roomId> should be present in the catalogue

    Examples:
      | roomId |
      | 1      |
      | 2      |
      | 3      |

  @regression @positive @catalogue-seed
  Scenario Outline: Guests can find a room of a listed accommodation type
    When I browse the hotel room catalogue
    Then at least one room should have type "<roomType>"

    Examples:
      | roomType |
      | Single   |
      | Double   |
      | Suite    |

  @regression @positive @catalogue-seed
  Scenario Outline: Guests can filter rooms by a useful facility
    When I browse the hotel room catalogue
    Then at least one room should offer the "<facility>" facility

    Examples:
      | facility |
      | TV       |
      | WiFi     |
      | Safe     |
      | Radio    |
