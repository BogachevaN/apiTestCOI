package ru.cft.coi.coordinator.apitests.comments;

import lombok.val;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static ru.cft.coi.coordinator.EndPoints.GET_COMMENTS_BY_INCIDENT_ID;

public class GetCommentsByIncidentIdTests extends TestBase {

    private int incidentId = 0;

    @BeforeClass
    public void setup() throws IOException {
        val requestBody = getJsonResource("createIncident.json").jsonString();
        incidentId = RestApiCoordinator.createIncident(SPEC_VERBOSE, requestBody).jsonPath().getInt("id");
    }

    @Test(priority = 1)
    public void getComment_incidentWithoutComment_emptyResponseBody() {
        given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .when()
                .get(GET_COMMENTS_BY_INCIDENT_ID, incidentId)
                .then()
                .statusCode(200)
                .body(equalTo("[]"));
    }

    @Test(priority = 2)
    public void getComment_incidentHasComment_notEmptyResponseBody() {
        val commentText = "Тестовый комментарий к инциденту";
        val commentResponse = RestApiCoordinator.createComment(SPEC_VERBOSE, incidentId, commentText);
        val commentId = commentResponse.jsonPath().getInt("id");

        given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .when()
                .get(GET_COMMENTS_BY_INCIDENT_ID, incidentId)
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(commentId))
                .body("[0].incidentId", equalTo(incidentId))
                .body("[0].body", equalTo(commentText));

    }

    @Test
    public void getComment_incorrectIncidentId_badRequest() {
        given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .when()
                .get(GET_COMMENTS_BY_INCIDENT_ID, "test")
                .then()
                .statusCode(400);
    }
}
