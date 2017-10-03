package org.ekstep.genieservices.partnerservice;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.telemetry.model.EventModel;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Sneha on 5/17/2017.
 */

public class PartnerServiceTest extends GenieServiceTestBase {
    private static final String TAG = PartnerServiceTest.class.getSimpleName();
    private static final String PARTNER_ID = "org.sample";
    private static final String PARTNER_DATA = "PARTNER_DATA";
    private String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGdo5VYOK9cTrQQ+ajOxfHMgg/\n" +
            " * TDX77o/eVTUjcErLLYKBQ6qb8t/jCCuRNexIexRBldk4gC9STyuVWN8x2xkSildf\n" +
            " * Nch3KUTvwgJx1n2y/03tIHkimOxEONCg3rWPdiWx7nLdW4TuHbwZTZmMdhLjM4lI\n" +
            " * OSyoyYpX/JmDnxjq4QIDAQAB";

    @Override
    public void setup() throws IOException {
        super.setup();
        GenieServiceDBHelper.clearPartnerDBEntry();
    }

    /**
     * Scenario : To check the entire partner flow.
     * <p>
     * Step 1. Register a partner with the specified partnerID and publicKey in PartnerData.
     * Step 2. Check if the partnerId is registered.
     * Step 3. Start the Partner Session with the specified partnerID in PartnerData.
     * Step 4. Send Partner data specified in PartnerData
     * Step 5. Terminate the Partner Session with the specified partnerID.
     * Step 6 : Save the telemetry event for the above steps.
     * <p>
     * Then : Check if telemetry event is logged in for every event.
     */
    @Test
    public void _10shouldTestEntirePartnerFlow() {

        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        GenieResponse registerPartnerResponse = activity.registerPartner(partnerData);
        Assert.assertTrue(registerPartnerResponse.getStatus());
        checkIfTelemetryEventIsLogged("GE_REGISTER_PARTNER");

        GenieResponse isRegisteredPartnerResponse = activity.isPartnerRegistered(PARTNER_ID);
        Assert.assertTrue(isRegisteredPartnerResponse.getStatus());

        GenieResponse startPartnerSession = activity.startPartnerSession(partnerData);
        Assert.assertTrue(startPartnerSession.getStatus());
        checkIfTelemetryEventIsLogged("GE_START_PARTNER_SESSION");

        GenieResponse sendDataResponse = activity.sendData(partnerData);
        Assert.assertTrue(sendDataResponse.getStatus());
        checkIfTelemetryEventIsLogged("GE_PARTNER_DATA");

        GenieResponse terminatePartnerSession = activity.terminatePartnerSession(partnerData);
        Assert.assertTrue(terminatePartnerSession.getStatus());
        checkIfTelemetryEventIsLogged("GE_STOP_PARTNER_SESSION");
    }

    private void checkIfTelemetryEventIsLogged(String telemetryEvent) {

        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(telemetryEvent);
        Map eventMap = eventModelList.get(0).getEventMap();
        Assert.assertEquals(telemetryEvent, eventMap.get("eid"));
        Assert.assertEquals("2.1", eventMap.get("ver"));
    }

    /**
     * Scenario: Validate register partner when the partner id is null in the partner data.
     */
    @Test
    public void _11shouldValidateForRegisterPartnerForNullPartnerId() {

        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", null, PARTNER_DATA, PUBLIC_KEY);
        GenieResponse registerPartnerResponse = activity.registerPartner(partnerData);
        Assert.assertFalse(registerPartnerResponse.getStatus());
        Assert.assertEquals("MISSING_PARTNER_ID", registerPartnerResponse.getErrorMessages().get(0));
    }

