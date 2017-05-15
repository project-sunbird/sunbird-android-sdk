package org.ekstep.genieservices.UserProfile;

import com.google.gson.Gson;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.fail;

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
        Assert.assertEquals(0, profile.size());
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

        activity.createUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                GenieServiceDBHelper.findProfile();
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
            }
        });

        return profile;
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

        activity.createUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                GenieServiceDBHelper.findProfile();
                Assert.assertEquals("Month should not be null", ((Profile) genieResponse.getResult()).getMonth(), profile.getMonth());
                Assert.assertEquals("Handle should not be null", ((Profile) genieResponse.getResult()).getHandle(), profile.getHandle());
                Assert.assertEquals("Avatar should not be null", ((Profile) genieResponse.getResult()).getAvatar(), profile.getAvatar());
                Assert.assertEquals("Day should not be null", ((Profile) genieResponse.getResult()).getDay(), profile.getDay());

            }

            @Override
            public void onError(GenieResponse genieResponse) {
                fail("ERROR PROFILE CREATION. Response :: " + genieResponse.getErrorMessages().get(0));
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
            }
        });
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

        waitForGenieToBecomeIdle();

        activity.updateUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                GenieServiceDBHelper.findProfile();
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    /**
     * Test to set current user.
     */
    @Test
    public void _5ShouldSetCurrentUser() {

        final Profile newUserProfile = createNewUserProfile("Happy5", false);
        GenieServiceDBHelper.findProfile();

        waitForGenieToBecomeIdle();

        activity.setCurrentUser(newUserProfile.getUid(), new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                Assert.assertTrue(genieResponse.getStatus());

                activity.getCurrentUser(new IResponseHandler() {
                    @Override
                    public void onSuccess(GenieResponse genieResponse) {
                        Assert.assertEquals(((Profile) genieResponse.getResult()).getUid(), newUserProfile.getUid());
                        Assert.assertEquals(((Profile) genieResponse.getResult()).getAge(), newUserProfile.getAge());
                        Assert.assertEquals(((Profile) genieResponse.getResult()).getDay(), newUserProfile.getDay());
                        Assert.assertEquals(((Profile) genieResponse.getResult()).getMonth(), newUserProfile.getMonth());
                    }

                    @Override
                    public void onError(GenieResponse genieResponse) {
                        Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
                    }
                });

            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    /**
     * Test to delete user profile.
     */
    @Test
    public void _6ShouldDeleteUserProfile() {

        final Profile createdProfile = createNewUserProfile("Happy6", false);

        GenieServiceDBHelper.findProfile();

        waitForGenieToBecomeIdle();

        activity.deleteUserProfile(createdProfile.getUid(), new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                userProfileDoesNotExist();
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    /**
     * Test to delete user profile if the user is the current user.
     */
    @Test
    public void _11ShouldDeleteUserProfileIfUserIsCurrentUser() {

        final Profile profile = createNewUserProfile("Happy11", false);
        GenieServiceDBHelper.findProfile();

        waitForGenieToBecomeIdle();

        activity.setCurrentUser(profile.getUid(), new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {

                Assert.assertEquals(((Profile) genieResponse.getResult()).getHandle(), profile.getHandle());
                Assert.assertEquals(((Profile) genieResponse.getResult()).getAge(), profile.getAge());
                Assert.assertEquals(((Profile) genieResponse.getResult()).getDay(), profile.getDay());
                Assert.assertEquals(((Profile) genieResponse.getResult()).getMonth(), profile.getMonth());

                activity.deleteUserProfile(profile.getUid(), new IResponseHandler() {
                    @Override
                    public void onSuccess(GenieResponse genieResponse) {

                        userProfileDoesNotExist();

                        activity.getAnonymousUser(new IResponseHandler() {
                            @Override
                            public void onSuccess(GenieResponse genieResponse) {
                                Assert.assertTrue(genieResponse.getStatus());
                            }

                            @Override
                            public void onError(GenieResponse genieResponse) {
                                Assert.assertEquals("Failure :: ", genieResponse.getErrorMessages().get(0));
                            }
                        });
                    }

                    @Override
                    public void onError(GenieResponse genieResponse) {
                        Assert.assertFalse(genieResponse.getStatus());
                    }
                });
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertFalse(genieResponse.getStatus());
            }
        });
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

        activity.createUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                GenieServiceDBHelper.findProfile();
                Assert.assertEquals("Avatar should not be null", ((Profile) genieResponse.getResult()).getAvatar(), profile.getAvatar());
                Assert.assertEquals("Handle should not be null", ((Profile) genieResponse.getResult()).getHandle(), profile.getHandle());
                Assert.assertEquals("Gender should be null", ((Profile) genieResponse.getResult()).getGender(), profile.getGender());
                Assert.assertEquals("IS_GROUP_USER should be true", ((Profile) genieResponse.getResult()).isGroupUser(), profile.isGroupUser());
                Assert.assertEquals("AGE should be -1 for group by default", ((Profile) genieResponse.getResult()).getAge(), profile.getAge());
                Assert.assertEquals("DAY should be -1 for group by default", ((Profile) genieResponse.getResult()).getDay(), profile.getDay());
                Assert.assertEquals("MONTH should be -1 for group by default", ((Profile) genieResponse.getResult()).getMonth(), profile.getMonth());

            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    /**
     * Test to update group user.
     */
    @Test
    public void _8ShouldUpdateGroupUser() {

        final Profile profile = createNewUserProfile("Group8", true);
        String newHandle = "Group88";
        profile.setHandle(newHandle);

        activity.updateUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                GenieServiceDBHelper.findProfile();
                Assert.assertTrue(genieResponse.getStatus());
                Assert.assertEquals(((Profile) genieResponse.getResult()).getUid(), profile.getUid());
                Assert.assertEquals(((Profile) genieResponse.getResult()).getGender(), profile.getGender());
                Assert.assertEquals(((Profile) genieResponse.getResult()).getAge(), profile.getAge());
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
            }
        });
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

        activity.createUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                GenieServiceDBHelper.findProfile();

                activity.setCurrentUser(profile.getUid(), new IResponseHandler() {
                    @Override
                    public void onSuccess(GenieResponse genieResponse) {

                        activity.getCurrentUser(new IResponseHandler() {
                            @Override
                            public void onSuccess(GenieResponse genieResponse) {

                                Assert.assertEquals("IS_GROUP_USER should be true", ((Profile) genieResponse.getResult()).isGroupUser(), profile.isGroupUser());
                                Assert.assertEquals("AGE should be defaulted to -1", ((Profile) genieResponse.getResult()).getAge(), -1);
                                Assert.assertEquals("DAY should be defaulted to -1", ((Profile) genieResponse.getResult()).getDay(), -1);
                                Assert.assertEquals("MONTH should be defaulted to -1", ((Profile) genieResponse.getResult()).getMonth(), -1);
                                Assert.assertEquals("STANDARD should be defaulted to -1", ((Profile) genieResponse.getResult()).getStandard(), -1);
                            }

                            @Override
                            public void onError(GenieResponse genieResponse) {
                                Assert.assertEquals("ERROR GET CURRENT OBJECT ::", genieResponse.getErrorMessages().get(0));

                            }
                        });
                    }

                    @Override
                    public void onError(GenieResponse genieResponse) {
                        Assert.assertEquals("ERROR SET CURRENT OBJECT ::", genieResponse.getErrorMessages().get(0));
                    }
                });
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("ERROR SET CURRENT OBJECT ::", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    /**
     * Test to delete group user.
     */
    @Test
    public void _10ShouldDeleteGroupUser() {

        final Profile profile = createNewUserProfile("Group10", true);

        GenieServiceDBHelper.findProfile();

        activity.deleteUserProfile(profile.getUid(), new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                userProfileDoesNotExist();
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
            }
        });
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

        activity.createUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                fail("ERROR DATE VALIDATION. Response" + new Gson().toJson(genieResponse));
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertFalse(genieResponse.getStatus());
            }
        });
    }

    /**
     * Test when month is not set
     */
    @Test
    public void _13ShouldValidateDayAndMonthIfOnlyDayIsSet() {
        final Profile profile = new Profile("Happy13" + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setDay(12);

        activity.createUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                fail("ERROR DATE VALIDATION. Response" + new Gson().toJson(genieResponse));
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertFalse(genieResponse.getStatus());
            }
        });
    }

    /**
     * Test when day is not set
     */
    @Test
    public void _14ShouldValidateDayAndMonthIfOnlyMonthIsSet() {
        final Profile profile = new Profile("Happy14" + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setMonth(12);

        activity.createUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                fail("ERROR DATE VALIDATION. Response" + new Gson().toJson(genieResponse));
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertFalse(genieResponse.getStatus());
            }
        });
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

        activity.createUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                fail("ERROR DATE VALIDATION. Response" + new Gson().toJson(genieResponse));
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertFalse(genieResponse.getStatus());
            }
        });
    }

    /**
     * Check for empty handler
     */
    @Test
    public void _14shouldBeAbleToLogGeErrorEventForEmptyHandler() {
        final Profile profile = new Profile("", "@drawable/ic_avatar2", "en");
        activity.createUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                fail("ERROR PROFILE VALIDATION. Response" + new Gson().toJson(genieResponse));
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertFalse(genieResponse.getStatus());
            }
        });
    }

    /**
     * Check for empty avatar
     */
    @Test
    public void _15shouldBeAbleToLogGeErrorEventForEmptyAvatar() {
        final Profile profile = new Profile("Happy15", "", "en");
        activity.createUserProfile(profile, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                fail("ERROR PROFILE VALIDATION. Response" + new Gson().toJson(genieResponse));
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertFalse(genieResponse.getStatus());
            }
        });
    }

    /**
     * Test to set anonymous user.
     */
    @Test
    public void _3ShouldSetAnonymousUser() {

        activity.setAnonymousUser(new IResponseHandler() {

            @Override
            public void onSuccess(GenieResponse genieResponse) {
                Assert.assertTrue(genieResponse.getStatus());
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    /**
     * Test to get anonymous user.
     */
    @Test
    public void _4ShouldGetAnonymousUser() {

        activity.setAnonymousUser(new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                Assert.assertTrue(genieResponse.getStatus());

                waitForGenieToBecomeIdle();

                activity.getAnonymousUser(new IResponseHandler() {
                    @Override
                    public void onSuccess(GenieResponse genieResponse) {
                        Assert.assertTrue(genieResponse.getStatus());
                    }

                    @Override
                    public void onError(GenieResponse genieResponse) {
                        Assert.assertEquals("FAILURE ::", genieResponse.getErrorMessages().get(0));
                    }
                });
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("FAILURE ::", genieResponse.getErrorMessages().get(0));
            }
        });
    }
}
