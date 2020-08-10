package ru.cft.coi.coordinator.apitests.incident;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.LogDetail;
import com.jayway.restassured.specification.RequestSpecification;
import lombok.val;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;

public class MissingRoleTests extends TestBase {

    private final RequestSpecification noRolesSpec = new RequestSpecBuilder()
            .setBaseUri(getUrlCoordinator())
            .addHeader("X-User-Claims", getUserClaimsAsBase64("coi", ""))
            .log(LogDetail.ALL)
            .build();

    @Test
    public void testWhenCreateIncidentAndNoCreateRoleThenMissingRole() throws IOException {
        val createIncidentRequest = getJsonResource("createIncident.json");

        RestApiCoordinator.createIncident(noRolesSpec, createIncidentRequest.jsonString())
                .then()
                .statusCode(403)
                .body("code", equalTo("MISSING_ROLE"));
    }

    @Test
    public void testWhenGetIncidentAndNoReadRoleThenMissingRole() throws IOException {
        val createIncidentRequest = getJsonResource("createIncident.json");
        val createIncidentResponse = RestApiCoordinator.createIncident(SPEC_VERBOSE, createIncidentRequest.jsonString());
        val incidentId = createIncidentResponse.jsonPath().getInt("id");

        RestApiCoordinator.getIncidentById(noRolesSpec, incidentId)
                .then()
                .statusCode(403)
                .body("code", equalTo("MISSING_ROLE"));
    }

    @Test
    public void testWhenUpdateIncidentAndNoUpdateRoleThenMissingRole() throws IOException {
        val createIncidentRequest = getJsonResource("createIncident.json");
        val createIncidentResponse = RestApiCoordinator.createIncident(SPEC_VERBOSE, createIncidentRequest.jsonString());
        val incidentId = createIncidentResponse.jsonPath().getInt("id");
        val updateIncidentRequest = getJsonResource("updateIncident.json");

        RestApiCoordinator.updateIncident(noRolesSpec, updateIncidentRequest.jsonString(), incidentId)
                .then()
                .statusCode(403)
                .body("code", equalTo("MISSING_ROLE"));
    }

    @Test
    public void testWhenSendIncidentAndNoSendRoleThenMissingRole() throws IOException {
        val createIncidentRequest = getJsonResource("createIncident.json");
        val createIncidentResponse = RestApiCoordinator.createIncident(SPEC_VERBOSE, createIncidentRequest.jsonString());
        val incidentId = createIncidentResponse.jsonPath().getInt("id");

        RestApiCoordinator.sendIncident(noRolesSpec, incidentId)
                .then()
                .statusCode(403)
                .body("code", equalTo("MISSING_ROLE"));
    }
}
