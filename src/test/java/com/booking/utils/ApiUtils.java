package com.booking.utils;

import com.booking.models.LoginRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public final class ApiUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ApiUtils.class);

    private ApiUtils() {
    }

    private static RequestSpecification request(String token) {
        RequestSpecification spec = given()
                .baseUri(Config.get("base.url"))
                .accept(ContentType.JSON);
        if (token != null && !token.isBlank()) {
            spec.cookie("token", token);
        }
        return spec;
    }

    public static Response get(String path, String token) {
        return record("GET", path, request(token).get(path));
    }

    public static Response post(String path, Object body) {
        RequestSpecification spec = request(null).contentType(ContentType.JSON);
        if (body != null) {
            spec.body(body);
        }
        return record("POST", path, spec.post(path));
    }

    public static Response put(String path, Object body, String token) {
        return record("PUT", path, request(token).contentType(ContentType.JSON).body(body).put(path));
    }

    public static Response patch(String path, Object body, String token) {
        return record("PATCH", path, request(token).contentType(ContentType.JSON).body(body).patch(path));
    }

    public static Response delete(String path, String token) {
        return record("DELETE", path, request(token).delete(path));
    }

    public static String loginToken() {
        LoginRequest credentials = new LoginRequest(Config.get("auth.username"), Config.get("auth.password"));
        Response response = post("/auth/login", credentials);
        if (response.statusCode() != 200) {
            throw new IllegalStateException("Authentication failed (HTTP " + response.statusCode() + "): " + response.asString());
        }
        String token = response.jsonPath().getString("token");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Authentication response did not contain a token");
        }
        return token;
    }

    private static Response record(String method, String path, Response response) {
        LOG.info("{} {} returned HTTP {}", method, path, response.statusCode());
        return response;
    }
}
