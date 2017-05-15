package org.ekstep.genieservices.UserProfile;

import android.util.Log;

import com.google.gson.Gson;

import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.fail;

/**
 * Created by Sneha on 5/12/2017.
 */

public class UserProfileTest extends GenieServiceTestBase {

    private static final String TAG = UserProfileTest.class.getSimpleName();

    /**
     * Method to create a new user profile and return uid.
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
                Profile createdProfile = (Profile) genieResponse.getResult();
                Log.v(TAG, "New Profile :: Name:" + createdProfile.getHandle() + "\n uid:" + createdProfile.getUid());
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Log.v(TAG, "ERROR CREATING PROFILE : " + genieResponse.getErrorMessages().get(0));
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
                Profile createdProfile = (Profile) genieResponse.getResult();
//                assertProfileEntryExist(getGenieServicesDatabase(), createdProfile.getHandle(), false);
                Assert.assertEquals("Month should not be null", ((Profile) genieResponse.getResult()).getMonth(), profile.getMonth());
                Assert.assertEquals("Handle should not be null", ((Profile) genieResponse.getResult()).getHandle(), profile.getHandle());
                Assert.assertEquals("Avatar should not be null", ((Profile) genieResponse.getResult()).getAvatar(), profile.getAvatar());
                Assert.assertEquals("Day should not be null", ((Profile) genieResponse.getResult()).getDay(), profile.getDay());

                Log.v(TAG, "PROFILE CREATION SUCCESSFUL :: Name:" + createdProfile.getHandle() + " Age:" + createdProfile.getAge() +
                        " UID:" + createdProfile.getUid());
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
                //assertProfileEntryExist(getGenieServicesDatabase(), profile.getHandle(), false);
                Log.v(TAG, "UPDATE PROFILE SUCCESSFUL :: name : " + profile.getHandle() + " uuid:: " + profile.getUid() + "day : " + profile.getDay());
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Log.v(TAG, "ERROR PROFILE UPDATION :: " + genieResponse.getErrorMessages().get(0));
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
//        assertProfileEntryExist(getGenieServicesDatabase(), newUserProfile.getHandle(), false);

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
                        Log.v(TAG, "SUCCESSFUL GET CURRENT USER :: " + genieResponse.getStatus());
                    }

                    @Override
                    public void onError(GenieResponse genieResponse) {
                        Log.v(TAG, "Failure :: " + genieResponse.getError());
//                        Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
                    }
                });

            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
                Log.v(TAG, "Failure set current :: " + genieResponse.getErrorMessages().get(0));
            }
        });
    }

    /**
     * Test to delete user profile.
     */
    @Test
    public void _6ShouldDeleteUserProfile() {

        final Profile createdProfile = createNewUserProfile("Happy6", false);
//        assertProfileEntryExist(getGenieServicesDatabase(), handle, false);
        Log.v(TAG, "uid:" + createdProfile);

        waitForGenieToBecomeIdle();

        activity.deleteUserProfile(createdProfile.getUid(), new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
//                assertUserProfileDoesNotExist(getGenieServicesDatabase(), uid);
                Log.v(TAG, "SUCCESSFULLY DELETED USER :: " + genieResponse.getMessage());
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Log.v(TAG, "FAILED TO DELETE USER PROFILE : " + genieResponse.getErrorMessages().get(0));
            }
        });
    }

    /**
     * Test to delete user profile if the user is the current user.
     */
    @Test
    public void _11ShouldDeleteUserProfileIfUserIsCurrentUser() {

        final Profile profile = createNewUserProfile("Happy11", false);

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
//                      assertUserProfileDoesNotExist(getGenieServicesDatabase(), ((Profile) genieResponse.getResult()).getUid());
                        Log.v(TAG, "SUCCESSFULLY DELETED CURRENT USER PROFILE");

                        activity.getAnonymousUser(new IResponseHandler() {
                            @Override
                            public void onSuccess(GenieResponse genieResponse) {
                                Log.v(TAG, "SUCCESSFUL GET ANONYMOUS USER");
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
                        Log.v(TAG, "UNABLE TO DELETE USER PROFILE");
                    }
                });
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Log.v(TAG, "Failure to set current user :: " + genieResponse.getErrorMessages().get(0));
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
                Log.v(TAG, "SUCCESSFULLY CREATED GROUP PROFILE :: name : " + profile.getHandle() + " uid : " + profile.getUid());
//                assertProfileEntryExist(getGenieServicesDatabase(), profile.getHandle(), true);
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
                Log.v(TAG, "FAILURE :: " + genieResponse.getErrorMessages().get(0));
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
//          assertProfileEntryExist(getGenieServicesDatabase(), profile.getHandle(), true);
                Assert.assertTrue(genieResponse.getStatus());
                Assert.assertEquals(((Profile) genieResponse.getResult()).getUid(), profile.getUid());
                Assert.assertEquals(((Profile) genieResponse.getResult()).getGender(), profile.getGender());
                Assert.assertEquals(((Profile) genieResponse.getResult()).getAge(), profile.getAge());

                Log.v(TAG, "SUCCESSFULLY UPDATED GROUP :: name: " + profile.getHandle() + "age :: " + profile.getAge());
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Log.v(TAG, "FAILURE :: " + genieResponse.getErrorMessages().get(0));
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
                Log.v(TAG, "SUCCESS CREATE USER");
//                assertProfileEntryExist(getGenieServicesDatabase(), profile.getHandle(), true);

                activity.setCurrentUser(profile.getUid(), new IResponseHandler() {
                    @Override
                    public void onSuccess(GenieResponse genieResponse) {
                        Log.v(TAG, "SUCCESS SET CURRENT USER");

                        activity.getCurrentUser(new IResponseHandler() {
                            @Override
                            public void onSuccess(GenieResponse genieResponse) {
                                Log.v(TAG, "SUCCESS GET CURRENT USER");

                                Assert.assertEquals("IS_GROUP_USER should be true", ((Profile) genieResponse.getResult()).isGroupUser(), profile.isGroupUser());
                                Assert.assertEquals("AGE should be defaulted to -1", ((Profile) genieResponse.getResult()).getAge(), -1);
                                Assert.assertEquals("DAY should be defaulted to -1", ((Profile) genieResponse.getResult()).getDay(), -1);
                                Assert.assertEquals("MONTH should be defaulted to -1", ((Profile) genieResponse.getResult()).getMonth(), -1);
                                Assert.assertEquals("STANDARD should be defaulted to -1", ((Profile) genieResponse.getResult()).getStandard(), -1);
                            }

                            @Override
                            public void onError(GenieResponse genieResponse) {
                                Log.v(TAG, "GET CURRENT FAILURE :: " + genieResponse.getError() + "Msg ::" + genieResponse.getErrorMessages().get(0));
                                Assert.assertEquals("ERROR GET CURRENT OBJECT ::", genieResponse.getErrorMessages().get(0));

                            }
                        });
                    }

                    @Override
                    public void onError(GenieResponse genieResponse) {
                        Log.v(TAG, "Failure :: " + genieResponse.getError());
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

        activity.deleteUserProfile(profile.getUid(), new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
//                assertUserProfileDoesNotExist(getGenieServicesDatabase(),profile.getUid());
                Log.v(TAG, "SUCCESSFULLY DELETED GROUP USER :: " + genieResponse.getStatus());
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Log.v(TAG, "FAILED DELETE PROFILE :: " + genieResponse.getErrorMessages().get(0));
                Log.v(TAG, "ERROR :: " + genieResponse.getError());
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
                Log.v(TAG, "Failure :: " + genieResponse.getErrorMessages().get(0));
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
                Log.v(TAG, "Failure :: " + genieResponse.getErrorMessages().get(0));
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
                Log.v(TAG, "Success :: " + genieResponse.getStatus());
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Log.v(TAG, "Failure :: " + genieResponse.getErrorMessages().get(0));
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
                Log.e(TAG, "Failure :: " + genieResponse.getErrorMessages().get(0));
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
                Log.e(TAG, "Failure :: " + genieResponse.getErrorMessages().get(0));
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
                Log.e(TAG, "Failure :: " + genieResponse.getErrorMessages().get(0));
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
                Log.v(TAG, "SUCCESSFULLY SET ANONYMOUS USER :: " + genieResponse.getStatus());
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure ::", genieResponse.getErrorMessages().get(0));
                Log.v(TAG, "Failure :: " + genieResponse.getErrorMessages().get(0));
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
                Log.v(TAG, "SUCCESSFULLY SET ANONYMOUS USER");

                waitForGenieToBecomeIdle();

                activity.getAnonymousUser(new IResponseHandler() {
                    @Override
                    public void onSuccess(GenieResponse genieResponse) {
                        Assert.assertTrue(genieResponse.getStatus());
                        Log.v(TAG, "SUCCESSFULLY GET ANONYMOUS USER");
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
