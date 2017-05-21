package org.ekstep.genieservices.userprofile;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.commons.bean.Profile;
import org.junit.Assert;

import java.util.List;

/**
 * Created by swayangjit on 21/5/17.
 */

public class AssertProfile {

    public static void verifyProfile(Profile inputProfile, Profile resultProfile) {

        Assert.assertEquals("Created profile uid and Input profile uid should be same", resultProfile.getUid(), inputProfile.getUid());
        Assert.assertEquals("Created profile handle and Input profile handle should be same", resultProfile.getHandle(), inputProfile.getHandle());
        Assert.assertEquals("Created profile avatar and Input profile avatar should be same", resultProfile.getAvatar(), inputProfile.getAvatar());
        Assert.assertEquals("Created profile month and Input profile month should be same", resultProfile.getMonth(), inputProfile.getMonth());
        Assert.assertEquals("Created profile day and Input profile month should be same", resultProfile.getDay(), inputProfile.getDay());
        Assert.assertEquals("Created profile isgroupUser and Input profile isgroupUser should be same", resultProfile.isGroupUser(), inputProfile.isGroupUser());
        Assert.assertEquals("Created profile board and Input profile board should be same", resultProfile.getBoard(), inputProfile.getBoard());
        Assert.assertEquals("Created profile medium and Input profile medium should be same", resultProfile.getMedium(), inputProfile.getMedium());
        Assert.assertEquals("Created profile age and Input profile age should be same", resultProfile.getAge(), inputProfile.getAge());
        Assert.assertEquals("Created profile language and Input profile language should be same", resultProfile.getLanguage(), inputProfile.getLanguage());
        Assert.assertEquals("Created profile gender and Input profile gender should be same", resultProfile.getGender(), inputProfile.getGender());
        Assert.assertEquals("Created profile standard and Input profile standard should be same", resultProfile.getStandard(), inputProfile.getStandard());

    }
    public static void verifyAnonymousUser(Profile profile) {

        Assert.assertNotNull("Anonymous user uid shouldn't be null",profile.getHandle());
        Assert.assertEquals("Anonymous user should have blank handle","", profile.getHandle());
        Assert.assertEquals("Anonymous user should have blank avatar","", profile.getAvatar());
        Assert.assertEquals("Anonymous user should have null gender",null, profile.getGender());
        Assert.assertEquals("Anonymous user should have default age(-1)",-1, profile.getAge());
        Assert.assertEquals("Anonymous user should have default day(-1)",-1, profile.getDay());
        Assert.assertEquals("Anonymous user should have default month(-1)",-1, profile.getMonth());
        Assert.assertEquals("Anonymous user should have default standard(-1)",-1, profile.getStandard());
        Assert.assertEquals("Anonymous user should have blank board(-1)","", profile.getBoard());
        Assert.assertEquals("Anonymous user should have blank medium","", profile.getMedium());
        Assert.assertEquals("Anonymous user should have blank language","", profile.getLanguage());
        Assert.assertEquals("Anonymous user should not be group user",false, profile.isGroupUser());

    }

    public static void verifyProfileisDeleted() {
        List<Profile> profile = GenieServiceDBHelper.findProfile();
        Assert.assertNull(profile);

    }
}
