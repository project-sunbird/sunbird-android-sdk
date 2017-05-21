package org.ekstep.genieservices.partnerservice;

import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.junit.Assert;
import org.junit.Test;

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

    @Test
    public void shouldTestEntirePartnerFlow() {

        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.6", PARTNER_ID, PARTNER_DATA, PUBLIC_KEY);

        //registering partner
        GenieResponse registerPartnerResponse = activity.registerPartner(partnerData);
        Assert.assertTrue(registerPartnerResponse.getStatus());
        checkIfTelemetryEventIsLogged();

        //check if the partner is registered
        GenieResponse isRegisteredPartnerResponse = activity.isPartnerRegistered(PARTNER_ID);
        Assert.assertTrue(isRegisteredPartnerResponse.getStatus());

        //start partner session
        GenieResponse startPartnerSession = activity.startPartnerSession(partnerData);
        Assert.assertTrue(startPartnerSession.getStatus());
//        GEStartPartnerSession geStartPartnerSession = new GEStartPartnerSession(gameData, PARTNER_ID, "20013fea6bcc820c", "PARTNER_SID");
        checkIfTelemetryEventIsLogged();

        //send partner data
        GenieResponse sendDataResponse = activity.sendData(partnerData);
        Assert.assertTrue(sendDataResponse.getStatus());
        checkIfTelemetryEventIsLogged();

        //terminate partner session
        GenieResponse terminatePartnerSession = activity.terminatePartnerSession(partnerData);
        Assert.assertTrue(terminatePartnerSession.getStatus());
        checkIfTelemetryEventIsLogged();
    }

    /**
     * Check for the telemetry data.
     */
    private void checkIfTelemetryEventIsLogged() {

        //TODO : Check if telemetry events are logged.
    }

    @Test
    public void shouldTestEntirePartnerFlowForMultiplePartner() {

        PartnerData partner1Data = new PartnerData("org.ekstep.partner", "1.7", "PARTNER1_ID", "partner_1 data", PUBLIC_KEY);

        GenieResponse registerPartner1Response = activity.registerPartner(partner1Data);
        Assert.assertTrue(registerPartner1Response.getStatus());
        checkIfTelemetryEventIsLogged();

        GenieResponse startPartner1Session = activity.startPartnerSession(partner1Data);
        Assert.assertTrue(startPartner1Session.getStatus());
        checkIfTelemetryEventIsLogged();

        PartnerData partner2Data = new PartnerData("org.ekstep.partner", "1.8", "PARTNER2_ID", "partner_2 data", PUBLIC_KEY);

        GenieResponse registerPartner2Response = activity.registerPartner(partner2Data);
        Assert.assertTrue(registerPartner2Response.getStatus());
        checkIfTelemetryEventIsLogged();

        GenieResponse startPartner2Session = activity.startPartnerSession(partner2Data);
        Assert.assertTrue(startPartner2Session.getStatus());
        checkIfTelemetryEventIsLogged();

        GenieResponse sendPartner1Data = activity.sendData(partner1Data);
        Assert.assertTrue(sendPartner1Data.getStatus());
        checkIfTelemetryEventIsLogged();

        GenieResponse sendPartner2Data = activity.sendData(partner2Data);
        Assert.assertTrue(sendPartner2Data.getStatus());
        checkIfTelemetryEventIsLogged();

        GenieResponse terminatePartner1Data = activity.terminatePartnerSession(partner1Data);
        Assert.assertTrue(terminatePartner1Data.getStatus());
        checkIfTelemetryEventIsLogged();

        GenieResponse terminatePartner2Data = activity.terminatePartnerSession(partner2Data);
        Assert.assertTrue(terminatePartner2Data.getStatus());
        checkIfTelemetryEventIsLogged();
    }
}
