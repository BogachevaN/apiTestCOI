package ru.cft.coi.coordinator.apitests.incident;

import lombok.val;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static ru.cft.coi.coordinator.EndPoints.GET_INCIDENT_BY_ID;

public class GetIncidentByIdTests extends TestBase {

    @Test
    public void getIncidentById_incidentExists_isOk() throws IOException {
        val createIncidentRequestBody = getJsonResource("createIncident.json").jsonString();
        val createIncidentResponse = RestApiCoordinator.createIncident(SPEC_VERBOSE, createIncidentRequestBody);
        val incidentId = createIncidentResponse.jsonPath().getInt("id");

        given()
                .log().all()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .when()
                .get(GET_INCIDENT_BY_ID, incidentId)
                .then()
                .statusCode(200)
                .body("id", equalTo(incidentId));
    }

    @Test
    public void getIncidentById_incidentNotExists_notFoundIncident() {
        val incidentId = 0;

        given()
                .log().all()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .when()
                .get(GET_INCIDENT_BY_ID, incidentId)
                .then()
                .assertThat().statusCode(404);
    }

    @Test
    public void getIncidentById_incorrectId_badRequest() {
        given()
                .log().all()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .when()
                .get(GET_INCIDENT_BY_ID, "test")
                .then()
                .assertThat().statusCode(400);
    }
}

