package org.ekstep.genieservices.telemetryservice;

import android.util.Log;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.telemetry.GECreateProfile;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
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

    /**
     * To check for the TelemetryEvent data.
     *
     * @param geCreateProfile
     */
    private void checkEventData(GECreateProfile geCreateProfile) {

        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(geCreateProfile.getEid());
        Map eventMap = eventModelList.get(0).getEventMap();

        Assert.assertEquals(geCreateProfile.getEid(), eventMap.get("eid"));
        Assert.assertEquals(geCreateProfile.getUid(), eventMap.get("uid"));
        Assert.assertNotNull(eventMap.get("sid"));
        Assert.assertNotNull(eventMap.get("did"));
    }

    /**
     * Scenario : To save Telemetry details passed to it as {@link Telemetry}
     */
    @Test
    public void shouldSaveTelemetryData() {

        Profile createdProfile = createUserProfile();

        GameData gameData = new GameData(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);

        GECreateProfile geCreateProfile = new GECreateProfile(gameData, createdProfile, null);

        GenieServiceDBHelper.findProfile();
        Assert.assertEquals(createdProfile.getUid(), geCreateProfile.getUid());

        activity.saveTelemetry(geCreateProfile);

        checkEventData(geCreateProfile);

    }

    /**
     * Scenario : To save Telemetry details passed to it as String.
     */
    @Test
    public void _2shouldSaveTelemetryData() {

        Profile createdProfile = createUserProfile();

        GameData gameData = new GameData(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);

        GECreateProfile geCreateProfile = new GECreateProfile(gameData, createdProfile, null);

        GenieServiceDBHelper.findProfile();
        Assert.assertEquals(createdProfile.getUid(), geCreateProfile.getUid());

        activity.saveTelemetry(geCreateProfile.toString());

        checkEventData(geCreateProfile);
    }

    /**
     * Scenario : To check if the partner telemetry events are saved.
     */
    @Test
    public void _3shouldCreatePartnerSession() {

        Profile profile = createUserProfile();

        GameData gameData = new GameData(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);

        GECreateProfile geCreateProfile = new GECreateProfile(gameData, profile, null);

        GenieServiceDBHelper.findProfile();
        Assert.assertEquals(profile.getUid(), geCreateProfile.getUid());

        activity.saveTelemetry(geCreateProfile);

        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(geCreateProfile.getEid());
        Map eventMap = eventModelList.get(0).getEventMap();

        Log.v(TAG, "eventMap :: " + eventMap);

        Log.v(TAG, "tags :: " + eventMap.get("tags").toString());

    }

    /**
     * This method,
     * 1. Clears profile table and telemetry table.
     * 2. Creates new user profile.
     * 3. Sets it as current user profile.
     */
    private Profile createUserProfile() {

        Profile profile = new Profile("Happy5 " + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");

        GenieServiceDBHelper.clearProfileTable();
        GenieServiceDBHelper.clearTelemetryTableEntry();

        Profile createdProfile = createNewProfile(profile);

        activity.setCurrentUser(createdProfile.getUid());

        return createdProfile;
    }
}
