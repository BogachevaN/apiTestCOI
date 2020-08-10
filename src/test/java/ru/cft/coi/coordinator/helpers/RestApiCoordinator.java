package ru.cft.coi.coordinator.helpers;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static ru.cft.coi.coordinator.EndPoints.*;

public class RestApiCoordinator {

    public static Response createIncident(RequestSpecification spec, String body) {
        return given()
                .spec(spec)
                .contentType(JSON)
                .body(body)
                .when()
                .post(CREATE_INCIDENT);
    }

    public static Response getIncidentById(RequestSpecification spec, int incidentId) {
        return given()
                .spec(spec)
                .contentType(JSON)
                .when()
                .get(GET_INCIDENT_BY_ID, incidentId);
    }


    public static Response createComment(RequestSpecification spec, int incidentId, String commentText) {
        return given()
                .spec(spec)
                .contentType(JSON)
                .body(commentText)
                .when()
                .post(CREATE_COMMENT, incidentId);
    }

    public static Response createDraft(RequestSpecification spec, int incidentId, String draft) {
        return given()
                .spec(spec)
                .contentType(JSON)
                .body(draft)
                .when()
                .post(CREATE_DRAFT, incidentId);
    }

    public static Response getDraftByIncidentId(RequestSpecification spec, int incidentId) {
        return given()
                .spec(spec)
                .contentType(JSON)
                .when()
                .get(GET_DRAFT_BY_INCIDENT_ID, incidentId);
    }

    public static Response sendIncident(RequestSpecification spec, int incidentId) {
        return given()
                .spec(spec)
                .contentType(JSON)
                .when()
                .post(SEND_INCIDENT, incidentId);
    }

    public static Response assignIncident(RequestSpecification spec, String fromAssignee, String toAssignee, int incidentId) {
        return given()
                .spec(spec)
                .contentType(JSON)
                .body("{\"from\":\"" + fromAssignee + "\"," + "\"to\":\"" + toAssignee + "\"}")
                .when()
                .post(ASSIGN_INCIDENT, incidentId);

    }

    public static Response updateIncident(RequestSpecification spec, String requestBody, int incidentId) {
        return given()
                .spec(spec)
                .contentType(JSON)
                .body(requestBody)
                .when()
                .put(UPDATE_INCIDENT, incidentId);
    }

    public static Response updateDraft(RequestSpecification spec, int incidentId, String draftBody) {
        return given()
                .spec(spec)
                .contentType(JSON)
                .body(draftBody)
                .when()
                .put(CREATE_DRAFT, incidentId);
    }

    public static Response archiveIncident(RequestSpecification spec, int incidentId) {
        return given()
                .spec(spec)
                .contentType(JSON)
                .when()
                .post(ARCHIVE_INCIDENT, incidentId);
    }

    public static Response unarchiveIncident(RequestSpecification spec, int incidentId) {
        return given()
                .spec(spec)
                .contentType(JSON)
                .when()
                .post(UNARCHIVE_INCIDENT, incidentId);
    }

    public static Response importIncident(RequestSpecification spec, String requestBody){
        return given()
                    .spec(spec)
                    .contentType(JSON)
                    .body(requestBody)
                    .when()
                    .post(IMPORT_INCIDENT);
    }

}
