package ru.cft.coi.coordinator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.LogDetail;
import com.jayway.restassured.specification.RequestSpecification;
import lombok.val;
import ru.cft.coi.coordinator.dto.IncidentAttachment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

public abstract class TestBase {

    protected static final String DEFAULT_REALM = "coi";
    protected static final String DEFAULT_USERNAME = "gavrilov";
    protected static final String DEFAULT_ROLES = "incidents_create,incidents_send,incidents_read,incidents_update";

    protected static final RequestSpecification SPEC;
    protected static final RequestSpecification SPEC_VERBOSE;

    private static final Configuration CONF = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider())
            .build();

    private static final Random RND = new Random();

    static {
        val claimsB64 = getUserClaimsAsBase64(DEFAULT_REALM, DEFAULT_ROLES);
        SPEC = getRequestSpecification(claimsB64, false);
        SPEC_VERBOSE = getRequestSpecification(claimsB64, true);
    }

    protected static String getUserClaimsAsBase64(String realm, String roles) {
        val claims = new UserClaims(
                realm,
                DEFAULT_USERNAME,
                Arrays.asList(roles.split(","))
        );
        try {
            val claimsJson = new ObjectMapper().writeValueAsString(claims);
            return Base64.getEncoder().encodeToString(claimsJson.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static RequestSpecification getRequestSpecification(String claimsB64, boolean logBody) {
        val specBuilder = new RequestSpecBuilder()
                .setBaseUri(getUrlCoordinator())
                .addHeader("X-User-Claims", claimsB64)
                .log(LogDetail.METHOD)
                .log(LogDetail.PATH)
                .log(LogDetail.PARAMS)
                .log(LogDetail.HEADERS);

        if (logBody) {
            specBuilder.log(LogDetail.BODY);
        }

        return specBuilder.build();
    }

    protected static String getUrlCoordinator() {
        val props = getProperties();
        val envValue = System.getenv("URL_COORDINATOR");
        return (envValue != null) ? envValue : props.getProperty("URL_COORDINATOR");
    }

    private static Properties getProperties() {
        val path = Paths.get("src", "test", "resources")
                .resolve("env.properties")
                .toAbsolutePath()
                .toString();

        val props = new Properties();

        try (val stream = new FileInputStream(path)) {
            props.load(stream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return props;
    }

    protected DocumentContext getJsonResource(String name) throws IOException {
        val loader = TestBase.class.getClassLoader();
        val file = new File(loader.getResource("json/" + name).getFile());
        return JsonPath.using(CONF).parse(file);
    }

    protected IncidentAttachment generateIncidentAttachment(int index, int fileSize) {
        return IncidentAttachment.builder()
                .sourceId(UUID.randomUUID())
                .datetimeAt("2020-03-12T06:07:22.265Z")
                .file(generateAttachmentFile(index, fileSize))
                .build();
    }

    private IncidentAttachment.File generateAttachmentFile(int index, int fileSize) {
        return IncidentAttachment.File.builder()
                .name(String.format("TestFile%d.txt", index + 1))
                .size(fileSize)
                .base64(generateBase64String(fileSize))
                .build();
    }

    private String generateBase64String(int count) {
        val bytes = new byte[count];
        RND.nextBytes(bytes);
        val bytesB64 = Base64.getEncoder().encode(bytes);
        return new String(bytesB64);
    }
}
