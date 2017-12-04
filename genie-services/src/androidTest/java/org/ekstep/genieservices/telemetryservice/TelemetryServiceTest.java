package org.ekstep.genieservices.telemetryservice;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.SampleResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.bean.SyncStat;
import org.ekstep.genieservices.commons.bean.TelemetryExportRequest;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.bean.TelemetryImportRequest;
import org.ekstep.genieservices.commons.bean.TelemetryStat;
import org.ekstep.genieservices.commons.db.contract.TelemetryProcessedEntry;
import org.ekstep.genieservices.telemetry.model.EventModel;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Sneha on 5/16/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TelemetryServiceTest extends GenieServiceTestBase {
    private static final String TAG = TelemetryServiceTest.class.getSimpleName();
    private static final String PARTNER_ID = "org.sample";
    private static final String PARTNER_DATA = "PARTNER_DATA";
    private String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGdo5VYOK9cTrQQ+ajOxfHMgg/\n" +
            " * TDX77o/eVTUjcErLLYKBQ6qb8t/jCCuRNexIexRBldk4gC9STyuVWN8x2xkSildf\n" +
            " * Nch3KUTvwgJx1n2y/03tIHkimOxEONCg3rWPdiWx7nLdW4TuHbwZTZmMdhLjM4lI\n" +
            " * OSyoyYpX/JmDnxjq4QIDAQAB";

    @Override
    public void setup() throws IOException {
        super.setup();
        GenieServiceDBHelper.clearTelemetryTableEntry();
        GenieServiceDBHelper.clearPartnerDBEntry();
    }

    @Test
    public void _1shouldExportTelemetryToGSAFileSuccessfully() {
        //This is event is generated to ensure all GE_SESSION_START and all events will be generated first
        activity.saveTelemetry(SampleResponse.getSampleEvent());

        GenieServiceDBHelper.clearTelemetryTableEntry();
        for (int i = 0; i <= 4; i++) {
            activity.saveTelemetry(SampleResponse.getSampleEvent());
        }

        Assert.assertEquals(5, GenieServiceDBHelper.findAllEvents().size());

        TelemetryExportRequest request = new TelemetryExportRequest.Builder().toFolder(activity.getExternalFilesDir(null).toString()).build();
        GenieResponse<TelemetryExportResponse> response = activity.exportTelemetry(request);
        Assert.assertTrue(response.getStatus());
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(activity.getExternalFilesDir(null).toString() + File.separator + "tmp" + File.separator + "transfer.gsa", response.getResult().getExportedFilePath());
    }

    @Test
    public void _2shouldImportGSAFileSuccessfully() {
        for (int i = 0; i <= 4; i++) {
            activity.saveTelemetry(SampleResponse.getSampleEvent());
        }

        TelemetryExportRequest request = new TelemetryExportRequest.Builder().toFolder(activity.getExternalFilesDir(null).toString()).build();
        activity.exportTelemetry(request);
        GenieServiceDBHelper.clearTelemetryTableEntry();
        GenieServiceDBHelper.clearTable(TelemetryProcessedEntry.TABLE_NAME);
        TelemetryImportRequest importRequest = new TelemetryImportRequest.Builder().fromFilePath(activity.getExternalFilesDir(null).toString() + File.separator + "tmp" + File.separator + "transfer.gsa").build();
        activity.importTelemetry(importRequest);
        Assert.assertEquals(1, GenieServiceDBHelper.findProcessedEvents().size());
    }


    /**
     * To check for the TelemetryEvent data.
     */
    private void checkIfTelemetryEventIsLogged(String telemetryEvent) {

        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(telemetryEvent);
        Map eventMap = eventModelList.get(0).getEventMap();

        Map<String, Object> edata = (Map<String, Object>) eventMap.get("edata");
        Map<String, Object> context = (Map<String, Object>) eventMap.get("context");

        Assert.assertEquals(telemetryEvent, eventMap.get("eid"));
        Assert.assertEquals("3.0", eventMap.get("ver"));
        Assert.assertNotNull(context.get("sid"));
        Assert.assertNotNull(context.get("did"));

        List<Map<String, String>> partnerDataList = ((List<Map<String, String>>) context.get("cdata"));
        if (partnerDataList.size() == 2) {
            Assert.assertEquals(PARTNER_ID, partnerDataList.get(1).get("id"));
        } else {
            Assert.assertEquals(PARTNER_ID, partnerDataList.get(0).get("id"));
        }

    }

    private void savePartnerData(PartnerData partnerData) {

        GenieServiceDBHelper.clearPartnerDBEntry();

        GenieResponse registerPartnerResponse = activity.registerPartner(partnerData);
        Assert.assertTrue(registerPartnerResponse.getStatus());

        GenieResponse isRegisteredPartnerResponse = activity.isPartnerRegistered(PARTNER_ID);
        Assert.assertTrue(isRegisteredPartnerResponse.getStatus());

        GenieResponse startPartnerSession = activity.startPartnerSession(partnerData);
        Assert.assertTrue(startPartnerSession.getStatus());

        GenieResponse sendDataResponse = activity.sendData(partnerData);
        Assert.assertTrue(sendDataResponse.getStatus());

        checkIfTelemetryEventIsLogged("EXDATA");
    }

    /**
     * Scenario : To save Telemetry details passed to it as {@link org.ekstep.genieservices.commons.bean.telemetry.Telemetry}
     * Given : Save telemetry details.
     * Step 1. Create a user profile and set as current user.
     * Step 2. Create a GameData Object.
     * Step 3. Create a Telemetry event ie GECreateProfile by passing GameData and created Profile object to it.
     * Step 4. Save the telemetry event.
     * When :
     * Then : Check if the telemetry event is saved.
     */
    @Test
    public void _3shouldSaveTelemetryData() {

        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        savePartnerData(partnerData);

    }

    /**
     * Scenario : To save Telemetry details passed to it as String.
     * Given : Save telemetry event as String.
     * Step 1. Create a user profile and set as current user.
     * Step 2. Create a GameData Object.
     * Step 3. Create a Telemetry event ie GECreateProfile by passing GameData and Profile object to it.
     * Step 4. Save the telemetry event as String.
     * <p>
     * Then :Check if the telemetry event is saved.
     */
    @Test
    public void _4shouldSaveTelemetryDataAsString() {

        GenieServiceDBHelper.clearTelemetryTableEntry();


        GenieResponse genieResponse = activity.saveTelemetry(SampleResponse.getSampleStartEvent());
        Assert.assertTrue("true", genieResponse.getStatus());
        checkIfTelemetryEventIsLogged("START");

    }

    @Test
    public void _5shoulCheckUnsyncedTelemetryData() {

        GenieServiceDBHelper.clearTelemetryTableEntry();

        PartnerData partnerData = new PartnerData("org.ekstep.partner2", "1.8", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        savePartnerData(partnerData);

        GenieResponse<TelemetryStat> telemetryStatGenieResponse = activity.getTelemetryStat();

        Assert.assertTrue("true", telemetryStatGenieResponse.getStatus());
        Assert.assertNotNull(telemetryStatGenieResponse.getResult().getUnSyncedEventCount());
        Assert.assertNotEquals(0, telemetryStatGenieResponse.getResult().getUnSyncedEventCount());

        shouldCheckSyncedTelemetryData(telemetryStatGenieResponse);
    }

    private void shouldCheckSyncedTelemetryData(GenieResponse<TelemetryStat> telemetryStatGenieResponse) {

        startMockServer();
        mMockServer.mockHttpResponse(SampleResponse.telemetrySyncResponse(), 200);
        mMockServer.mockHttpResponse(SampleResponse.telemetrySyncResponse(), 200);
        mMockServer.mockHttpResponse(SampleResponse.telemetrySyncResponse(), 200);
        GenieResponse<SyncStat> syncStatGenieResponse = activity.sync();

        Assert.assertTrue(syncStatGenieResponse.getStatus());
        Assert.assertNotNull(syncStatGenieResponse.getResult().getSyncedEventCount());
        Assert.assertNotEquals(0, syncStatGenieResponse.getResult().getSyncedEventCount());
        Assert.assertNotEquals(0, syncStatGenieResponse.getResult().getSyncTime());

        //TODO : check getUnSyncedEventCount and getSyncedEventCount are different.
        Assert.assertEquals(telemetryStatGenieResponse.getResult().getUnSyncedEventCount() + 1,
                syncStatGenieResponse.getResult().getSyncedEventCount());
        shutDownMockServer();
    }


}
