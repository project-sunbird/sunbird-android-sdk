package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.network.ProfileSkillsAPI;
import org.ekstep.genieservices.profile.network.TenantInfoAPI;
import org.ekstep.genieservices.profile.network.UserProfileDetailsAPI;

/**
 * Created on 5/3/18.
 *
 * @author anil
 */
public class UserProfileHandler {

    public static GenieResponse fetchUserProfileDetailsFromServer(AppContext appContext, String userId, String fields) {
        UserProfileDetailsAPI userProfileDetailsAPI = new UserProfileDetailsAPI(appContext, userId, fields);
        return userProfileDetailsAPI.get();
    }

    public static void refreshUserProfileDetailsFromServer(final AppContext appContext, final String userId, final String fields, final NoSqlModel userProfileInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse userProfileDetailsAPIResponse = fetchUserProfileDetailsFromServer(appContext, userId, fields);
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

    public static GenieResponse fetchTenantInfoFromServer(AppContext appContext, String slug) {
        TenantInfoAPI tenantInfoAPI = new TenantInfoAPI(appContext, slug);
        return tenantInfoAPI.get();
    }

    public static void refreshTenantInfoFromServer(final AppContext appContext, final String slug, final NoSqlModel tenantInfoInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse tenantInfoAPIResponse = fetchTenantInfoFromServer(appContext, slug);
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

    public static GenieResponse fetchProfileSkillsFromServer(AppContext appContext) {
        ProfileSkillsAPI profileSkillsAPI = new ProfileSkillsAPI(appContext);
        return profileSkillsAPI.get();
    }

    public static void refreshProfileSkillsFromServer(final AppContext appContext,final NoSqlModel profileSkillsInDB) {
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
}
