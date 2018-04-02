package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.EndorseOrAddSkillRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileVisibilityRequest;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.UpdateUserInfoRequest;
import org.ekstep.genieservices.commons.bean.UploadFileRequest;
import org.ekstep.genieservices.commons.bean.UserSearchCriteria;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.network.EndorseOrAddSkillAPI;
import org.ekstep.genieservices.profile.network.FileUploadAPI;
import org.ekstep.genieservices.profile.network.ProfileSkillsAPI;
import org.ekstep.genieservices.profile.network.ProfileVisibilityAPI;
import org.ekstep.genieservices.profile.network.SearchUserAPI;
import org.ekstep.genieservices.profile.network.TenantInfoAPI;
import org.ekstep.genieservices.profile.network.UpdateUserInfoAPI;
import org.ekstep.genieservices.profile.network.UserProfileDetailsAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 5/3/18.
 *
 * @author anil
 */
public class UserProfileHandler {


    private static Map<String, String> getCustomHeaders(Session authSession) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Authenticated-User-Token", authSession.getAccessToken());
        return headers;
    }

    public static GenieResponse fetchUserProfileDetailsFromServer(AppContext appContext, Session sessionData,
                                                                  String userId, String fields) {
        UserProfileDetailsAPI userProfileDetailsAPI = new UserProfileDetailsAPI(appContext,
                getCustomHeaders(sessionData), userId, fields);
        return userProfileDetailsAPI.get();
    }

    public static void refreshUserProfileDetailsFromServer(final AppContext appContext, final Session sessionData,
                                                           final String userId, final String fields,
                                                           final NoSqlModel userProfileInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse userProfileDetailsAPIResponse = fetchUserProfileDetailsFromServer(appContext,
                        sessionData, userId, fields);
                if (userProfileDetailsAPIResponse.getStatus()) {
                    String jsonResponse = userProfileDetailsAPIResponse.getResult().toString();
                    if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                        userProfileInDB.setValue(jsonResponse);
                        userProfileInDB.update();
                    }
                }
            }
        }).start();
    }

    public static GenieResponse fetchTenantInfoFromServer(AppContext appContext, Session sessionData, String slug) {
        TenantInfoAPI tenantInfoAPI = new TenantInfoAPI(appContext, getCustomHeaders(sessionData), slug);
        return tenantInfoAPI.get();
    }

    public static void refreshTenantInfoFromServer(final AppContext appContext, final Session sessionData,
                                                   final String slug, final NoSqlModel tenantInfoInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse tenantInfoAPIResponse = fetchTenantInfoFromServer(appContext, sessionData, slug);
                if (tenantInfoAPIResponse.getStatus()) {
                    String jsonResponse = tenantInfoAPIResponse.getResult().toString();
                    if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                        tenantInfoInDB.setValue(jsonResponse);
                        tenantInfoInDB.update();
                    }
                }
            }
        }).start();
    }

    public static GenieResponse setProfileVisibilityDetailsInServer(AppContext appContext, Session sessionData,
                                                                    ProfileVisibilityRequest profileVisibilityRequest) {
        ProfileVisibilityAPI profileVisibilityAPI = new ProfileVisibilityAPI(appContext, getCustomHeaders(sessionData),
                getProfileVisibilityRequest(profileVisibilityRequest));
        return profileVisibilityAPI.post();
    }

    private static Map<String, Object> getProfileVisibilityRequest(ProfileVisibilityRequest profileVisibilityRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", profileVisibilityRequest.getUserId());

        if (profileVisibilityRequest.getPrivateFields() != null) {
            requestMap.put("private", profileVisibilityRequest.getPrivateFields());
        }

        if (profileVisibilityRequest.getPublicFields() != null) {
            requestMap.put("public", profileVisibilityRequest.getPublicFields());
        }

        return requestMap;
    }

    public static GenieResponse searchUser(AppContext appContext, Session sessionData, UserSearchCriteria userSearchCriteria) {
        SearchUserAPI searchUserAPI = new SearchUserAPI(appContext, getCustomHeaders(sessionData),
                getSearchUserParameters(userSearchCriteria));
        return searchUserAPI.post();
    }

    private static Map<String, Object> getSearchUserParameters(UserSearchCriteria userSearchCriteria) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("query", userSearchCriteria.getQuery());
        requestMap.put("offset", userSearchCriteria.getOffset());
        requestMap.put("limit", userSearchCriteria.getLimit());
        if (userSearchCriteria.getIdentifiers() != null) {
            Map<String, Object> identifiersMap = new HashMap<>();
            identifiersMap.put("identifier", userSearchCriteria.getIdentifiers());
            requestMap.put("filters", identifiersMap);
        }
        return requestMap;
    }

    public static GenieResponse fetchProfileSkillsFromServer(AppContext appContext, Session sessionData) {
        ProfileSkillsAPI profileSkillsAPI = new ProfileSkillsAPI(appContext, getCustomHeaders(sessionData));
        return profileSkillsAPI.get();
    }

    public static void refreshProfileSkillsFromServer(final AppContext appContext, final Session sessionData,
                                                      final NoSqlModel profileSkillsInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse profileSkillsAPIResponse = fetchProfileSkillsFromServer(appContext, sessionData);
                if (profileSkillsAPIResponse.getStatus()) {
                    String jsonResponse = profileSkillsAPIResponse.getResult().toString();
                    if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                        profileSkillsInDB.setValue(jsonResponse);
                        profileSkillsInDB.update();
                    }
                }
            }
        }).start();
    }

    public static GenieResponse endorseOrAddSkillsInServer(AppContext appContext, Session sessionData,
                                                           EndorseOrAddSkillRequest endorseOrAddSkillRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("endorsedUserId", endorseOrAddSkillRequest.getUserId());
        requestMap.put("skillName", endorseOrAddSkillRequest.getSkills());

        EndorseOrAddSkillAPI endorseOrAddSkillAPI = new EndorseOrAddSkillAPI(appContext,
                getCustomHeaders(sessionData), requestMap);
        return endorseOrAddSkillAPI.post();
    }

    public static GenieResponse uploadFile(AppContext appContext, Session sessionData, UploadFileRequest uploadFileRequest) {
        FileUploadAPI fileUploadAPI = new FileUploadAPI(appContext, getCustomHeaders(sessionData),
                getFileUploadParameters(uploadFileRequest));
        return fileUploadAPI.post();
    }

    private static Map<String, String> getFileUploadParameters(UploadFileRequest uploadFileRequest) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("file", uploadFileRequest.getFilePath());
        requestMap.put("container", uploadFileRequest.getUserId());

        return requestMap;
    }

    public static GenieResponse updateUserInfoInServer(AppContext appContext, Session sessionData, UpdateUserInfoRequest updateUserInfoRequest) {
        UpdateUserInfoAPI updateUserInfoAPI = new UpdateUserInfoAPI(appContext, getCustomHeaders(sessionData),
                getUpdateUserInfoRequestMap(updateUserInfoRequest));
        return updateUserInfoAPI.patch();
    }

    private static Map<String, Object> getUpdateUserInfoRequestMap(UpdateUserInfoRequest updateUserInfoRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", updateUserInfoRequest.getUserId());
        requestMap.put("firstName", updateUserInfoRequest.getFirstName());
        requestMap.put("lastName", updateUserInfoRequest.getLastName());
        requestMap.put("language", updateUserInfoRequest.getLanguage());
        requestMap.put("phone", updateUserInfoRequest.getPhone());
        requestMap.put("profileSummary", updateUserInfoRequest.getProfileSummary());
        requestMap.put("subject", updateUserInfoRequest.getSubject());
        requestMap.put("gender", updateUserInfoRequest.getGender());
        requestMap.put("dob", updateUserInfoRequest.getDob());
        requestMap.put("grade", updateUserInfoRequest.getGrade());
        requestMap.put("location", updateUserInfoRequest.getLocation());
        requestMap.put("webPages", updateUserInfoRequest.getWebPages());
        requestMap.put("education", updateUserInfoRequest.getEducation());
        requestMap.put("jobProfile", updateUserInfoRequest.getJobProfile());
        requestMap.put("address", updateUserInfoRequest.getAddress());
        return requestMap;
    }

}
