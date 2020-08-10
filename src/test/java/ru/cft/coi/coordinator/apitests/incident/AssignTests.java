package ru.cft.coi.coordinator.apitests.incident;

import com.jayway.jsonpath.DocumentContext;
import lombok.val;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class AssignTests extends TestBase {

    private int incidentId;
    private String body;

    @BeforeClass
    public void beforeTests() throws IOException {
        body = getJsonResource("createIncidentHashes.json")
                .set("$.assignee", "selivjorstova")
                .jsonString();

        incidentId = RestApiCoordinator.createIncident(SPEC_VERBOSE, body)
                .jsonPath()
                .getInt("id");
    }

    @Test
    public void sendIncidentOtherAssignMustReturn403() {
        val sendIncidentResponse = RestApiCoordinator.sendIncident(SPEC_VERBOSE, incidentId);

        assertEquals(sendIncidentResponse.statusCode(), 403);
    }

    @Test
    public void updateIncidentOtherAssignMustReturn403() throws IOException {
        val body = getJsonResource("updateIncident.json").jsonString();
        val sendIncidentResponse = RestApiCoordinator.updateIncident(SPEC_VERBOSE, body, incidentId);

        assertEquals(sendIncidentResponse.statusCode(), 403);
    }

    @Test
    public void changeAssignAndSendIncidentMustReturn200() throws IOException {
        val body = getJsonResource("createIncidentHashes.json")
                .set("$.assignee", "selivjorstova")
                .jsonString();

        int incidentId = RestApiCoordinator.createIncident(SPEC_VERBOSE, body)
                .jsonPath()
                .getInt("id");

        RestApiCoordinator.assignIncident(SPEC_VERBOSE, "selivjorstova", "gavrilov", incidentId);

        val sendIncidentResponse = RestApiCoordinator.sendIncident(SPEC_VERBOSE, incidentId);

        assertEquals(sendIncidentResponse.statusCode(), 200);
    }

    @Test
    public void changeAssignAndSendIncidentMustReturn409() {
        val assignIncidentResponse = RestApiCoordinator.assignIncident(SPEC_VERBOSE, "assigneeName", "gavrilov", incidentId);
        assertEquals(assignIncidentResponse.statusCode(), 409);
    }
}
