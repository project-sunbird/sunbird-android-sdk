package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.enums.UserCreatedIn;
import org.ekstep.genieservices.commons.db.contract.GroupEntry;
import org.ekstep.genieservices.commons.db.contract.GroupProfileEntry;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.db.model.UserProfilesModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/7/18.
 * shriharsh
 */
public class _13_GroupAndProfileMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 13;
    private static final int TARGET_DB_VERSION = 18;


    public _13_GroupAndProfileMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        appContext.getDBSession().execute(GroupEntry.getCreateEntry());
        appContext.getDBSession().execute(GroupProfileEntry.getCreateEntry());

        List<String> alterQueries = ProfileEntry.getAlterEntryForProfileUserFromAndValue();
        for (String query: alterQueries) {
            appContext.getDBSession().execute(query);
        }

        updateProfileTableForValueAndUserFor(appContext);
    }

    private void updateProfileTableForValueAndUserFor(AppContext appContext) {
        UserProfilesModel userProfilesModel = UserProfilesModel.find(appContext.getDBSession());
        if (userProfilesModel != null) {
            List<Profile> updatedProfileList = new ArrayList<>();
            for (Profile profile : userProfilesModel.getProfileList()) {
                if (profile.getUid().equals(profile.getHandle())) {
                    profile.setUserCreatedIn(UserCreatedIn.SERVER);
                } else {
                    profile.setUserCreatedIn(UserCreatedIn.LOCAL);
                }


                updatedProfileList.add(profile);
            }

            for (Profile profile : updatedProfileList) {
                UserProfileModel userProfileModel = UserProfileModel.build(appContext.getDBSession(), profile);
                userProfileModel.update();
            }
        }
    }
}
