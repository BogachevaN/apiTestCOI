package ru.cft.coi.coordinator.helpers;

import org.testng.annotations.DataProvider;
import ru.cft.coi.coordinator.TestBase;

import java.io.IOException;

public class DataProviderCreateIncident extends TestBase {

    @DataProvider(name = "correctNumPassport")
    public Object[][] correctNumPassport() throws IOException {
        return new Object[][]{
                {getJsonResource("createIncidentHashes.json").set("$.attachment.incident.antifraud.transfers[0].payer.hashes[0].hash", "5423123456").jsonString()},
                {getJsonResource("createIncidentHashes.json").set("$.attachment.incident.antifraud.transfers[0].payer.hashes[0].hash", "5423 123456").jsonString()},
                {getJsonResource("createIncidentHashes.json").set("$.attachment.incident.antifraud.transfers[0].payer.hashes[0].hash", "54 23 123456").jsonString()}
        };
    }

    @DataProvider(name = "incorrectNumPassportAndSnils")
    public Object[][] incorrectNumPassport() throws IOException {
        return new Object[][]{
                {getJsonResource("createIncidentHashes.json").set("$.attachment.incident.antifraud.transfers[0].payer.hashes[0].hash", "5423 123 456").jsonString()},
                {getJsonResource("createIncidentHashes.json").set("$.attachment.incident.antifraud.transfers[0].payer.hashes[0].hash", "5423123456GTUY").jsonString()},
                {getJsonResource("createIncidentHashes.json").set("$.attachment.incident.antifraud.transfers[0].payer.hashes[0].hashSnils", "54231234567TUYf").jsonString()},
                {getJsonResource("createIncidentHashes.json").set("$.attachment.incident.antifraud.transfers[0].payer.hashes[0].hash", "63640264849a87c90356129d99ea165e37aa5fabc1fea46906df1a7ca50db492e").jsonString()},
                {getJsonResource("createIncidentHashes.json").set("$.attachment.incident.antifraud.transfers[0].payer.hashes[0].hashSnils", "63640264849a87c90356129d99ea165e37aa5fabc1fea46906df1a7ca50db492e").jsonString()}
        };
    }

    @DataProvider(name = "correctAttachments")
    public Object[][] provideCorrectAttachments() {
        return new Object[][]{
                {1, 512000},
                {3, 102400}
        };
    }

    @DataProvider(name = "incorrectAttachments")
    public Object[][] provideIncorrectAttachments() {
        return new Object[][]{
                {1, 6291456},
                {11, 524288}
        };
    }
}
