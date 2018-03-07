package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileVisibilityRequest;
import org.ekstep.genieservices.commons.bean.SearchUserRequest;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.UploadFileRequest;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.network.EndorseOrAddSkillAPI;
import org.ekstep.genieservices.profile.network.ProfileSkillsAPI;
import org.ekstep.genieservices.profile.network.FileUploadAPI;
import org.ekstep.genieservices.profile.network.ProfileVisibilityAPI;
import org.ekstep.genieservices.profile.network.SearchUserAPI;
import org.ekstep.genieservices.profile.network.TenantInfoAPI;
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
        UserProfileDetailsAPI userProfileDetailsAPI = new UserProfileDetailsAPI(appContext, getCustomHeaders(sessionData), userId, fields);
        return userProfileDetailsAPI.get();
    }

    public static void refreshUserProfileDetailsFromServer(final AppContext appContext, final Session sessionData,
                                                           final String userId, final String fields, final NoSqlModel userProfileInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse userProfileDetailsAPIResponse = fetchUserProfileDetailsFromServer(appContext, sessionData, userId, fields);
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

    public static GenieResponse setProfileVisibilityDetailsInServer(AppContext appContext, Session sessionData, ProfileVisibilityRequest profileVisibilityRequest) {
        ProfileVisibilityAPI profileVisibilityAPI = new ProfileVisibilityAPI(appContext, getCustomHeaders(sessionData), getProfileVisibilityRequest(profileVisibilityRequest));
        return profileVisibilityAPI.post();
    }

    private static Map<String, Object> getProfileVisibilityRequest(ProfileVisibilityRequest
                                                                           profileVisibilityRequest) {
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

    public static GenieResponse searchUser(AppContext appContext, Session sessionData, SearchUserRequest searchUserRequest) {
        SearchUserAPI searchUserAPI = new SearchUserAPI(appContext, getCustomHeaders(sessionData), getSearchUserParameters(searchUserRequest));
        return searchUserAPI.post();
    }

    private static Map<String, Object> getSearchUserParameters(SearchUserRequest searchUserRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("query", searchUserRequest.getQuery());
        requestMap.put("offset", searchUserRequest.getOffset());
        requestMap.put("limit", searchUserRequest.getLimit());

        return requestMap;
    }

    public static GenieResponse fetchProfileSkillsFromServer(AppContext appContext) {
        ProfileSkillsAPI profileSkillsAPI = new ProfileSkillsAPI(appContext);
        return profileSkillsAPI.get();
    }

    public static void refreshProfileSkillsFromServer(final AppContext appContext, final NoSqlModel profileSkillsInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse profileSkillsAPIResponse = fetchProfileSkillsFromServer(appContext);
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

    public static GenieResponse endorseOrAddSkillsFromServer(AppContext appContext, String userId, String[] skills) {
        EndorseOrAddSkillAPI endorseOrAddSkillAPI = new EndorseOrAddSkillAPI(appContext, userId, skills);
        return endorseOrAddSkillAPI.post();
    }

    public static GenieResponse uploadFile(AppContext appContext, Session sessionData, UploadFileRequest uploadFileRequest) {
        FileUploadAPI fileUploadAPI = new FileUploadAPI(appContext, getCustomHeaders(sessionData), getFileUploadParameters(uploadFileRequest));
        return fileUploadAPI.post();
    }

    private static Map<String, Object> getFileUploadParameters(UploadFileRequest uploadFileRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("file", uploadFileRequest.getFilePath());
        requestMap.put("container", uploadFileRequest.getUserId());

        return requestMap;
    }


}