    /**
     * Scenario: Validate register partner when the public key is null in the partner data.
     */
    @Test
    public void _12shouldValidateForRegisterPartnerForNullPublicKey() {
        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, null);
        GenieResponse registerPartnerResponse = activity.registerPartner(partnerData);
        Assert.assertFalse(registerPartnerResponse.getStatus());
        Assert.assertEquals("MISSING_PUBLIC_KEY", registerPartnerResponse.getErrorMessages().get(0));
    }

    /**
     * Scenario: Validation when registering partner twice with the same partner data.
     */
    @Test
    public void _13shouldValidateForPartnerDataInRegistration() {

        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        GenieResponse registerPartnerResponse = activity.registerPartner(partnerData);
        Assert.assertTrue(registerPartnerResponse.getStatus());

        GenieResponse registerResponse = activity.registerPartner(partnerData);
        Assert.assertTrue(registerResponse.getStatus());
    }

    /**
     * Scenario: Trying to start the partner session without registering the partner.
     */
    @Test
    public void _14shouldCheckStartPartnerSessionForUnregisteredPartner() {

        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        GenieResponse startPartnerSession = activity.startPartnerSession(partnerData);
        Assert.assertFalse(startPartnerSession.getStatus());
        Assert.assertEquals("UNREGISTERED_PARTNER", startPartnerSession.getError());
        Assert.assertEquals("- Session start failed! Partner: org.sample", startPartnerSession.getErrorMessages().get(0));
    }

    /**
     * Scenario: Trying to send partner data without registering the partner.
     */
    @Test
    public void _15shouldCheckSendDataForUnregisteredPartner() {

        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        GenieResponse sendDataResponse = activity.sendData(partnerData);
        Assert.assertFalse(sendDataResponse.getStatus());
        Assert.assertEquals("UNREGISTERED_PARTNER", sendDataResponse.getError());
        Assert.assertEquals("- Sending data failed! Partner: org.sample", sendDataResponse.getErrorMessages().get(0));
    }

    /**
     * Scenario: Validate for start partner session when the partner is not registered.
     */
    @Test
    public void _16shouldCheckStartPartnerSessionForUnregisteredPartner() {
        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        GenieResponse startPartnerSession = activity.startPartnerSession(partnerData);
        Assert.assertFalse(startPartnerSession.getStatus());
        Assert.assertEquals("UNREGISTERED_PARTNER", startPartnerSession.getError());
        Assert.assertEquals("- Session start failed! Partner: org.sample", startPartnerSession.getErrorMessages().get(0));
    }

    /**
     * Scenario: Validate for the terminate partner session when the partner is not registered.
     */
    @Test
    public void _17shouldCheckTerminatePartnerSessionForUnregisteredPartner() {
        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        GenieResponse terminatePartnerSession = activity.terminatePartnerSession(partnerData);
        Assert.assertFalse(terminatePartnerSession.getStatus());
        Assert.assertEquals("UNREGISTERED_PARTNER", terminatePartnerSession.getError());
        Assert.assertEquals("- Session termination failed! Partner: org.sample", terminatePartnerSession.getErrorMessages().get(0));
    }

    /**
     * To check if the partner is registered for an unregistered partner.
     */
    @Test
    public void _18shouldCheckIfPartnerIsRegistered() {
        GenieResponse isRegisteredResponse = activity.isPartnerRegistered("org.test");
        Assert.assertTrue(isRegisteredResponse.getStatus());
        Assert.assertEquals(false, isRegisteredResponse.getResult());
    }

    @Test
    public void _19shouldCheckForPartnerSessionTelemetryEvent() {
        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        GenieResponse registerPartnerResponse = activity.registerPartner(partnerData);
        Assert.assertTrue(registerPartnerResponse.getStatus());
        checkIfTelemetryEventIsLogged("GE_REGISTER_PARTNER");

        GenieResponse startPartnerSession = activity.startPartnerSession(partnerData);
        Assert.assertTrue(startPartnerSession.getStatus());

        PartnerData partnerData2 = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        GenieResponse registerPartner = activity.registerPartner(partnerData2);
        Assert.assertTrue(registerPartner.getStatus());
        checkIfTelemetryEventIsLogged("GE_REGISTER_PARTNER");

        GenieResponse startPartner = activity.startPartnerSession(partnerData2);
        Assert.assertTrue(startPartner.getStatus());

        checkIfTelemetryEventIsLogged("GE_STOP_PARTNER_SESSION");

    }
}
