package org.ekstep.genieservices.UserProfile;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sneha on 5/12/2017.
 */

public class UserProfileTest extends GenieServiceTestBase {

    private static final String TAG = UserProfileTest.class.getSimpleName();

    /**
     * Check if the profile is deleted.
     *
     * @return
     */
    public static void userProfileDoesNotExist() {
        List<Profile> profile = GenieServiceDBHelper.findProfile();
        Assert.assertNull(profile);
    }

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        GenieServiceDBHelper.clearProfileTableEntry();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    /**
     * Method to create a new user profile and return it.
     *
     * @return
     */
    public Profile createNewUserProfile(String handle, boolean isGroupUser) {

        Profile profile = new Profile(handle, "@drawable/ic_avatar2", "en");
        profile.setAge(7);
        profile.setDay(12);
        profile.setMonth(11);
        profile.setGroupUser(isGroupUser);

        GenieResponse<Profile> response = activity.createUserProfile(profile);
        Profile createdProfile = response.getResult();
        Assert.assertNotNull(response);
        Assert.assertNotNull(createdProfile);
        Assert.assertTrue(createdProfile.isValid());
        Assert.assertTrue(response.getStatus());
        GenieServiceDBHelper.findProfile();

        return createdProfile;
    }

    /**
     * Test to create new user profile.
     */
    @Test
    public void _1ShouldCreateUserProfile() {

        final Profile profile = new Profile("Happy1 " + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setAge(4);
        profile.setDay(12);
        profile.setMonth(11);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);
        Profile createdProfile = genieResponse.getResult();

        GenieServiceDBHelper.findProfile();
        Assert.assertNotNull(genieResponse);
        Assert.assertNotNull(createdProfile);
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertTrue(createdProfile.isValid());
        Assert.assertEquals("Month should not be null", (genieResponse.getResult()).getMonth(), profile.getMonth());
        Assert.assertEquals("Handle should not be null", (genieResponse.getResult()).getHandle(), profile.getHandle());
        Assert.assertEquals("Avatar should not be null", (genieResponse.getResult()).getAvatar(), profile.getAvatar());
        Assert.assertEquals("Day should not be null", (genieResponse.getResult()).getDay(), profile.getDay());

    }

    /**
     * Test to update new profile.
     */
    @Test
    public void _2ShouldUpdateUserProfile() {

        final Profile profile = createNewUserProfile("Happy2", false);
        profile.setUid(profile.getUid());

        String newHandle = "Happy22" + UUID.randomUUID().toString();
        profile.setHandle(newHandle);
        profile.setDay(23);
        profile.setMonth(12);

        GenieResponse<Profile> response = activity.updateUserProfile(profile);
        Profile updatedProfile = response.getResult();

        Assert.assertNotNull(response);
        Assert.assertNotNull(profile);
        Assert.assertTrue("VALID_PROFILE", updatedProfile.isValid());
        Assert.assertEquals(profile.getUid(), updatedProfile.getUid());
//        Assert.assertNotEquals(profile.getDay(), updatedProfile.getDay());
//        Assert.assertNotEquals(profile.getHandle(), updatedProfile.getHandle());
        GenieServiceDBHelper.findProfile();
    }

    /**
     * Test to set current user.
     */
    @Test
    public void _5ShouldSetCurrentUser() {

        final Profile newUserProfile = createNewUserProfile("Happy5", false);
        Assert.assertNotNull(newUserProfile);
        GenieServiceDBHelper.findProfile();

        GenieResponse genieResponse = activity.setCurrentUser(newUserProfile.getUid());
        Assert.assertNotNull(genieResponse);
        Assert.assertTrue(genieResponse.getStatus());

        GenieResponse<Profile> response = activity.getCurrentUser();
        Profile currentProfile = response.getResult();
        Assert.assertTrue(currentProfile.isValid());
        Assert.assertEquals(currentProfile.getUid(), newUserProfile.getUid());
        Assert.assertEquals(currentProfile.getAge(), newUserProfile.getAge());
        Assert.assertEquals(currentProfile.getDay(), newUserProfile.getDay());
        Assert.assertEquals(currentProfile.getMonth(), newUserProfile.getMonth());


    }

