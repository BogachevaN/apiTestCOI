package ru.cft.coi.coordinator.apitests.incident;

import lombok.val;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

public class SendIncidentTests extends TestBase {

    @Test
    public void createIncidentVectorExtMustIncidentCreated() throws IOException {
        val requestBody = getJsonResource("createIncidentEXT.json")
                .set("$.attachment.incident.antifraud.transfers[0].sourceId", UUID.randomUUID())
                .jsonString();

        val incidentId = RestApiCoordinator.createIncident(SPEC_VERBOSE, requestBody)
                .jsonPath()
                .getInt("id");

        val sendIncidentResponse = RestApiCoordinator.sendIncident(SPEC_VERBOSE, incidentId);

        assertEquals(sendIncidentResponse.statusCode(), 200);
    }

    @Test
    public void createAndSendIncidentWithoutRequiredFieldsMustReturn409() throws IOException {
        val requestBody = getJsonResource("createIncidentWithoutAttachment.json").jsonString();
        val createIncidentResponse = RestApiCoordinator.createIncident(SPEC_VERBOSE, requestBody);

        assertEquals(createIncidentResponse.statusCode(), 200);

        val incidentId = createIncidentResponse.jsonPath().getInt("id");
        val sendIncidentResponse = RestApiCoordinator.sendIncident(SPEC_VERBOSE, incidentId);

        assertEquals(sendIncidentResponse.statusCode(), 409);
    }
}
