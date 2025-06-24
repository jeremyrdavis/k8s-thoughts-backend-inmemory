package com.redhat.k8sthoughts;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsIn.isIn;

@QuarkusTest
class ThoughtOfTheDayResourceTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String CURRENT_DATE = LocalDate.now().format(DATE_FORMATTER);

    @BeforeEach
    void setUp() {
        // Reset the DEFAULT_THOUGHTS to known state for testing
        // Note: In a real scenario, you might want to use @DirtiesContext or similar
        // to reset the static state between tests
    }

    @Test
    void testListEndpoint() {
        given()
          .when().get("/api/thoughts")
          .then()
             .statusCode(200)
             .body("size()", greaterThan(0)) // Should have at least one thought
             .body("thought", notNullValue())
             .body("author", notNullValue());
    }

    @Test
    void testGetEndpoint() {
        given()
          .when().get("/api/thoughts/1")
          .then()
             .statusCode(200)
             .body("thought", notNullValue())
             .body("author", notNullValue());
    }

    @Test
    void testGetEndpointNotFound() {
        given()
          .when().get("/api/thoughts/9999")
          .then()
             .statusCode(404);
    }

    @Test
    void testRandomEndpoint() {
        given()
          .when().get("/api/thoughts/random")
          .then()
             .statusCode(200)
             .body("thought", notNullValue())
             .body("author", notNullValue());
    }

    @Test
    void testCreateEndpoint() {
        ThoughtOfTheDay newThought = new ThoughtOfTheDay();
        newThought.thought = "New test thought";
        newThought.author = "New Author";
        newThought.day = LocalDate.now();

        given()
          .contentType(ContentType.JSON)
          .body(newThought)
          .when().post("/api/thoughts")
          .then()
             .statusCode(201)
             .body("thought", is("New test thought"))
             .body("author", is("New Author"));
    }

    @Test
    void testUpdateEndpoint() {
        ThoughtOfTheDay updatedThought = new ThoughtOfTheDay();
        updatedThought.thought = "Updated thought";
        updatedThought.author = "Updated Author";
        updatedThought.day = LocalDate.now();

        given()
          .contentType(ContentType.JSON)
          .body(updatedThought)
          .when().put("/api/thoughts/1")
          .then()
             .statusCode(200)
             .body("thought", is("Updated thought"))
             .body("author", is("Updated Author"));
    }

    @Test
    void testUpdateEndpointNotFound() {
        ThoughtOfTheDay updatedThought = new ThoughtOfTheDay();
        updatedThought.thought = "Updated thought";
        updatedThought.author = "Updated Author";

        given()
          .contentType(ContentType.JSON)
          .body(updatedThought)
          .when().put("/api/thoughts/9998")
          .then()
             .statusCode(404);
    }

    @Test
    void testDeleteEndpoint() {
        given()
          .when().delete("/api/thoughts/2")
          .then()
             .statusCode(204);
    }

    @Test
    void testDeleteEndpointNotFound() {
        given()
          .when().delete("/api/thoughts/9999")
          .then()
             .statusCode(404);
    }
} 