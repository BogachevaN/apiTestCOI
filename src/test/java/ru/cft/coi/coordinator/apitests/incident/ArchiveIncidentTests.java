package ru.cft.coi.coordinator.apitests.incident;

import lombok.val;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class ArchiveIncidentTests extends TestBase {

    @Test
    public void archiveIncidentMustArchivedStatus() throws IOException {
        val requestBody = getJsonResource("createIncidentEXT.json")
                .set("theme", "Тест архивация инцидента")
                .set("assignee", DEFAULT_USERNAME)
                .jsonString();

        int incidentId = RestApiCoordinator.createIncident(SPEC, requestBody).jsonPath().getInt("id");

        assertEquals(RestApiCoordinator.archiveIncident(SPEC, incidentId).statusCode(), 204);
        assertEquals(RestApiCoordinator.getIncidentById(SPEC, incidentId).jsonPath().getString("status"), "Archived");
    }

    @Test
    public void unarchiveIncidentMustInProgressStatus() throws IOException {
        val requestBody = getJsonResource("createIncidentEXT.json")
                .set("theme", "Тест архивация инцидента")
                .set("assignee", "selivjorstova")
                .jsonString();

        int incidentId = RestApiCoordinator.createIncident(SPEC, requestBody)
                .jsonPath()
                .getInt("id");

        RestApiCoordinator.archiveIncident(SPEC, incidentId);

        assertEquals(RestApiCoordinator.unarchiveIncident(SPEC, incidentId).statusCode(), 204);

        val incidentJsonPath = RestApiCoordinator.getIncidentById(SPEC, incidentId).jsonPath();

        assertEquals(incidentJsonPath.getString("status"), "InProgress");
        assertEquals(incidentJsonPath.getString("assignee"), "selivjorstova");

    }

    @Test
    public void archiveIncidentConflictAssigneeMustError() throws IOException {
        val requestBody = getJsonResource("createIncidentEXT.json")
                .set("theme", "Тест архивация инцидента")
                .set("assignee", "selivjorstova")
                .jsonString();

        int incidentId = RestApiCoordinator.createIncident(SPEC, requestBody)
                .jsonPath()
                .getInt("id");

        assertEquals(RestApiCoordinator.archiveIncident(SPEC, incidentId).statusCode(), 403);
    }

    @Test
    public void archiveIncidentAssigneeNullMustError() throws IOException {
        val requestBody = getJsonResource("createIncident.json")
                .set("theme", "Тест архивация инцидента")
                .jsonString();

        int incidentId = RestApiCoordinator.createIncident(SPEC, requestBody)
                .jsonPath()
                .getInt("id");

        assertEquals(RestApiCoordinator.archiveIncident(SPEC, incidentId).statusCode(), 403);
    }
}
