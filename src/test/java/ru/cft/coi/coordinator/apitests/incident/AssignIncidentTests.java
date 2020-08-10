package ru.cft.coi.coordinator.apitests.incident;

import lombok.val;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.testng.Assert.assertEquals;
import static ru.cft.coi.coordinator.EndPoints.ASSIGN_INCIDENT;

public class AssignIncidentTests extends TestBase {

    private int incidentId = 0;

    @BeforeClass
    public void setup() throws IOException {
        val requestBody = getJsonResource("createIncident.json").jsonString();
        incidentId = RestApiCoordinator.createIncident(SPEC_VERBOSE, requestBody)
                .jsonPath()
                .getInt("id");
    }

    @Test(priority = 1)
    public void assignIncident_incidentIsNew_incidentInProgressAndHasAssignee() {
        given()
                .log().all()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body("{\"from\": null, \"to\":\"selivjorstova\"}")
                .when()
                .post(ASSIGN_INCIDENT, incidentId)
                .then()
                .statusCode(200);

        val getIncidentResponse = RestApiCoordinator.getIncidentById(SPEC_VERBOSE, incidentId);

        assertEquals(getIncidentResponse.jsonPath().getString("assignee"), "selivjorstova");
        assertEquals(getIncidentResponse.jsonPath().getString("status"), "InProgress");
    }

    @Test(priority = 2)
    public void assignIncident_incidentInProgressAndHasAssignee_newAssignee() {
        given()
                .log().all()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body("{\"from\": \"selivjorstova\", \"to\":\"gavrilov\"}")
                .when()
                .post(ASSIGN_INCIDENT, incidentId)
                .then()
                .statusCode(200);

        val getIncidentResponse = RestApiCoordinator.getIncidentById(SPEC_VERBOSE, incidentId);

        assertEquals(getIncidentResponse.jsonPath().getString("assignee"), "gavrilov");
        assertEquals(getIncidentResponse.jsonPath().getString("status"), "InProgress");
    }

    @Test
    public void assignIncident_incidentNotExists_notFoundIncidentToAssign() {
        given()
                .log().all()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body("{\"from\":null,\"to\":\"gavrilov\"}")
                .when()
                .post(ASSIGN_INCIDENT, 0)
                .then()
                .statusCode(404);
    }

    @Test
    public void assignIncident_incorrectIncidentId_badRequest() {
        given()
                .log().all()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body("\"from\":null,{\"to\":\"gavrilov\"}")
                .when()
                .post(ASSIGN_INCIDENT, "test")
                .then()
                .statusCode(400);
    }
}
