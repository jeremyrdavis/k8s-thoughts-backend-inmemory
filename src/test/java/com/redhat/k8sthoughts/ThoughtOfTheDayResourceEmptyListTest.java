package com.redhat.k8sthoughts;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class ThoughtOfTheDayResourceEmptyListTest {

    @Test
    void testRandomEndpointWithDefaultThoughts() {
        // Test that the random endpoint returns a valid thought
        given()
          .when().get("/api/thoughts/random")
          .then()
             .statusCode(200)
             .body("thought", notNullValue())
             .body("author", notNullValue());
    }

    @Test
    void testListEndpointWithDefaultThoughts() {
        given()
          .when().get("/api/thoughts")
          .then()
             .statusCode(200)
             .body("size()", greaterThan(0)) // Should have at least one thought
             .body("thought", notNullValue());
    }
} 