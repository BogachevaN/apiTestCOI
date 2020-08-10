package ru.cft.coi.coordinator.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class IncidentAttachment {
    private final UUID sourceId;
    private final String datetimeAt;
    private final File file;

    @Data
    @Builder
    public static class File {
        private final String name;
        private final int size;
        private final String base64;
    }
}
