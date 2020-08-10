package ru.cft.coi.coordinator.apitests.comments;

import lombok.val;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertNotNull;
import static ru.cft.coi.coordinator.EndPoints.CREATE_COMMENT;

public class CreateCommentTests extends TestBase {

    @Test
    public void createComment_incidentExists_isOk() throws IOException {
        val incidentRequestBody = getJsonResource("createIncident.json").jsonString();
        val incidentResponse = RestApiCoordinator.createIncident(SPEC_VERBOSE, incidentRequestBody);
        val incidentId = incidentResponse.jsonPath().getInt("id");
        val commentText = "Тестовый комментарий к инциденту";

        val commentResponse = given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body(commentText)
                .when()
                .post(CREATE_COMMENT, incidentId)
                .then()
                .statusCode(200)
                .body("incidentId", equalTo(incidentId))
                .body("body", equalTo(commentText))
                .extract()
                .response();

        val commentId = commentResponse.jsonPath().getString("id");
        assertNotNull(commentId);
    }

    @Test
    public void createComment_incidentNotExists_notFoundIncidentToAddComment() {
        val incidentId = 0;

        given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body("Тестовый комментарий к инциденту")
                .when()
                .post(CREATE_COMMENT, incidentId)
                .then()
                .statusCode(404)
                .extract()
                .response();
    }
}
