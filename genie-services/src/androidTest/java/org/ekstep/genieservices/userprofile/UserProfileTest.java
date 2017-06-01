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
     * Scenario : To create a new user with specific {@link Profile}.
     * Given : To create a new user profile.
     * When :
     * Then : On Successful creation of new profile, the response will return status as TRUE and with successful message.
     */
    @Test
    public void shouldCreateUserProfile() {

        Profile profile = new Profile("Happy1", "@drawable/ic_avatar2", "en");
        profile.setAge(4);
        profile.setDay(12);
        profile.setMonth(11);

        Profile createdProfile = createNewProfile(profile);

        Profile profileinDb = GenieServiceDBHelper.findProfile().get(0);

        AssertProfile.verifyProfile(createdProfile, profileinDb);
        AssertProfile.checkIfTelemetryEventIsLogged("GE_CREATE_PROFILE", createdProfile);
    }

    /**
     * Scenario : To update the specific profile that is passed to it.
     * Given : To update a specific profile
     * When :
     * Then : On successful updating the profile, the response will return status as TRUE
     * and with the updated profile set in result.
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
     * Scenario : To set the specific uid passed to it as active current user.
     * Given : To set a user as current user.
     * When :
     * Then : On successful setting a user active, the response will return status as TRUE
     * and with successful message
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
     * Scenario : To delete a existing user with a specific uid.
     * Given : To delete existing user.
     * When :
     * Then : On successful deletion of a user, the response will return status as TRUE
     * and with result set as Profile related data
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
     * Scenario : Deleting the user profile which is set as current user.
     * Given : To delete user profile which is a current user
     * When : A profile is created, set as current user and that current user profile is deleted.
     * Then : Profile is deleted, anonymous user is set as current user.
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

        AssertProfile.verifyAnonymousUser(anonymousProfile);
        Assert.assertEquals(-1, anonymousProfile.getAge());
    }

    /**
     * Scenario : To create a new group user with specific {@link Profile}.
     * Given : To create a new group user profile.
     * When :
     * Then : On Successful creation of new group profile, the response will return status as TRUE and with successful message.
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
     * Scenario : To update the specific group profile that is passed to it.
     * Given : To update a specific group profile
     * When :
     * Then : On successful updating the profile, the response will return status as TRUE
     * and with the updated group profile sent in result.
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
     * Scenario : To set the specific uid passed to it as active current group user.
     * Given : Set a group user as current group user.
     * When : User creates a new group user, sets the user as current group user.
     * Then : On successful setting a group user active, the response will return status as TRUE and with successful message
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

    /**
     * Scenario : To delete a existing group user with a specific uid.
     * Given : To delete existing group user.
     * When :
     * Then : On successful deletion of a group user, the response will return status as TRUE
     * and with result set as Profile related data
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
     * Scenario :
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
     * Scenario : Invalid Date error while creating a new user profile.
     * Given : A new user profile is bring created.
     * When : User does not set the month.
     * Then : Invalid date error message is displayed.
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
     * Scenario : Invalid Date error while creating a new user profile.
     * Given : A new user profile is bring created.
     * When : User does not set the day.
     * Then : Invalid error message is displayed.
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
     * Scenario : Validate date for create new profile.
     * Given : A new user profile is being created.
     * When : Invalid date is given.
     * Then : Invalid date error message is displayed.
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
     * Scenario : When user is creating new profile, user does not set handle,
     * handle cannot be empty error message is displayed.
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
     * Scenario : When user is creating new profile, user does not set avatar,
     * avatar cannot be empty error message is displayed.
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
     * Scenario : When user is creating new profile, user sends null instead of {@link Profile},
     */
    @Test
    public void shouldCheckForNullProfile() {

        GenieResponse<Profile> genieResponse = activity.createUserProfile(null);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
    }

    /**
     * Scenario : To get the anonymous user from getAnonymousUser() api and sets it to current active user.
     * Given : To set an anonymous user.
     * When :
     * Then : On successful setting the anonymous user as active, the response will return status as TRUE
     * and with the uid of anonymous user set in result.
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
     * Scenario : To get the anonymous user the one if exists or a new anonymous user will be created.
     * Given : To get the anonymous user.
     * When :
     * Then :On successful fetching the anonymous user, the response will return status as TRUE and
     * with the profile of anonymous user set in result.
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
