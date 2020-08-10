package ru.cft.coi.coordinator.apitests.incident;

import com.jayway.restassured.response.Response;
import lombok.val;
import org.testng.annotations.Test;
import ru.cft.coi.coordinator.TestBase;
import ru.cft.coi.coordinator.helpers.DataProviderCreateIncident;
import ru.cft.coi.coordinator.helpers.RestApiCoordinator;

import java.io.IOException;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

public class AttachmentsTests extends TestBase {

    @Test(dataProvider = "correctAttachments", dataProviderClass = DataProviderCreateIncident.class)
    public void createAndSendIncidentWithAttachmentMustReturn200(int attachmentCount, int fileSize) throws IOException {
        val ctx = getJsonResource("createIncidentWithAttachments.json")
                .set("$.attachment.header.sourceId", UUID.randomUUID());

        for (int i = 0; i < attachmentCount; ++i) {
            ctx.add("$.attachment.incident.attachments", generateIncidentAttachment(i, fileSize));
        }

        Response createIncidentResponse = RestApiCoordinator.createIncident(SPEC, ctx.jsonString());
        assertEquals(createIncidentResponse.statusCode(), 200);

        int incidentId = createIncidentResponse.jsonPath().getInt("id");
        Response sendIncidentResponse = RestApiCoordinator.sendIncident(SPEC_VERBOSE, incidentId);
        assertEquals(sendIncidentResponse.statusCode(), 200);
    }

    @Test(dataProvider = "incorrectAttachments", dataProviderClass = DataProviderCreateIncident.class)
    public void createAndSendIncidentWithIncorrectAttachmentMustReturn400(int attachmentCount, int fileSize) throws IOException {
        val ctx = getJsonResource("createIncidentWithAttachments.json")
                .set("$.attachment.header.sourceId", UUID.randomUUID());

        for (int i = 0; i < attachmentCount; ++i) {
            ctx.add("$.attachment.incident.attachments", generateIncidentAttachment(i, fileSize));
        }

        Response createIncidentResponse = RestApiCoordinator.createIncident(SPEC, ctx.jsonString());
        assertEquals(createIncidentResponse.statusCode(), 400);
    }
}
