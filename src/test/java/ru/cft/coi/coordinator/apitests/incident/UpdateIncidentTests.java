package ru.cft.coi.coordinator.apitests.incident;

import lombok.val;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.EndPoints;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

public class UpdateIncidentTests extends TestBase {

    private int incidentId = 0;

    @BeforeClass
    public void setup() throws IOException {
        val requestBody = getJsonResource("createIncidentWithoutAttachment.json").jsonString();
        incidentId = RestApiCoordinator.createIncident(SPEC_VERBOSE, requestBody).jsonPath().getInt("id");
    }

    @Test(priority = 1)
    public void updateIncident_incidentWithoutDraft_notStarted() throws IOException {
        val requestBody = getJsonResource("updateIncidentINT.json").jsonString();

        given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body(requestBody)
                .when()
                .put(EndPoints.UPDATE_INCIDENT, incidentId)
                .then()
                .statusCode(200);

        val getDraftResponse = RestApiCoordinator.getDraftByIncidentId(SPEC_VERBOSE, incidentId);
        val draftId = getDraftResponse.jsonPath().getString("id");

        assertNotNull(draftId);

        val getIncidentResponse = RestApiCoordinator.getIncidentById(SPEC_VERBOSE, incidentId);
        val correspondenceStatus = getIncidentResponse.jsonPath().getString("correspondenceStatus");

        assertEquals(correspondenceStatus, "NotStarted");
    }

    @Test(priority = 2)
    public void updateIncident_incidentHasDraft_isOk() throws IOException {
        val requestBody = getJsonResource("updateIncident.json").jsonString();

        given()
                .spec(SPEC)
                .contentType(JSON)
                .body(requestBody)
                .when()
                .put(EndPoints.UPDATE_INCIDENT, incidentId)
                .then()
                .statusCode(200);

        val getDraftResponse = RestApiCoordinator.getDraftByIncidentId(SPEC_VERBOSE, incidentId);
        val draftId = getDraftResponse.jsonPath().getString("id");

        assertNotNull(draftId);

        val getIncidentResponse = RestApiCoordinator.getIncidentById(SPEC_VERBOSE, incidentId);
        val correspondenceStatus = getIncidentResponse.jsonPath().getString("correspondenceStatus");

        assertEquals(correspondenceStatus, "NotStarted");
    }
}
