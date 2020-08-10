package ru.cft.coi.coordinator.apitests.incident;

import com.jayway.restassured.response.Response;
import lombok.val;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.DataProviderCreateIncident;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static ru.cft.coi.coordinator.EndPoints.CREATE_INCIDENT;

public class CreateIncidentTests extends TestBase {

    @Test
    public void createIncident_withoutDraft_correspondenceNotStarted() throws IOException {
        val requestBody = getJsonResource("createIncidentWithoutAttachment.json").jsonString();

        given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post(CREATE_INCIDENT)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("realmName", equalTo(DEFAULT_REALM))
                .body("correspondenceStatus", equalTo("NotStarted"))
                .body("status", equalTo("InProgress"))
                .extract()
                .response();
    }

    @Test
    public void createIncident_withoutAssignee_incidentIsNew() throws IOException {
        val requestBody = getJsonResource("createIncident.json").jsonString();

        given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post(CREATE_INCIDENT)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("realmName", equalTo(DEFAULT_REALM))
                .body("correspondenceStatus", equalTo("NotStarted"))
                .body("status", equalTo("New"))
                .extract()
                .response();
    }

    @Test
    public void createIncident_withDraftAndAssignee_NotStartedAndIncidentInProgress() throws IOException {
        val requestBody = getJsonResource("createIncidentINT.json").jsonString();

        given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post(CREATE_INCIDENT)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("realmName", equalTo(DEFAULT_REALM))
                .body("correspondenceStatus", equalTo("NotStarted"))
                .body("status", equalTo("InProgress"))
                .extract()
                .response();
    }

    @Test(dataProvider = "correctNumPassport", dataProviderClass = DataProviderCreateIncident.class)
    public void createIncident_withNormalNumPassportAndSnils_HashesFormed(String body) {
        val responseCreateIncident = (Response) given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body(body)
                .when()
                .post(CREATE_INCIDENT);

        assertEquals(responseCreateIncident.statusCode(), 200);
        Response responseGetIncident = RestApiCoordinator.getDraftByIncidentId(SPEC_VERBOSE, responseCreateIncident.jsonPath().get("id"));
        String hash = responseGetIncident.jsonPath().get("incident.antifraud.transfers[0].payer.hashes[0].hash");
        String hashSnils = responseGetIncident.jsonPath().get("incident.antifraud.transfers[0].payer.hashes[0].hashSnils");
        assertEquals(hash.length(), 64);
        assertEquals(hashSnils.length(), 64);
    }

    @Test(dataProvider = "incorrectNumPassportAndSnils", dataProviderClass = DataProviderCreateIncident.class)
    public void createIncident_withIncorrectNumPassportAndSnils_HashesFormed(String body) {
        val responseCreateIncident = (Response) given()
                .spec(SPEC_VERBOSE)
                .contentType(JSON)
                .body(body)
                .when()
                .post(CREATE_INCIDENT);

        assertEquals(responseCreateIncident.statusCode(), 400);
    }


}
