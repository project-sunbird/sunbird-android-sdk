package org.ekstep.genieservices.partnerservice;

import android.util.Log;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.telemetry.model.EventModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by Sneha on 5/17/2017.
 */

public class PartnerServiceTest extends GenieServiceTestBase {
    private static final String TAG = PartnerServiceTest.class.getSimpleName();
    private static final String PARTNER_ID = "PARTNER_ID";
    private static final String PARTNER_DATA = "PARTNER_DATA";
    private String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGdo5VYOK9cTrQQ+ajOxfHMgg/\n" +
            " * TDX77o/eVTUjcErLLYKBQ6qb8t/jCCuRNexIexRBldk4gC9STyuVWN8x2xkSildf\n" +
            " * Nch3KUTvwgJx1n2y/03tIHkimOxEONCg3rWPdiWx7nLdW4TuHbwZTZmMdhLjM4lI\n" +
            " * OSyoyYpX/JmDnxjq4QIDAQAB";

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
    public void shouldTestEntirePartnerFlow() {

        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        GenieResponse registerPartnerResponse = activity.registerPartner(partnerData);
        Assert.assertTrue(registerPartnerResponse.getStatus());
        Log.v(TAG, "response :: " + registerPartnerResponse.getStatus());
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

    /**
     * Scenario : To the entire partner flow for multiple partners.
     * <p>
     * Step 1. Register multiple partner with the specified partnerID and publicKey in PartnerData.
     * Step 2. Start the multiple partners Session with their partnerID in PartnerData.
     * Step 3. Send Partner data specified in PartnerData
     * Step 4. Terminate the multiple partners session with the specified partnerID.
     * Step 5. Save the telemtry event for the above steps
     * <p>
     * Then :  Check if telemetry event is logged in for every partner.
     */
    @Test
    public void shouldTestEntirePartnerFlowForMultiplePartner() {

        PartnerData partner1Data = new PartnerData("org.ekstep.partner", "1.7", "PARTNER1_ID", "partner_1 data", PUBLIC_KEY);

        GenieResponse registerPartner1Response = activity.registerPartner(partner1Data);
        Assert.assertTrue(registerPartner1Response.getStatus());
        checkIfTelemetryEventIsLogged("GE_REGISTER_PARTNER");

        GenieResponse startPartner1Session = activity.startPartnerSession(partner1Data);
        Assert.assertTrue(startPartner1Session.getStatus());
        checkIfTelemetryEventIsLogged("GE_START_PARTNER_SESSION");

        PartnerData partner2Data = new PartnerData("org.ekstep.partner", "1.8", "PARTNER2_ID", "partner_2 data", PUBLIC_KEY);

        GenieResponse registerPartner2Response = activity.registerPartner(partner2Data);
        Assert.assertTrue(registerPartner2Response.getStatus());
        checkIfTelemetryEventIsLogged("GE_REGISTER_PARTNER");

        GenieResponse startPartner2Session = activity.startPartnerSession(partner2Data);
        Assert.assertTrue(startPartner2Session.getStatus());
        checkIfTelemetryEventIsLogged("GE_START_PARTNER_SESSION");

        GenieResponse sendPartner1Data = activity.sendData(partner1Data);
        Assert.assertTrue(sendPartner1Data.getStatus());
        checkIfTelemetryEventIsLogged("GE_PARTNER_DATA");

        GenieResponse sendPartner2Data = activity.sendData(partner2Data);
        Assert.assertTrue(sendPartner2Data.getStatus());
        checkIfTelemetryEventIsLogged("GE_PARTNER_DATA");

        GenieResponse terminatePartner1Data = activity.terminatePartnerSession(partner1Data);
        Assert.assertTrue(terminatePartner1Data.getStatus());
        checkIfTelemetryEventIsLogged("GE_STOP_PARTNER_SESSION");

        GenieResponse terminatePartner2Data = activity.terminatePartnerSession(partner2Data);
        Assert.assertTrue(terminatePartner2Data.getStatus());
        checkIfTelemetryEventIsLogged("GE_STOP_PARTNER_SESSION");
    }


    private void checkIfTelemetryEventIsLogged(String telemetryEvent) {

        //TODO : Check if telemetry events are logged.

        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(telemetryEvent);
        Map eventMap = eventModelList.get(0).getEventMap();

        Log.v(TAG, "eventMap getEID() :::: " + eventMap.get("eid"));
    }
}
