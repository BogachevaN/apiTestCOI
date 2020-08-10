package ru.cft.coi.coordinator.apitests.draft;

import lombok.val;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.cft.coi.coordinator.EndPoints.GET_DRAFT_BY_INCIDENT_ID;

public class GetDraftByIncidentIdTests extends TestBase {

    @Test
    public void getDraftByIncidentId_draftNotExists_notFoundIncidentToGetDraft() {
        given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .when()
                .get(GET_DRAFT_BY_INCIDENT_ID, 0)
                .then()
                .statusCode(404)
                .body("code", equalTo("DRAFT_NOT_FOUND"));
    }

    @Test
    public void getDraftByIncidentId_incidentHasDraft_isOk() throws IOException {
        val incidentRequestBody = getJsonResource("createIncident.json").jsonString();
        val incidentResponse = RestApiCoordinator.createIncident(SPEC_VERBOSE, incidentRequestBody);
        val incidentId = incidentResponse.jsonPath().getInt("id");

        given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .when()
                .get(GET_DRAFT_BY_INCIDENT_ID, incidentId)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("incidentId", equalTo(incidentId));
    }

    @Test
    public void getDraftByIncidentId_incorrectIncidentId_badRequest() {
        given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .when()
                .get(GET_DRAFT_BY_INCIDENT_ID, "test")
                .then()
                .statusCode(400);
    }
}
