# Hotel Booking API Tests

Java 17 API regression tests for the [Restful Booker Platform](https://automationintesting.online/), using REST Assured, Cucumber, JUnit Platform and Maven.

## Structure

- `models`: Lombok POJOs serialized by Jackson
- `utils`: configuration, API requests, room availability selection, scenario context and test-data builders
- `stepdefinitions`: business-readable Cucumber steps and assertions
- `hooks`: SLF4J scenario logging
- `src/test/resources/features`: Gherkin scenarios, including scenario outlines
- `src/test/resources/schemas`: response JSON schema

## Configuration

Default non-production values are in `src/test/resources/config.properties`.
Override with Maven properties such as `-Dbase.url=...` or environment variables such as `BASE_URL`. Do not commit real credentials.

## Execution

Run all scenarios with `mvn clean test`.
Run positive creation scenarios with `mvn clean test -Dcucumber.filter.tags='@create and @positive'`.
Run scenarios excluding **known external service failures** with `mvn clean test -Dcucumber.filter.tags='not @known-service-issue'`. Note: remaining untagged scenarios can still fail if the service changes.

Test failures are not suppressed: scenarios marked `@known-service-issue` still run in the full suite. They are tagged to distinguish observed server behavior from local framework problems.

## Reports

Surefire results: `target/surefire-reports/`. Cucumber reports are configured in `TestRunner`.

## External API limitations

The public booking service has returned HTTP 500 on GET `/booking/{id}` and health requests, and intermittent HTTP 409 on booking creation. Negative validation behavior also differs from the bundled OpenAPI specification. The tests preserve expected contract assertions; these issues require service-side investigation.

## Important note about negative tests
The published API description treats `email` and `phone` as optional for booking creation. Their absence is tested as a valid request. The publicly hosted service sometimes returns HTTP 500 even for expected validation failures; the tests intentionally report these differences rather than rewriting the expected behavior.

## Room discovery tests

The `roomCatalog.feature` scenarios verify room listing, identifiers, names, types, prices, descriptions, facilities, accessibility and image references. Run them independently with `mvn test -Dcucumber.filter.tags="@room"`. These scenarios do not create or change bookings.

## Coverage overview

The feature files cover booking creation, retrieval, updates, deletion,
authentication, health, room catalogue discovery and a complete booking lifecycle.
Scenario Outlines produce separate executions for each example row.

Run room catalogue checks independently with `mvn test -Dcucumber.filter.tags="@room"`.
Use `target/cucumber-reports.html` for actual execution results. The presence of
scenarios does not imply they pass against the shared demo API.

Room IDs come from `GET /room/`. When the administrative booking-summary API is
unavailable, the framework uses locally tracked dates and retries HTTP 409 with
new room/date combinations. That fallback does not guarantee the shared server
will accept a booking; unexpected responses remain visible as test failures.

## Catalogue coverage against the hosted demo data

The independent room-catalogue tests include example-driven verification that
published room IDs 1–3, room types Single/Double/Suite, and TV/WiFi/Safe/Radio
are represented in the catalogue. These expectations reflect observed seed data
on the public demonstration service; if its inventory is reset, the
`@catalogue-seed` scenarios may need to be revised based on the latest fixture.

To run only the catalogue tests:

```bash
mvn test -Dcucumber.filter.tags='@room'
```

The Surefire test count is not the expanded Cucumber scenario count; Cucumber's
`target/cucumber.json` contains one scenario record per executed example row.
Use the bundled `scripts/coverage_summary.py` to report scenario-level totals.


## Interpreting test results

Run `mvn clean test` for the complete suite. Scenario Outline example rows count
as separate Cucumber scenarios; Maven Surefire test totals can differ from the
expanded Cucumber scenario count. For expanded totals, run
`python3 scripts/coverage_summary.py` after execution.

The public demo API can return `409 Conflict` when room dates overlap.
Room selection uses actual IDs obtained from the rooms API, rotates across them
and tries alternate future dates after a conflict. This does not guarantee an
available booking on a shared, changing service.

An HTTP `500 Internal Server Error` on GET, PUT, DELETE or invalid-input
requests is recorded as a failed expectation; this project does not convert
server faults into successes or silently omit the affected scenarios.

For room-catalogue-only diagnostics (not a full suite run):
`mvn test -Dcucumber.filter.tags="@room"`.
