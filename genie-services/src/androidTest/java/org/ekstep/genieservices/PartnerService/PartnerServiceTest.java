package org.ekstep.genieservices.PartnerService;

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
    public void _1shouldTestEntirePartnerFlow() {

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
}
