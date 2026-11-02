# Hotel Booking API Tests

Java 17 API regression tests for the [Restful Booker Platform](https://automationintesting.online/), using REST Assured, Cucumber, JUnit Platform and Maven.

## Structure

- `models`: Lombok POJOs serialized by Jackson
- `utils`: configuration, API requests, scenario context and test-data builders
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
Run scenarios excluding **known external service failures** with `mvn clean test -Dcucumber.filter.tags='not @known-service-issue'`.

Test failures are not suppressed: scenarios marked `@known-service-issue` still run in the full suite. They are tagged to distinguish observed server behavior from local framework problems.

## Reports

Surefire results: `target/surefire-reports/`. Cucumber reports are configured in `TestRunner`.

## External API limitations

The public booking service has returned HTTP 500 on GET `/booking/{id}` and health requests, and intermittent HTTP 409 on booking creation. Negative validation behavior also differs from the bundled OpenAPI specification. The tests preserve expected contract assertions; these issues require service-side investigation.
