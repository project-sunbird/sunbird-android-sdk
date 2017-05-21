package org.ekstep.genieservices.userprofile;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Sneha on 5/12/2017.
 */

public class UserProfileTest extends GenieServiceTestBase {

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        GenieServiceDBHelper.clearProfileTable();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    /**
     * Test to create new user profile.
     */
    @Test
    public void shouldCreateUserProfile() {

        Profile profile = new Profile("Happy1 " + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setAge(4);
        profile.setDay(12);
        profile.setMonth(11);

        Profile createdProfile = createNewProfile(profile);

        Profile profileinDb = GenieServiceDBHelper.findProfile().get(0);

        AssertProfile.verifyProfile(createdProfile, profileinDb);

    }

    /**
     * Test to update new profile.
     */
    @Test
    public void shouldUpdateUserProfile() {
        Profile profile = new Profile("Happy2", "@drawable/ic_avatar2", "en");
        Profile createdProfile = createNewProfile(profile);

        Profile profileForUpdation = new Profile("Happy22", "@drawable/ic_avatar2", "en");
        profileForUpdation.setUid(createdProfile.getUid());
        profileForUpdation.setDay(23);
        profileForUpdation.setMonth(12);

        GenieResponse<Profile> response = activity.updateUserProfile(profileForUpdation);
        Profile updatedProfile = response.getResult();

        AssertProfile.verifyProfile(profileForUpdation, updatedProfile);

        Profile profileinDb = GenieServiceDBHelper.findProfile().get(0);

        AssertProfile.verifyProfile(updatedProfile, profileinDb);
    }

    /**
     * Test to set current user.
     */
    @Test
    public void shouldSetCurrentUser() {
        Profile profile = new Profile("Happy5 " + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        Profile newUserProfile = createNewProfile(profile);

        Profile profileinDb = GenieServiceDBHelper.findProfile().get(0);

        AssertProfile.verifyProfile(newUserProfile, profileinDb);

        GenieResponse genieResponse = activity.setCurrentUser(newUserProfile.getUid());
        Assert.assertNotNull(genieResponse);
        Assert.assertTrue(genieResponse.getStatus());

        GenieResponse<Profile> response = activity.getCurrentUser();
        Profile currentProfile = response.getResult();
        AssertProfile.verifyProfile(newUserProfile, currentProfile);

    }

    /**
     * Test to delete user profile.
     */
    @Test
    public void shouldDeleteUserProfile() {

        Profile profile = new Profile("Happy6", "@drawable/ic_avatar2", "en");
        Profile createdProfile = createNewProfile(profile);

        GenieResponse response = activity.deleteUserProfile(createdProfile.getUid());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus());

        AssertProfile.verifyProfileisDeleted();
    }

    /**
     * Test to delete user profile if the user is the current user.
     */
    @Test
    public void shouldDeleteUserProfileIfUserIsCurrentUser() {
        Profile profile = new Profile("Happy11", "@drawable/ic_avatar2", "en");

        Profile createdProfile = createNewProfile(profile);

        Profile profileinDb = GenieServiceDBHelper.findProfile().get(0);

        AssertProfile.verifyProfile(createdProfile, profileinDb);

        GenieResponse response = activity.setCurrentUser(profile.getUid());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus());

        GenieResponse genieResponse = activity.deleteUserProfile(profile.getUid());
        Assert.assertNotNull(genieResponse);
        Assert.assertTrue(response.getStatus());

        AssertProfile.verifyProfileisDeleted();

        GenieResponse<Profile> response1 = activity.getCurrentUser();
        Profile anonymousProfile = response1.getResult();
        Assert.assertNotNull(anonymousProfile);
        Assert.assertTrue(response1.getStatus());

        Assert.assertEquals(-1, anonymousProfile.getAge());
    }

    /**
     * Test to create group user
     */
    @Test
    public void shouldCreateGroupUser() {
        Profile profile = new Profile("Group7", "@drawable/ic_avatar2", "en");
        profile.setGroupUser(true);

        Profile createdProfile = createNewProfile(profile);

        Profile profileinDb = GenieServiceDBHelper.findProfile().get(0);

        AssertProfile.verifyProfile(createdProfile, profileinDb);

    }

    /**
     * Test to update group user.
     */
    @Test
    public void shouldUpdateGroupUser() {

        Profile profile = new Profile("Group8", "@drawable/ic_avatar2", "en");
        profile.setGroupUser(true);

        final Profile createdProfile = createNewProfile(profile);
        profile.setUid(createdProfile.getUid());
        profile.setHandle("Group88");

        GenieResponse<Profile> response = activity.updateUserProfile(profile);
        Profile updatedProfile = response.getResult();


        AssertProfile.verifyProfile(profile, updatedProfile);

        Profile profileinDb = GenieServiceDBHelper.findProfile().get(0);

        AssertProfile.verifyProfile(updatedProfile, profileinDb);
    }

