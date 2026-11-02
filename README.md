# Kata API Testing in Java

API Testing and Java Exercise: Setting up a Basic API Test Automation Framework.

## Objective
The objective of this exercise is to evaluate your knowledge on API testing and Java by setting up a basic API Test Automation framework using Rest-Assured and Cucumber. You will need to create a test suite that executes a few tests against one endpoint of a hotel booking website and evaluates their responses.

## Background
The application under test is a simple hotel booking website where you can book a room and also send a form with a request.

The website can be accessed at https://automationintesting.online/.

The Swagger documentation for the two endpoints you will be testing can be found at:

Booking endpoint: https://automationintesting.online/booking/swagger-ui/index.html  
Optionally, you also have the Authentican endpoint: https://automationintesting.online/auth/swagger-ui/index.html

### Swagger
This website is an external application which is not in our control.  
We noticed that the Swagger documentation is sometimes not available on the mentioned URL above.  
As a backup, you can find the Swagger documentation in this repository at [src/test/resources/spec/booking.yaml](src/test/resources/spec/booking.yaml)

The Open API Spec file is only supported in the Ultimate version of IntelliJ IDEA. But you can copy the content of the file and paste it in an online Swagger editor like https://editor.swagger.io/ to visualize the API documentation.

### Authentication
In order to authenticate yourself, the required credentials are:
* Username: `admin`
* Password: `password`

## Task
You are provided with an extremely basic API test project.

Please clone the project and create a new branch with your name. At the end, please push your branch to this project.

The project to start from, can be found here: https://github.com/freddyschoeters/API_Testing_kata

Your task is to set up an API Test Automation framework from this project using Java, Rest-Assured, and Cucumber (feel free to add more dependencies if required).

It is up to you to define the test cases. You don’t need to have a full coverage, but you need to show enough variation on the types of tests that you would need to write and execute, and what to check in the response.

This kata has the purpose to evaluate both your technical skills as well as your testing skills.

`For this task, you will use the booking endpoint.`


## Requirements
* Use Java as the programming language
* Use Rest-Assured as the API testing library
* Use Cucumber as the BDD framework
* Design your codebase using a proper Java design pattern
* Write good tests with correct checks
* Use Git for version control and push your codebase to an open GitHub repository
* Make regular commits to demonstrate your progress


## Deliverables
* Your branch pushed in the provided project.
* A comprehensive test suite covering the scenarios mentioned above
* A well-structured codebase with proper design patterns and comments
* Regular commits demonstrating your progress

## Evaluation Criteria
* Being able to successfully run the tests
* Correctness and completeness of the test suite
* Quality of the codebase (design patterns, structure, code quality, …)
* Use of Rest-Assured and Cucumber features
* Commit history and progress demonstration
---

## Hotel Booking API Automation Framework

This project uses Java 17, REST Assured, Cucumber BDD, JUnit 5 and Maven to automate hotel booking API tests.

### Test Coverage

- API health check
- Authentication with valid and invalid credentials
- Create booking and input validation
- Get booking with and without authentication
- Update booking
- Delete booking
- API response status code validation

### Running Tests

Run the tests using:

```bash
mvn clean test
```

### Test Reports

Cucumber reports are generated in the target directory. Maven test results are available under `target/surefire-reports`.

### Known API Issues

Some API scenarios currently return HTTP 500 Internal Server Error instead of the expected status codes. These failures are retained for investigation rather than changing the expected assertions.

API observations are maintained separately with supporting evidence.

### Version Control

Project changes are maintained using Git and pushed to the main branch on GitHub.

### Latest Test Execution Results

Execution date: 31 October 2026

- Total scenarios: 16
- Passed: 6
- Failed: 10
- Skipped: 0
- Build status: FAILURE

The failed scenarios are documented for investigation. Several API endpoints returned HTTP 500 Internal Server Error instead of the expected status codes.

### Test Execution Commands

Run all scenarios:
`mvn clean test`

Run only delete booking scenarios:
`mvn test -Dcucumber.filter.tags="@deleteBooking"`

Run only update booking scenarios:
`mvn test -Dcucumber.filter.tags="@updateBooking"`

### API Testing Observations

The automation tests retain the expected status codes from the API specification. Unexpected responses are recorded as observations rather than changing assertions simply to make tests pass.


## Reviewer-feedback refactoring (October 2026)

- `src/test/resources/config.properties` provides the base URL and local test credentials. Override values with `-Dbase.url=...`, `-Dauth.username=...`, `-Dauth.password=...` or matching environment variables (`BASE_URL`, `AUTH_USERNAME`, `AUTH_PASSWORD`). Do not commit real secrets.
- `models/` contains Lombok POJOs; REST Assured serializes these objects through Jackson.
- `utils/ApiUtils` centralizes request construction, auth cookies, endpoints, and SLF4J response status logging.
- `hooks/ScenarioHooks` logs scenario start and completion; no redundant global test state is stored.
- Create/update/delete scenarios use newly created booking IDs instead of hardcoded IDs, where practical.
- Existing Scenario Outline, Background, JSON Schema, and end-to-end feature remain in place.

**Run:** `mvn clean test`. The final HTML report is `target/cucumber-reports.html`.
Live API 500 responses can still cause legitimate failures; do not change expected assertions to make those tests pass.

**Important:** This refactor has not been validated against the live service in the generated ZIP. Run Maven locally and review actual API responses before submission.


### Cucumber Background usage

- `authentication.feature` uses Background for shared authentication API availability setup.
- `updateBooking.feature` creates a fresh booking in Background for each update scenario; authentication remains scenario-specific so the unauthenticated negative case stays valid.
- `deleteBooking.feature` creates a fresh booking in Background for each deletion scenario; authentication remains scenario-specific.
- Other feature files do not have a shared Given prerequisite that warrants Background; adding one solely for style would make tests less clear.

**Validation note:** Run `mvn test-compile` and `mvn test` locally before committing. Live API HTTP 500 responses may cause test failures unrelated to compilation.

### Final verification checklist (before submission)

1. Import the Maven project in Eclipse and enable Lombok annotation processing if the IDE reports missing generated getters or constructors.
2. Run `mvn test-compile` to check Java compilation.
3. Run `mvn clean test` to generate the latest Cucumber HTML report.
4. Record actual status codes and failures. Do not change contract-based expected values to force green tests.
5. Review `git diff`, commit only reviewed project changes, and push to your own GitHub remote.
6. On Monday, rerun tests and archive the newly generated `target/cucumber-reports.html`.

**Scope note:** Cucumber Background is used only where setup is shared by every scenario in a feature. Adding Background to every feature is not a requirement.
