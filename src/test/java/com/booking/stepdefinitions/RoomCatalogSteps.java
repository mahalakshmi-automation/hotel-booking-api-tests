package com.booking.stepdefinitions;

import com.booking.utils.ApiUtils;
import com.booking.utils.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/** Validates room discovery independently of the booking-creation workflow. */
public final class RoomCatalogSteps {
    private final ScenarioContext context;

    public RoomCatalogSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I browse the hotel room catalogue")
    public void browseRooms() {
        context.response(ApiUtils.get("/room/", null));
    }

    @Then("the room catalogue request should succeed")
    public void catalogueSuccess() {
        assertEquals(200, context.response().statusCode(), context.response().asString());
    }

    private List<Map<String, Object>> rooms() {
        Response response = context.response();
        assertEquals(200, response.statusCode(), "Room listing request failed: " + response.asString());
        List<Map<String, Object>> rooms = response.jsonPath().getList("rooms");
        assertNotNull(rooms, "Room listing should contain a rooms array");
        assertFalse(rooms.isEmpty(), "Room listing should not be empty");
        return rooms;
    }

    @Then("at least one room should be listed")
    public void roomsPresent() {
        assertFalse(rooms().isEmpty());
    }

    @Then("each listed room should have a unique positive identifier")
    public void identifiers() {
        Set<Integer> seen = new HashSet<>();
        for (Map<String, Object> room : rooms()) {
            Object id = room.get("roomid");
            assertInstanceOf(Number.class, id, "Room ID must be numeric");
            int number = ((Number) id).intValue();
            assertTrue(number > 0, "Room ID must be positive");
            assertTrue(seen.add(number), "Duplicate room ID: " + number);
        }
    }

    private void requireNonblank(String field) {
        for (Map<String, Object> room : rooms()) {
            Object value = room.get(field);
            assertInstanceOf(String.class, value, "Missing or invalid field: " + field);
            assertFalse(((String) value).isBlank(), "Blank " + field + " for room " + room.get("roomid"));
        }
    }

    @Then("each listed room should have a nonblank room name")
    public void names() { requireNonblank("roomName"); }

    @Then("each listed room should have a nonblank room type")
    public void types() { requireNonblank("type"); }

    @Then("each listed room should have a positive nightly price")
    public void prices() {
        for (Map<String, Object> room : rooms()) {
            assertInstanceOf(Number.class, room.get("roomPrice"), "Room price must be numeric");
            assertTrue(((Number) room.get("roomPrice")).doubleValue() > 0, "Room price must be positive");
        }
    }

    @Then("each listed room should have an image reference")
    public void images() { requireNonblank("image"); }

    @Then("each listed room should have a facilities list")
    public void facilities() {
        for (Map<String, Object> room : rooms()) {
            assertInstanceOf(List.class, room.get("features"), "Room facilities must be a list");
        }
    }

    @Then("each listed room should include accessibility information")
    public void accessibility() {
        for (Map<String, Object> room : rooms()) {
            assertInstanceOf(Boolean.class, room.get("accessible"), "Accessibility must be true or false");
        }
    }

    @Then("each listed room should have a nonblank description")
    public void descriptions() { requireNonblank("description"); }

    @Then("room names should be unique")
    public void uniqueRoomNames() {
        Set<String> names = new HashSet<>();
        for (Map<String, Object> room : rooms()) {
            Object value = room.get("roomName");
            assertInstanceOf(String.class, value);
            String name = ((String) value).trim();
            assertFalse(name.isBlank(), "Room name must not be blank");
            assertTrue(names.add(name), "Duplicate room name: " + name);
        }
    }

    @Then("every listed facility should have a name")
    public void readableFacilities() {
        for (Map<String, Object> room : rooms()) {
            Object value = room.get("features");
            assertInstanceOf(List.class, value, "Missing facilities for room " + room.get("roomid"));
            for (Object feature : (List<?>) value) {
                assertInstanceOf(String.class, feature, "Facility label must be a string");
                assertFalse(((String) feature).isBlank(), "Blank facility label");
            }
        }
    }

    @Then("every room image reference should have a valid path or URL")
    public void imagePaths() {
        for (Map<String, Object> room : rooms()) {
            Object value = room.get("image");
            assertInstanceOf(String.class, value);
            String path = ((String) value).trim();
            assertTrue(path.startsWith("/") || path.startsWith("https://") || path.startsWith("http://"),
                    "Unexpected image reference: " + path);
        }
    }

    @Then("the room catalogue should match its JSON schema")
    public void roomCatalogueSchema() {
        context.response().then().body(
            io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath(
                "schemas/room-catalogue-schema.json"));
    }

    @Then("room ID {int} should be present in the catalogue")
    public void seededRoomPresent(int expectedId) {
        assertTrue(rooms().stream().anyMatch(room ->
                        room.get("roomid") instanceof Number id && id.intValue() == expectedId),
                "Expected seeded room ID " + expectedId + " in room catalogue");
    }

    @Then("at least one room should have type {string}")
    public void roomTypeAvailable(String expectedType) {
        assertTrue(rooms().stream().anyMatch(room ->
                        expectedType.equals(room.get("type"))),
                "No room of type " + expectedType + " was listed");
    }

    @Then("at least one room should offer the {string} facility")
    public void roomFacilityAvailable(String expectedFacility) {
        assertTrue(rooms().stream().anyMatch(room -> {
                    Object features = room.get("features");
                    return features instanceof List<?> list && list.contains(expectedFacility);
                }), "No room offers facility " + expectedFacility);
    }

    @Then("the room catalogue content type should be JSON")
    public void roomCatalogueJson() {
        assertTrue(context.response().contentType().toLowerCase(java.util.Locale.ROOT).contains("json"),
                "Room catalogue response should be JSON");
    }
}