    /**
     * Test to delete user profile.
     */
    @Test
    public void _6ShouldDeleteUserProfile() {

        final Profile createdProfile = createNewUserProfile("Happy6", false);

        GenieServiceDBHelper.findProfile();

        GenieResponse response = activity.deleteUserProfile(createdProfile.getUid());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus());

        userProfileDoesNotExist();
    }

    /**
     * Test to delete user profile if the user is the current user.
     */
    @Test
    public void _11ShouldDeleteUserProfileIfUserIsCurrentUser() {

        final Profile profile = createNewUserProfile("Happy11", false);

        GenieServiceDBHelper.findProfile();

        GenieResponse response = activity.setCurrentUser(profile.getUid());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus());

        GenieResponse genieResponse = activity.deleteUserProfile(profile.getUid());
        Assert.assertNotNull(genieResponse);
        Assert.assertTrue(response.getStatus());
        userProfileDoesNotExist();

        GenieResponse<Profile> response1 = activity.getAnonymousUser();
        Profile anonymousProfile = response1.getResult();
        Assert.assertNotNull(anonymousProfile);
        Assert.assertTrue(response1.getStatus());

        Assert.assertEquals(-1, anonymousProfile.getAge());
    }

    /**
     * Test to create group user
     */
    @Test
    public void _7ShouldCreateGroupUser() {
        final Profile profile = new Profile("Group7", "@drawable/ic_avatar2", "en");
        profile.setGroupUser(true);
        profile.setAge(8);
        profile.setDay(17);
        profile.setMonth(5);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);
        Profile groupUser = genieResponse.getResult();
        Assert.assertNotNull(genieResponse);
        Assert.assertNotNull(groupUser);
        Assert.assertTrue(genieResponse.getStatus());

        GenieServiceDBHelper.findProfile();

        Assert.assertEquals("Avatar should not be null", (genieResponse.getResult()).getAvatar(), profile.getAvatar());
        Assert.assertEquals("Handle should not be null", (genieResponse.getResult()).getHandle(), profile.getHandle());
        Assert.assertEquals("Gender should be null", (genieResponse.getResult()).getGender(), profile.getGender());
        Assert.assertEquals("IS_GROUP_USER should be true", (genieResponse.getResult()).isGroupUser(), profile.isGroupUser());
        Assert.assertEquals("AGE should be -1 for group by default", (genieResponse.getResult()).getAge(), profile.getAge());
        Assert.assertEquals("DAY should be -1 for group by default", (genieResponse.getResult()).getDay(), profile.getDay());
        Assert.assertEquals("MONTH should be -1 for group by default", (genieResponse.getResult()).getMonth(), profile.getMonth());

    }

    /**
     * Test to update group user.
     */
    @Test
    public void _8ShouldUpdateGroupUser() {

        final Profile profile = createNewUserProfile("Group8", true);
        String newHandle = "Group88";
        profile.setHandle(newHandle);

        GenieResponse<Profile> response = activity.updateUserProfile(profile);
        Profile updatedProfile = response.getResult();

        Assert.assertNotNull(response);
        Assert.assertNotNull(profile);
        Assert.assertTrue("VALID_GROUP_PROFILE", updatedProfile.isValid());
        Assert.assertEquals(profile.getUid(), updatedProfile.getUid());
        GenieServiceDBHelper.findProfile();

    }

    /**
     * Test to validate set current user for the group profile.
     */
    @Test
    public void _9ShouldValidateSetCurrentUserForGroupUser() {
        final Profile profile = new Profile("Group9", "@drawable/ic_avatar2", "en");
        profile.setGroupUser(true);
        profile.setAge(5);
        profile.setMonth(11);
        profile.setDay(13);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);
        Profile createdProfile = genieResponse.getResult();

        Assert.assertNotNull(createdProfile);
        GenieServiceDBHelper.findProfile();

        GenieResponse genieResponse1 = activity.setCurrentUser(createdProfile.getUid());
        Assert.assertNotNull(genieResponse1);
        Assert.assertTrue(genieResponse1.getStatus());

        GenieResponse<Profile> genieResponse2 = activity.getCurrentUser();
        Profile currentUserProfile = genieResponse2.getResult();
        Assert.assertNotNull(currentUserProfile);
        Assert.assertTrue(currentUserProfile.isValid());
        Assert.assertEquals(profile.getUid(), currentUserProfile.getUid());
        Assert.assertEquals("IS_GROUP_USER should be true", (genieResponse.getResult()).isGroupUser(), profile.isGroupUser());
        Assert.assertEquals("AGE should be defaulted to -1", (genieResponse.getResult()).getAge(), -1);
        Assert.assertEquals("DAY should be defaulted to -1", (genieResponse.getResult()).getDay(), -1);
        Assert.assertEquals("MONTH should be defaulted to -1", (genieResponse.getResult()).getMonth(), -1);
        Assert.assertEquals("STANDARD should be defaulted to -1", (genieResponse.getResult()).getStandard(), -1);


    }

    /**
     * Test to delete group user.
     */
    @Test
    public void _10ShouldDeleteGroupUser() {

        final Profile profile = createNewUserProfile("Group10", true);

        GenieServiceDBHelper.findProfile();

        GenieResponse genieResponse = activity.deleteUserProfile(profile.getUid());
        Assert.assertNotNull(genieResponse);
        Assert.assertTrue(genieResponse.getStatus());
        userProfileDoesNotExist();

    }

    /**
     * Check for leap year.
     */
    @Test
    public void _12ShouldValidateLeapYear() {
        final Profile profile = new Profile("Happy12", "@drawable/ic_avatar2", "en");
        profile.setAge(7);
        profile.setDay(29);
        profile.setMonth(2);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.fail("ERROR DATE VALIDATION. Response " + genieResponse.getErrorMessages().get(0));
    }

    /**
     * Test when month is not set
     */
    @Test
    public void _13ShouldValidateDayAndMonthIfOnlyDayIsSet() {
        final Profile profile = new Profile("Happy13" + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setDay(12);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.fail("ERROR DATE VALIDATION. Response " + genieResponse.getErrorMessages().get(0));
    }

    /**
     * Test when day is not set
     */
    @Test
    public void _14ShouldValidateDayAndMonthIfOnlyMonthIsSet() {
        final Profile profile = new Profile("Happy14" + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setMonth(12);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        userProfileDoesNotExist();
        Assert.fail("ERROR DATE VALIDATION. Response " + genieResponse.getErrorMessages().get(0));
    }

    /**
     * Test for month validation.
     */
    @Test
    public void _15ShouldValidateDayAndMonth() {
        final Profile profile = new Profile("Happy15" + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setAge(8);
        profile.setDay(2);
        profile.setMonth(14);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        userProfileDoesNotExist();
        Assert.fail("ERROR DATE VALIDATION. Response " + genieResponse.getErrorMessages().get(0));
    }

    /**
     * Check for empty handler
     */
    @Test
    public void _14shouldBeAbleToLogGeErrorEventForEmptyHandler() {
        final Profile profile = new Profile("", "@drawable/ic_avatar2", "en");
        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.fail("ERROR DATE VALIDATION. Response " + genieResponse.getErrorMessages().get(0));
    }

    /**
     * Check for empty avatar
     */
    @Test
    public void _15shouldBeAbleToLogGeErrorEventForEmptyAvatar() {
        final Profile profile = new Profile("Happy15", "", "en");
        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.fail("ERROR DATE VALIDATION. Response " + genieResponse.getErrorMessages().get(0));
    }

    /**
     * Test to set anonymous user.
     */
    @Test
    public void _3ShouldSetAnonymousUser() {

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
    public void _4ShouldGetAnonymousUser() {

        GenieResponse<String> genieResponse = activity.setAnonymousUser();
        String result = genieResponse.getResult();
        Assert.assertNotNull(result);
        Assert.assertTrue(genieResponse.getStatus());

        GenieResponse<Profile> response = activity.getAnonymousUser();
        Profile anonymousProfile = response.getResult();
        Assert.assertNotNull(anonymousProfile);
        Assert.assertEquals(-1, anonymousProfile.getAge());
    }
}
