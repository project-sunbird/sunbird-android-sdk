package org.ekstep.genieservices.userprofile;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentLearnerState;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.ProfileExportRequest;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportRequest;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.bean.enums.ContentAccessStatus;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;
import org.ekstep.genieservices.commons.db.contract.UserEntry;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.profile.db.model.ContentAccessModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 5/12/2017.
 *
 * @author Sneha
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserProfileTest extends GenieServiceTestBase {

    private static final String ONE_PROFILE_EPAR = "epar/" + "PROFILE_ONE.epar";
    private static final String TWO_PROFILE_EPAR = "epar/" + "TWOProfile.epar";
    private static final String COLLECTION_ASSET_PATH = "Download/Times_Tables_2_to_10.ecar";
    private final String ONE_PROFILE_FILE_PATH = DESTINATION + File.separator + "PROFILE_ONE.epar";
    private final String PROFILE_ID = "76c33e90-5b82-4739-af56-78580bb0f91a";

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
    public void _11ShouldCreateUserProfileSuccessfully() {

        Profile profile = new Profile("Happy1", "@drawable/ic_avatar2", "en");
        profile.setAge(4);
        profile.setDay(12);
        profile.setMonth(11);

        Profile createdProfile = createNewProfile(profile);

        Profile profileinDb = GenieServiceDBHelper.findProfile().get(0);

        AssertProfile.verifyProfile(createdProfile, profileinDb);
        AssertProfile.checkTelemtryEventIsLoggedIn("GE_CREATE_PROFILE", createdProfile);
    }

    /**
     * Scenario : To update the specific profile that is passed to it.
     * Given : To update a specific profile
     * When :
     * Then : On successful updating the profile, the response will return status as TRUE
     * and with the updated profile set in result.
     */
    @Test
    public void _12ShouldUpdateUserProfileSuccessfully() {
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
        AssertProfile.checkTelemtryEventIsLoggedIn("GE_UPDATE_PROFILE", updatedProfile);
    }


    @Test
    public void _13ShouldReturnProfileNotFoundErrorResponseForInvalidProfile() {

        Profile profileForUpdation = new Profile("Happy22", "@drawable/ic_avatar2", "en");
        profileForUpdation.setUid("samplerandomuid");
        profileForUpdation.setDay(23);
        profileForUpdation.setMonth(12);

        GenieResponse<Profile> response = activity.updateUserProfile(null);
        Assert.assertFalse(response.getStatus());
        Assert.assertEquals(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, response.getError());
        Assert.assertEquals(ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, response.getErrorMessages().get(0));
    }

    @Test
    public void _14ShouldReturnValidationErrorErrorResponseForInvalidProfile() {

        Profile profile = new Profile("Happy2", "@drawable/ic_avatar2", "en");
        Profile createdProfile = createNewProfile(profile);

        Profile profileForUpdation = new Profile("Happy22", "@drawable/ic_avatar2", "en");
        profileForUpdation.setUid(createdProfile.getUid());
        profileForUpdation.setDay(23);
        profileForUpdation.setMonth(15);
        GenieResponse<Profile> response = activity.updateUserProfile(profileForUpdation);
        Assert.assertFalse(response.getStatus());
        Assert.assertEquals(ServiceConstants.ErrorCode.VALIDATION_ERROR, response.getError());
    }


    /**
     * Scenario : To set the specific uid passed to it as active current user.
     * Given : To set a user as current user.
     * When :
     * Then : On successful setting a user active, the response will return status as TRUE
     * and with successful message
     */
    @Test
    public void _21ShouldSetCurrentUserSuccessfully() {
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

    @Test
    public void _22ShouldReturnInvalidUserErrorForInvalidUserId() {

        GenieResponse genieResponse = activity.setCurrentUser("sampleuid-111");
        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals(ServiceConstants.ErrorCode.INVALID_USER, genieResponse.getError());
        Assert.assertEquals(ServiceConstants.ErrorMessage.NO_USER_WITH_SPECIFIED_ID, genieResponse.getErrorMessages().get(0));


    }

    /**
     * Scenario : To delete a existing user with a specific uid.
     * Given : To delete existing user.
     * When :
     * Then : On successful deletion of a user, the response will return status as TRUE
     * and with result set as Profile related data
     */
    @Test
    public void _31ShouldDeleteUserProfileSuccessfully() {

        Profile profile = new Profile("Happy6", "@drawable/ic_avatar2", "en");
        Profile createdProfile = createNewProfile(profile);
        activity.setCurrentUser(createdProfile.getUid());
        ContentAccess contentAccess = new ContentAccess();
        contentAccess.setStatus(ContentAccessStatus.PLAYED.getValue());
        contentAccess.setContentId(CHILD_C3_ID + "sample");
        activity.addContentAccess(contentAccess);
        AssertProfile.checkTelemtryEventIsLoggedIn("GE_CREATE_PROFILE", createdProfile);

        GenieResponse response = activity.deleteUserProfile(createdProfile.getUid());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus());

        AssertProfile.verifyProfileisDeleted();
        AssertProfile.checkTelemetryDataForDeleteProfile("GE_DELETE_PROFILE");

    }

    /**
     * Scenario : Deleting the user profile which is set as current user.
     * Given : To delete user profile which is a current user
     * When : A profile is created, set as current user and that current user profile is deleted.
     * Then : Profile is deleted, anonymous user is set as current user.
     */
    @Test
    public void _32ShouldSetAnonymousUserIfDeleteUserProfileIsCurrentUser() {
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

    @Test
    public void _33ShouldReturnProfileNotFoundErrorIfGivenUidisNotAvailable() {

        GenieResponse response = activity.deleteUserProfile("sampleuid");
        Assert.assertFalse(response.getStatus());
        Assert.assertEquals(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, response.getError());
        Assert.assertEquals(ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, response.getErrorMessages().get(0));

    }

    /**
     * Scenario : To create a new group user with specific {@link Profile}.
     * Given : To create a new group user profile.
     * When :
     * Then : On Successful creation of new group profile, the response will return status as TRUE and with successful message.
     */
    @Test
    public void _41ShouldCreateGroupUserSuccessfully() {
        Profile profile = new Profile("Group7", "@drawable/ic_avatar2", "en");
        profile.setGroupUser(true);

        Profile createdProfile = createNewProfile(profile);

        Profile profileinDb = GenieServiceDBHelper.findProfile().get(0);

        AssertProfile.verifyProfile(createdProfile, profileinDb);
        AssertProfile.checkTelemtryEventIsLoggedIn("GE_CREATE_PROFILE", createdProfile);

    }

    /**
     * Scenario : To update the specific group profile that is passed to it.
     * Given : To update a specific group profile
     * When :
     * Then : On successful updating the profile, the response will return status as TRUE
     * and with the updated group profile sent in result.
     */
    @Test
    public void _42ShouldUpdateGroupUserSuccessfully() {

        GenieServiceDBHelper.clearProfileTable();

        Profile profile = new Profile("Group8", "@drawable/ic_avatar2", "en");
        profile.setGroupUser(true);

        final Profile createdProfile = createNewProfile(profile);
        profile.setUid(createdProfile.getUid());
        profile.setHandle("Group88");
        AssertProfile.checkTelemtryEventIsLoggedIn("GE_CREATE_PROFILE", createdProfile);

        GenieResponse<Profile> response = activity.updateUserProfile(profile);
        Profile updatedProfile = response.getResult();

        AssertProfile.verifyProfile(profile, updatedProfile);
        AssertProfile.checkTelemtryEventIsLoggedIn("GE_UPDATE_PROFILE", updatedProfile);

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
    public void _43ShouldValidateSetCurrentUserForGroupUser() {
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
    public void _44ShouldDeleteGroupUser() {
        Profile profile = new Profile("Group10 " + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setGroupUser(true);

        createNewProfile(profile);

        GenieResponse genieResponse = activity.deleteUserProfile(profile.getUid());
        Assert.assertNotNull(genieResponse);
        Assert.assertTrue(genieResponse.getStatus());

        AssertProfile.verifyProfileisDeleted();
        AssertProfile.checkTelemetryDataForDeleteProfile("GE_DELETE_PROFILE");
    }

    /**
     * Scenario :
     */

    @Test
    public void _51ShouldValidateLeapYear() {
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
    public void _52ShouldValidateDayAndMonthIfOnlyDayIsSet() {
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
    public void _53ShouldValidateDayAndMonthIfOnlyMonthIsSet() {
        final Profile profile = new Profile("Happy14" + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setMonth(12);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals("[invalid date, field: -1/12/2017]", genieResponse.getErrorMessages().get(0));
    }

    /**
     * Scenario : Validate date for create new profile.
     * Given : A new user profile is being created.
     * When : Invalid date is given.
     * Then : Invalid date error message is displayed.
     */
    @Test
    public void _54ShouldValidateDayAndMonth() {
        final Profile profile = new Profile("Happy15" + UUID.randomUUID().toString(), "@drawable/ic_avatar2", "en");
        profile.setAge(8);
        profile.setDay(2);
        profile.setMonth(14);

        GenieResponse<Profile> genieResponse = activity.createUserProfile(profile);

        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals("[invalid date, field: 2/14/2009]", genieResponse.getErrorMessages().get(0));

        //TODO verify GE_ERROR event
    }

    /**
     * Scenario : When user is creating new profile, user does not set handle,
     * handle cannot be empty error message is displayed.
     */
    @Test
    public void _61ShouldBeAbleToLogGeErrorEventForEmptyHandler() {
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
    public void _62ShouldBeAbleToLogGeErrorEventForEmptyAvatar() {
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
    public void _63ShouldCheckForNullProfile() {

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
    public void _71ShouldSetAnonymousUser() {

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
    public void _72ShouldGetAnonymousUser() {

        GenieResponse<String> genieResponse = activity.setAnonymousUser();
        String result = genieResponse.getResult();
        Assert.assertNotNull(result);
        Assert.assertTrue(genieResponse.getStatus());

        GenieResponse<Profile> response = activity.getAnonymousUser();
        Profile anonymousProfile = response.getResult();
        Assert.assertNotNull(anonymousProfile);
        AssertProfile.verifyAnonymousUser(anonymousProfile);
    }

    @Test
    public void _73ShouldGetAnonymousUserWhenNoAnonymoususerisCreated() {

        GenieServiceDBHelper.clearTable(UserEntry.TABLE_NAME);
        GenieResponse<Profile> genieResponse = activity.getAnonymousUser();
        Profile result = genieResponse.getResult();
        Assert.assertNotNull(result);
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(result.getUid());

    }

    /**
     * Scenario: To get the list of all the created profiles.
     * Given : To get the list of all profiles.
     * Then: On Successful fetching of the profiles, the response will status as TRUE and
     * list of profiles.
     */
    @Test
    public void _81GetAllUserProfileShouldReturnzeroProfiles() {


        GenieResponse<List<Profile>> genieResponse = activity.getAllUserProfile();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals(0, genieResponse.getResult().size());


    }

    /**
     * Scenario: To get the list of all the created profiles.
     * Given : To get the list of all profiles.
     * Then: On Successful fetching of the profiles, the response will status as TRUE and
     * list of profiles.
     */
    @Test
    public void _82shouldGetAllUserProfile() {

        GenieServiceDBHelper.clearProfileTable();

        final Profile profile1 = new Profile("Happy21", "@drawable/ic_avatar2", "en");
        final Profile profile2 = new Profile("Happy31", "@drawable/ic_avatar2", "en");
        createNewProfile(profile1);
        createNewProfile(profile2);

        GenieResponse<List<Profile>> genieResponse = activity.getAllUserProfile();
        Profile saved_profile1 = genieResponse.getResult().get(0);
        Profile saved_profile2 = genieResponse.getResult().get(1);

        Assert.assertEquals(profile1.getHandle(), saved_profile1.getHandle());
        Assert.assertEquals(profile2.getHandle(), saved_profile2.getHandle());

        Assert.assertEquals(profile1.getUid(), saved_profile1.getUid());
        Assert.assertEquals(profile2.getUid(), saved_profile2.getUid());

    }

    /**
     * Scenario: Import the profile by providing the filepath.
     * Given: Import the profile by providing the filepath.
     * Then: Profile gets successfully imported in the app.
     */
    @Test
    public void _83ShouldImportProfileSuccessfully() {

        GenieServiceDBHelper.clearProfileTable();
        GenieServiceDBHelper.clearUserTableDBEntry();
        EcarCopyUtil.createFileFromAsset(activity, ONE_PROFILE_EPAR, DESTINATION);

        ProfileImportRequest.Builder profileImportRequest = new ProfileImportRequest.Builder().fromFilePath(ONE_PROFILE_FILE_PATH);
        GenieResponse<ProfileImportResponse> genieResponse = activity.importProfile(profileImportRequest.build());
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals(0, genieResponse.getResult().getFailed());
        Assert.assertEquals(1, genieResponse.getResult().getImported());


    }

    @Test
    public void _84ShouldReturnInvalidFileIfFileDoesntExist() {

        GenieServiceDBHelper.clearProfileTable();
        GenieServiceDBHelper.clearUserTableDBEntry();


        ProfileImportRequest.Builder profileImportRequest = new ProfileImportRequest.Builder().fromFilePath(DESTINATION + File.separator + "XYZ" + File.separator + "OneProfile.epar");
        GenieResponse<ProfileImportResponse> genieResponse = activity.importProfile(profileImportRequest.build());
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals(ServiceConstants.ErrorCode.INVALID_FILE, genieResponse.getError());
        Assert.assertEquals("Profile import failed, file doesn't exists", genieResponse.getErrorMessages().get(0));

    }

    @Test
    public void _85ShouldReturnUnsupportedFileExtensionErrorForInvalidFiles() {

        GenieServiceDBHelper.clearProfileTable();
        GenieServiceDBHelper.clearUserTableDBEntry();
        EcarCopyUtil.createFileFromAsset(activity, COLLECTION_ASSET_PATH, DESTINATION);
        ProfileImportRequest.Builder profileImportRequest = new ProfileImportRequest.Builder().fromFilePath(DESTINATION + File.separator + "Times_Tables_2_to_10.ecar");
        GenieResponse<ProfileImportResponse> genieResponse = activity.importProfile(profileImportRequest.build());
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals(ServiceConstants.ErrorCode.INVALID_FILE, genieResponse.getError());
        Assert.assertEquals("Profile import failed, unsupported file extension", genieResponse.getErrorMessages().get(0));

    }

    /**
     * Scenario: Export the profile to a specified filepath.
     * Given: Export the profile to a specified filepath.
     * Then: Profile gets successfully exported to the given filepath.
     */
    @Test
    public void _86ShouldExportProfileSuccessfully() {

        GenieServiceDBHelper.clearProfileTable();
        GenieServiceDBHelper.clearUserTableDBEntry();
        final Profile profile = new Profile("Happy31", "@drawable/ic_avatar2", "en");
        profile.setUid("sampleuid");
        createNewProfile(profile);
        List<String> userIds = new ArrayList<>();
        userIds.add("sampleuid");

        ProfileExportRequest.Builder exportRequest = new ProfileExportRequest.Builder().exportUsers(userIds).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<ProfileExportResponse> genieResponse = activity.exportProfile(exportRequest.build());
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals(activity.getExternalFilesDir(null).toString() + File.separator + "tmp" + File.separator + "Happy31" + ".epar", genieResponse.getResult().getExportedFilePath());
    }


    @Test
    public void _87ShouldReturnExportFailedErrorIfUserDoesntexist() {

        GenieServiceDBHelper.clearProfileTable();
        GenieServiceDBHelper.clearUserTableDBEntry();

        List<String> userIds = new ArrayList<>();

        ProfileExportRequest.Builder exportRequest = new ProfileExportRequest.Builder().exportUsers(userIds).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<ProfileExportResponse> genieResponse = activity.exportProfile(exportRequest.build());
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals(ServiceConstants.ErrorCode.EXPORT_FAILED, genieResponse.getError());
        Assert.assertEquals("There are no profile to export.", genieResponse.getErrorMessages().get(0));
    }


    @Test
    public void _91ShouldSavetoContentAccessTable() {

        ContentAccess contentAccess = new ContentAccess();
        contentAccess.setStatus(ContentAccessStatus.PLAYED.getValue());
        Map<String, Object> map = new HashMap<>();
        map.put("sectionIdentifier", CHILD_C2_ID);
        ContentLearnerState learnerState = new ContentLearnerState();
        learnerState.setLearnerState(map);
        contentAccess.setContentLearnerState(learnerState);
        contentAccess.setContentId(CHILD_C2_ID);

        activity.addContentAccess(contentAccess);

        ContentAccessModel contentAccessModel = GenieServiceDBHelper.findContentAccessInDB(CHILD_C2_ID);

        Assert.assertNotNull(contentAccessModel);
        Assert.assertEquals(CHILD_C2_ID, contentAccessModel.getIdentifier());
        Assert.assertNotNull(contentAccessModel.getLearnerStateJson());
        Assert.assertEquals(CHILD_C2_ID, (GsonUtil.fromJson(contentAccessModel.getLearnerStateJson(), Map.class).get("sectionIdentifier")).toString());
        Assert.assertEquals(ContentAccessStatus.PLAYED.getValue(), contentAccessModel.getStatus());


    }

    @Test
    public void _92ShouldUpdateContentStatusinContentStatusTable() {

        ContentAccess contentAccess = new ContentAccess();
        contentAccess.setStatus(ContentAccessStatus.NOT_PLAYED.getValue());
        Map<String, Object> map = new HashMap<>();
        map.put("sectionIdentifier", CHILD_C2_ID);
        ContentLearnerState learnerState = new ContentLearnerState();
        learnerState.setLearnerState(map);
        contentAccess.setContentLearnerState(learnerState);
        contentAccess.setContentId(CHILD_C2_ID);

        activity.addContentAccess(contentAccess);

        ContentAccessModel contentAccessModel = GenieServiceDBHelper.findContentAccessInDB(CHILD_C2_ID);

        Assert.assertNotNull(contentAccessModel);
        Assert.assertEquals(ContentAccessStatus.PLAYED.getValue(), contentAccessModel.getStatus());

    }

    @Test
    public void _93ShouldGetAllContentAccessInfo() {

        GenieServiceDBHelper.clearTable(ContentAccessEntry.TABLE_NAME);

        String uid = "sampleuid1";
        Profile profile = new Profile("profile1", "@drawable/icavatar", "en");
        profile.setUid(uid);
        activity.createUserProfile(profile);
        activity.setCurrentUser(uid);

        ContentAccess contentAccess = new ContentAccess();
        contentAccess.setStatus(ContentAccessStatus.PLAYED.getValue());
        contentAccess.setContentId(CHILD_C3_ID);
        activity.addContentAccess(contentAccess);

        contentAccess.setStatus(ContentAccessStatus.PLAYED.getValue());
        contentAccess.setContentId(CHILD_C2_ID);
        activity.addContentAccess(contentAccess);

        ContentAccessFilterCriteria criteria = new ContentAccessFilterCriteria.Builder().forContent(CHILD_C2_ID).build();
        GenieResponse<List<ContentAccess>> contentAccessList = activity.getAllContentAccess(criteria);
        Assert.assertEquals(1, contentAccessList.getResult().size());

        ContentAccessFilterCriteria criteria1 = new ContentAccessFilterCriteria.Builder().forContent(CHILD_C3_ID).build();
        GenieResponse<List<ContentAccess>> contentAccessList1 = activity.getAllContentAccess(criteria1);
        Assert.assertEquals(1, contentAccessList1.getResult().size());

        ContentAccessFilterCriteria criteria2 = new ContentAccessFilterCriteria.Builder().byUser("sampleuid1").forContent(CHILD_C2_ID).build();
        GenieResponse<List<ContentAccess>> contentAccessList2 = activity.getAllContentAccess(criteria2);
        Assert.assertEquals(1, contentAccessList2.getResult().size());

        ContentAccessFilterCriteria criteria3 = new ContentAccessFilterCriteria.Builder().byUser("sampleuid1").build();
        GenieResponse<List<ContentAccess>> contentAccessList3 = activity.getAllContentAccess(criteria3);
        Assert.assertEquals(2, contentAccessList3.getResult().size());


    }
}
