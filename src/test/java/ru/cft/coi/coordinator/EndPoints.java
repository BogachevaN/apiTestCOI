package ru.cft.coi.coordinator;

public class EndPoints {
    public static final String CREATE_INCIDENT = "/coordinator/incident";
    public static final String UPDATE_INCIDENT = "/coordinator/incident/{id}";
    public static final String SEND_INCIDENT = "/coordinator/incident/{id}/send";
    public static final String ASSIGN_INCIDENT = "/coordinator/incident/{id}/assign";
    public static final String GET_INCIDENT_BY_ID = "/coordinator/incident/{id}";
    public static final String GET_INCIDENTS = "/coordinator/incident";
    public static final String CREATE_COMMENT = "/coordinator/incident/{id}/comment";
    public static final String GET_COMMENTS_BY_INCIDENT_ID = "/coordinator/incident/{id}/comment";
    public static final String CREATE_DRAFT = "/coordinator/incident/{id}/draft";
    public static final String UPDATE_DRAFT_BY_INCIDENT_ID = "/coordinator/incident/{id}/draft";
    public static final String GET_DRAFT_BY_INCIDENT_ID = "/coordinator/incident/{id}/draft";
    public static final String ARCHIVE_INCIDENT = "/coordinator/incident/{id}/archive";
    public static final String UNARCHIVE_INCIDENT = "/coordinator/incident/{id}/unarchive";
    public static final String IMPORT_INCIDENT = "/coordinator/incident/import";
}
