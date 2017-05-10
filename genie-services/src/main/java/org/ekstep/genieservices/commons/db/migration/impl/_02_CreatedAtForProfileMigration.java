package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.profile.db.contract.ProfileEntry;

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
        // TODO: 4/19/2017 Anil - Uncomment after model is ready
//        ProfileDTOs profileDTOs = getProfileDTOs(appContext);
//        profileDTOs.updateWithCurrentTime();
//        updateProfiles(appContext, profileDTOs);
    }

//    @NonNull
//    private ProfileDTOs getProfileDTOs(AppContext appContext) {
//        ProfileDTOs profileDTOs = new ProfileDTOs();
//        Reader profileReader = new Reader(profileDTOs);
//        profileReader.perform(db);
//        return profileDTOs;
//    }

//    private void updateProfiles(AppContext appContext, ProfileDTOs profileDTOs) {
//        List<ProfileDTO> profiles = profileDTOs.getProfiles();
//        for (ProfileDTO profile : profiles) {
//            profile.enableMigration_02();
//
//            Updater profileUpdater = new Updater(profile);
//            profileUpdater.perform(db);
//        }
//    }
}
