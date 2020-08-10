package ru.cft.coi.coordinator.apitests.incident;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import lombok.val;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;

import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ImportIncidentTests extends TestBase {
    @Test
    public void importIncidentMustCreateIncident() throws IOException {
        val requestBody = getJsonResource("import200.json").jsonString();
        Response response = RestApiCoordinator.importIncident(SPEC, requestBody);
        assertEquals(response.statusCode(), 200);
        int id = response.jsonPath().get("id");

        Response draft = RestApiCoordinator.getDraftByIncidentId(SPEC, id);
        JsonPath json = draft.jsonPath();
        assertEquals(json.get("header.schemaVersion"), "2.3");
        assertEquals(json.get("incident.antifraud.transfers[0].payer.orgType"), "individual");
    }

    @Test
    public void importIncidentWithBadFieldMustReturn400() throws IOException {
        val requestBody = getJsonResource("import400.json").jsonString();
        Response response = RestApiCoordinator.importIncident(SPEC, requestBody);
        assertEquals(response.statusCode(), 400);
        assertEquals(response.jsonPath().get("code"), "INCIDENT_IMPORT_ERROR");
    }
}
