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
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
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
        Map<String, Object> eks = (Map<String, Object>) edata.get("eks");
        Map<String, Object> etags = (Map<String, Object>) eventMap.get("etags");

        Assert.assertEquals(telemetryEvent, eventMap.get("eid"));
        Assert.assertEquals("2.2", eventMap.get("ver"));
        Assert.assertNotNull(eventMap.get("sid"));
        Assert.assertNotNull(eventMap.get("did"));
        Assert.assertEquals(PARTNER_ID, eks.get("partnerid"));

        List<Object> partnerDataList = ((List<Object>) etags.get("partner"));
        Assert.assertEquals(PARTNER_ID, partnerDataList.get(0));
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

        checkIfTelemetryEventIsLogged("GE_PARTNER_DATA");
    }

    /**
     * Scenario : To save Telemetry details passed to it as {@link Telemetry}
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

        String eventStrng = "{\n" +
                "  \"cdata\": [\n" +
                "    \n" +
                "  ],\n" +
                "  \"did\": \"85f9b35bd361605a41911948359e61b2a22f75c7\",\n" +
                "  \"edata\": {\n" +
                "    \"eks\": {\n" +
                "      \"dspec\": {\n" +
                "        \"camera\": \"16.10.bong\",\n" +
                "        \"cap\": [\n" +
                "          \n" +
                "        ],\n" +
                "        \"cpu\": \"abi: armeabi-v7a processor\\t: 0 \",\n" +
                "        \"dlocname\": \"\",\n" +
                "        \"dname\": \"\",\n" +
                "        \"edisk\": 11.57,\n" +
                "        \"id\": \"caf46b2f69333e63\",\n" +
                "        \"idisk\": 0.0,\n" +
                "        \"make\": \"Samsung SM-G900H\",\n" +
                "        \"mdata\": {\n" +
                "          \"id\": \"diNqKQV9Xdg:APA91bEhupodZ8ohXKIhnieDHhvISQcWLTUBHdOLzZmPgLgkXp52U_YBV-Jc4tSUgPT9iQU_-WOseZ9jDFmtuCCcxwNBiq3va1Rp8iVdj332QSB6BSKtkBCGHz8zVw9Bl_9wlal533gD\",\n" +
                "          \"type\": \"fcm\"\n" +
                "        },\n" +
                "        \"mem\": -1.0,\n" +
                "        \"os\": \"Android 6.0.1\",\n" +
                "        \"scrn\": 5.21,\n" +
                "        \"sims\": -1\n" +
                "      },\n" +
                "      \"loc\": \"\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"eid\": \"GE_GENIE_START\",\n" +
                "  \"ets\": 1496300828767,\n" +
                "  \"gdata\": {\n" +
                "    \"id\": \"genie.android\",\n" +
                "    \"ver\": \"6.2.localqa-debug.24\"\n" +
                "  },\n" +
                "  \"sid\": \"\",\n" +
                "  \"tags\": [\n" +
                "    \n" +
                "  ],\n" +
                "  \"ts\": \"2017-06-01T07:07:08+0000\",\n" +
                "  \"uid\": \"bf228c46-f7fe-49e1-bb91-eb1b254bec7c\",\n" +
                "  \"ver\": \"2.0\"\n" +
                "}";

        GenieResponse genieResponse = activity.saveTelemetry(eventStrng);
        Assert.assertTrue("true", genieResponse.getStatus());

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
