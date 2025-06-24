package com.redhat.k8sthoughts;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.get;

import static org.hamcrest.collection.IsIn.isIn;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ThoughtOfTheDayResourceEmptyListTest {

    @InjectMock
    ThoughtOfTheDayRepository repository;

    @BeforeEach
    void setUp() {
        // Create test data
        when(repository.listAll()).thenReturn(Collections.emptyList());
    }

    @Test
    void testRandomEndpointNoThoughts() {

        get("/api/thoughts/random").then().assertThat().statusCode(200)
                .body("thought", isIn(ThoughtOfTheDayResource.DEFAULT_THOUGHTS));
    }

}
