package ru.cft.coi.coordinator.apitests.incident;

import com.jayway.restassured.specification.RequestSpecification;
import lombok.val;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static ru.cft.coi.coordinator.EndPoints.GET_INCIDENTS;

public class GetIncidentsTests extends TestBase {

    private RequestSpecification spec;

    @BeforeClass
    public void setup() {
        val realm = UUID.randomUUID().toString();
        val claimsB64 = getUserClaimsAsBase64(realm, DEFAULT_ROLES);
        spec = getRequestSpecification(claimsB64, true);
    }

    @Test(priority = 0)
    public void getIncidents_clientWithoutIncidents_emptyResponseBody() {
        given()
                .log().all()
                .spec(spec)
                .contentType(JSON)
                .when()
                .get(GET_INCIDENTS)
                .then()
                .statusCode(200)
                .body(equalTo("[]"));
    }

    @Test(priority = 1)
    public void getIncidents_clientHasIncident_notEmptyResponseBody() throws IOException {
        val createIncidentRequestBody = getJsonResource("createIncident.json").jsonString();
        val createIncidentResponse = RestApiCoordinator.createIncident(spec, createIncidentRequestBody);
        val incidentId = createIncidentResponse.jsonPath().getInt("id");

        given()
                .log().all()
                .spec(spec)
                .contentType(JSON)
                .when()
                .get(GET_INCIDENTS)
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(incidentId));
    }
}