    /**
     * Test to validate set current user for the group profile.
     */
    @Test
    public void shouldValidateSetCurrentUserForGroupUser() {
        Profile profile = new Profile("Group9", "@drawable/ic_avatar2", "en");
        profile.setGroupUser(true);

        Profile createdProfile = createNewProfile(profile);

        GenieResponse genieResponse = activity.setCurrentUser(createdProfile.getUid());
        Assert.assertNotNull(genieResponse);
        Assert.assertTrue(genieResponse.getStatus());

        GenieResponse<Profile> genieResponse2 = activity.getCurrentUser();

        AssertProfile.verifyProfile(createdProfile, genieResponse2.getResult());

    }
//

    /**
     * Test to delete group user.
     */
    @Test
    public void shouldDeleteGroupUser() {
        Profile profile = new Profile("Group10 " + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setGroupUser(true);

        createNewProfile(profile);

        GenieResponse genieResponse = activity.deleteUserProfile(profile.getUid());
        Assert.assertNotNull(genieResponse);
        Assert.assertTrue(genieResponse.getStatus());
        AssertProfile.verifyProfileisDeleted();

    }

    /**
     * Check for leap year.
     */
    @Test
    public void shouldValidateLeapYear() {
        final Profile profile = new Profile("Happy12", "@drawable/ic_avatar2", "en");
        profile.setAge(7);
        profile.setDay(29);
        profile.setMonth(2);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals("[invalid date, field: 29/2/2010]", genieResponse.getErrorMessages().get(0));
    }

    /**
     * Test when month is not set
     */
    @Test
    public void shouldValidateDayAndMonthIfOnlyDayIsSet() {
        final Profile profile = new Profile("Happy13" + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setDay(12);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals("[invalid date, field: 12/-1/2017]", genieResponse.getErrorMessages().get(0));
    }

    /**
     * Test when day is not set
     */
    @Test
    public void shouldValidateDayAndMonthIfOnlyMonthIsSet() {
        final Profile profile = new Profile("Happy14" + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setMonth(12);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        AssertProfile.verifyProfileisDeleted();
        Assert.assertEquals("[invalid date, field: -1/12/2017]", genieResponse.getErrorMessages().get(0));
    }

    /**
     * Test for month validation.
     */
    @Test
    public void shouldValidateDayAndMonth() {
        final Profile profile = new Profile("Happy15" + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setAge(8);
        profile.setDay(2);
        profile.setMonth(14);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        AssertProfile.verifyProfileisDeleted();
        Assert.assertEquals("[invalid date, field: 2/14/2009]", genieResponse.getErrorMessages().get(0));

        //TODO verify GE_ERROR event
    }

    /**
     * Check for empty handler
     */
    @Test
    public void shouldBeAbleToLogGeErrorEventForEmptyHandler() {
        final Profile profile = new Profile("", "@drawable/ic_avatar2", "en");
        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals("[handle can not be empty]", genieResponse.getErrorMessages().get(0));

        //TODO verify GE_ERROR event
    }

    /**
     * Check for empty avatar
     */
    @Test
    public void shouldBeAbleToLogGeErrorEventForEmptyAvatar() {
        final Profile profile = new Profile("Happy15", "", "en");
        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals("[avatar can not be empty]", genieResponse.getErrorMessages().get(0));

        //TODO verify GE_ERROR event
    }

    /**
     * Test to set anonymous user.
     */
    @Test
    public void shouldSetAnonymousUser() {

        GenieResponse<String> genieResponse = activity.setAnonymousUser();
        String result = genieResponse.getResult();
        Assert.assertNotNull(genieResponse);
        Assert.assertNotNull(result);
        Assert.assertTrue(genieResponse.getStatus());
    }

    /**
     * Test to get anonymous user.
     */
    @Test
    public void shouldGetAnonymousUser() {

        GenieResponse<String> genieResponse = activity.setAnonymousUser();
        String result = genieResponse.getResult();
        Assert.assertNotNull(result);
        Assert.assertTrue(genieResponse.getStatus());

        GenieResponse<Profile> response = activity.getAnonymousUser();
        Profile anonymousProfile = response.getResult();
        Assert.assertNotNull(anonymousProfile);
        AssertProfile.verifyAnonymousUser(anonymousProfile);
    }
}
