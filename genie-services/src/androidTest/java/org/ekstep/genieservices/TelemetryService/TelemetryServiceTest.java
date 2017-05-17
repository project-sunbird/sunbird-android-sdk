package org.ekstep.genieservices.TelemetryService;

import android.util.Log;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.telemetry.GECreateProfile;
import org.ekstep.genieservices.telemetry.model.EventModel;
import org.ekstep.genieservices.test.BuildConfig;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Sneha on 5/16/2017.
 */

public class TelemetryServiceTest extends GenieServiceTestBase {
    private static final String TAG = TelemetryServiceTest.class.getSimpleName();
    private String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGdo5VYOK9cTrQQ+ajOxfHMgg/\n" +
            " * TDX77o/eVTUjcErLLYKBQ6qb8t/jCCuRNexIexRBldk4gC9STyuVWN8x2xkSildf\n" +
            " * Nch3KUTvwgJx1n2y/03tIHkimOxEONCg3rWPdiWx7nLdW4TuHbwZTZmMdhLjM4lI\n" +
            " * OSyoyYpX/JmDnxjq4QIDAQAB";


    /**
     * To create profile.
     * @return
     */
    private Profile createUserProfile() {

        final Profile profile = new Profile("Happy2 " + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setAge(4);
        profile.setDay(12);
        profile.setMonth(11);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);
        Profile createdProfile = genieResponse.getResult();

        return createdProfile;
    }

    /**
     * To check for the event data.
     * @param geCreateProfile
     */
    private void checkEventData(GECreateProfile geCreateProfile) {

        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(geCreateProfile.getEID());
        Map eventMap = eventModelList.get(0).getEventMap();

        Assert.assertEquals(geCreateProfile.getEID(), eventMap.get("eid"));
        Assert.assertEquals(geCreateProfile.getUid(), eventMap.get("uid"));
        Assert.assertNotNull(eventMap.get("sid"));
        Assert.assertNotNull(eventMap.get("did"));
    }

    @Test
    public void _1shouldSaveTelemetryData() {

        GenieServiceDBHelper.clearProfileTableEntry();
        GenieServiceDBHelper.clearTelemetryTableEntry();

        Profile createdProfile = createUserProfile();

        activity.setCurrentUser(createdProfile.getUid());

        GameData gameData = new GameData(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);

        GECreateProfile geCreateProfile = new GECreateProfile(gameData, createdProfile, null);

        GenieServiceDBHelper.findProfile();
        Assert.assertEquals(createdProfile.getUid(), geCreateProfile.getUid());

        activity.saveTelemetry(geCreateProfile);

        checkEventData(geCreateProfile);

    }

    @Test
    public void _2shouldSaveTelemetryData() {

        GenieServiceDBHelper.clearProfileTableEntry();
        GenieServiceDBHelper.clearTelemetryTableEntry();

        Profile profile = createUserProfile();

        activity.setCurrentUser(profile.getUid());

        GameData gameData = new GameData(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);

        GECreateProfile geCreateProfile = new GECreateProfile(gameData, profile, null);

        GenieServiceDBHelper.findProfile();
        Assert.assertEquals(profile.getUid(), geCreateProfile.getUid());

        activity.saveTelemetry(geCreateProfile.toString());

        checkEventData(geCreateProfile);
    }

    @Test
    public void _3shouldCreatePartnerSession() {

        GenieServiceDBHelper.clearProfileTableEntry();
        GenieServiceDBHelper.clearTelemetryTableEntry();

        PartnerData partnerData = new PartnerData("org.ekstep.partner", "1.5", "1243", "PARTNER_DATA", PUBLIC_KEY);

        GenieResponse genieResponse = activity.registerPartner(partnerData);
        Assert.assertEquals(true, genieResponse.getStatus());

        Profile profile = createUserProfile();

        GameData gameData = new GameData(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);

        GECreateProfile geCreateProfile = new GECreateProfile(gameData, profile, null);

        GenieServiceDBHelper.findProfile();
        Assert.assertEquals(profile.getUid(), geCreateProfile.getUid());

        activity.saveTelemetry(geCreateProfile);

        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(geCreateProfile.getEID());
        Map eventMap = eventModelList.get(0).getEventMap();

        Log.v(TAG, "eventMap :: " + eventMap);

        Log.v(TAG, "tags :: " + eventMap.get("tags").toString());

    }
}
