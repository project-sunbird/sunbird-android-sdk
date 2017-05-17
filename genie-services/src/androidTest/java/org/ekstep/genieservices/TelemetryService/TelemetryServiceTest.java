package org.ekstep.genieservices.TelemetryService;

import android.util.Log;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
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

    @Test
    public void _1shouldSaveTelemetryData() {

        GenieServiceDBHelper.clearProfileTableEntry();
        GenieServiceDBHelper.clearTelemetryTableEntry();

        final Profile profile = new Profile("Happy1 " + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setAge(4);
        profile.setDay(12);
        profile.setMonth(11);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);
        Profile createdProfile = genieResponse.getResult();

        activity.setCurrentUser(createdProfile.getUid());

        GameData gameData = new GameData(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);

        GECreateProfile geCreateProfile = new GECreateProfile(gameData, profile, null);

        GenieServiceDBHelper.findProfile();
        Assert.assertEquals(createdProfile.getUid(), geCreateProfile.getUid());

        activity.saveTelemetry(geCreateProfile);

        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(geCreateProfile.getEID());
        Map eventMap = eventModelList.get(0).getEventMap();

        Assert.assertEquals(geCreateProfile.getEID(), eventMap.get("eid"));
        Assert.assertEquals(geCreateProfile.getUid(), eventMap.get("uid"));
    }

    @Test
    public void _2shouldSaveTelemetryData() {

        GenieServiceDBHelper.clearProfileTableEntry();
        GenieServiceDBHelper.clearTelemetryTableEntry();

        final Profile profile = new Profile("Happy2 " + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setAge(4);
        profile.setDay(12);
        profile.setMonth(11);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);
        Profile createdProfile = genieResponse.getResult();

        GameData gameData = new GameData(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);

        GECreateProfile geCreateProfile = new GECreateProfile(gameData, profile, null);

        GenieServiceDBHelper.findProfile();
        Assert.assertEquals(createdProfile.getUid(), geCreateProfile.getUid());

        activity.saveTelemetry(geCreateProfile.toString());

        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(geCreateProfile.getEID());
        Map eventMap = eventModelList.get(0).getEventMap();

        Assert.assertEquals(geCreateProfile.getEID(), eventMap.get("eid"));
        Assert.assertEquals(geCreateProfile.getUid(), eventMap.get("uid"));
    }
}
