package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.db.model.UserProfilesModel;

import java.util.ArrayList;
import java.util.List;

public class _02_CreatedAtForProfileMigration extends Migration {

    private static final int MIGRATION_NUMBER = 2;
    private static final int TARGET_DB_VERSION = 9;

    public _02_CreatedAtForProfileMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        appContext.getDBSession().execute(ProfileEntry.getAlterEntryForCreateAt());

        updateCreatedAtToCurrentTime(appContext);
    }

    private void updateCreatedAtToCurrentTime(AppContext appContext) {
        UserProfilesModel userProfilesModel = UserProfilesModel.find(appContext.getDBSession());
        if (userProfilesModel != null) {
            List<Profile> updatedProfileList = new ArrayList<>();
            for (Profile profile : userProfilesModel.getProfileList()) {
                profile.setCreatedAt(DateUtil.now());
                updatedProfileList.add(profile);
            }

            for (Profile profile : updatedProfileList) {
                UserProfileModel userProfileModel = UserProfileModel.build(appContext.getDBSession(), profile);
                // TODO: 6/14/2017 - Need to varify
//               profile.enableMigration_02();
                userProfileModel.update();
            }
        }
    }
}
