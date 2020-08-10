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

public class AnotherRealmTests extends TestBase {

    private final RequestSpecification anotherRealmSpec = new RequestSpecBuilder()
            .setBaseUri(getUrlCoordinator())
            .addHeader("X-User-Claims", getUserClaimsAsBase64("another-realm", DEFAULT_ROLES))
            .log(LogDetail.ALL)
            .build();

    @Test
    public void testWhenGetIncidentFromAnotherRealmThenForbidden() throws IOException {
        val createIncidentRequest = getJsonResource("createIncident.json");
        val createIncidentResponse = RestApiCoordinator.createIncident(SPEC_VERBOSE, createIncidentRequest.jsonString());
        val incidentId = createIncidentResponse.jsonPath().getInt("id");

        RestApiCoordinator.getIncidentById(anotherRealmSpec, incidentId)
                .then()
                .statusCode(403)
                .body("code", equalTo("ACCESS_DENIED"));
    }

    @Test
    public void testWhenUpdateIncidentFromAnotherRealmThenForbidden() throws IOException {
        val createIncidentRequest = getJsonResource("createIncident.json");
        val createIncidentResponse = RestApiCoordinator.createIncident(SPEC_VERBOSE, createIncidentRequest.jsonString());
        val incidentId = createIncidentResponse.jsonPath().getInt("id");
        val updateIncidentRequest = getJsonResource("updateIncident.json");

        RestApiCoordinator.updateIncident(anotherRealmSpec, updateIncidentRequest.jsonString(), incidentId)
                .then()
                .statusCode(403)
                .body("code", equalTo("ACCESS_DENIED"));
    }

    @Test
    public void testWhenSendIncidentFromAnotherRealmThenForbidden() throws IOException {
        val createIncidentRequest = getJsonResource("createIncident.json");
        val createIncidentResponse = RestApiCoordinator.createIncident(SPEC_VERBOSE, createIncidentRequest.jsonString());
        val incidentId = createIncidentResponse.jsonPath().getInt("id");

        RestApiCoordinator.sendIncident(anotherRealmSpec, incidentId)
                .then()
                .statusCode(403)
                .body("code", equalTo("ACCESS_DENIED"));
    }
}
