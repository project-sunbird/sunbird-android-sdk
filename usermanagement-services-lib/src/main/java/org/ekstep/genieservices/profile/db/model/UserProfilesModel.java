package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 24/5/17.
 *
 * @author swayangjit
 */
public class UserProfilesModel implements IReadable {

    private IDBSession mDBSession;
    private List<Profile> mUserProfileList;
    private String mFilterCondition;
    private boolean isLatestCreated = false;

    private UserProfilesModel(IDBSession dbSession, String filterCondition, boolean isLatestCreated) {
        this.mDBSession = dbSession;
        this.mFilterCondition = filterCondition;
        this.isLatestCreated = isLatestCreated;
    }

    public static UserProfilesModel find(IDBSession dbSession) {
        UserProfilesModel userProfilesModel = new UserProfilesModel(dbSession, "", false);
        dbSession.read(userProfilesModel);
        if (userProfilesModel.getProfileList() == null) {
            return null;
        } else {
            return userProfilesModel;
        }
    }

    public static UserProfilesModel find(IDBSession dbSession, String filterCondition, boolean isLatestCreated) {
        UserProfilesModel userProfilesModel = new UserProfilesModel(dbSession, filterCondition, isLatestCreated);
        dbSession.read(userProfilesModel);
        if (userProfilesModel.getProfileList() == null) {
            return null;
        } else {
            return userProfilesModel;
        }
    }

    public static UserProfilesModel find(IDBSession dbSession, String filterCondition) {
        UserProfilesModel userProfilesModel = new UserProfilesModel(dbSession, filterCondition, false);
        dbSession.read(userProfilesModel);
        if (userProfilesModel.getProfileList() == null) {
            return null;
        } else {
            return userProfilesModel;
        }
    }


    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            mUserProfileList = new ArrayList<>();

            do {
                UserProfileModel userProfileModel = UserProfileModel.build(mDBSession, new Profile("", "", ""));

                userProfileModel.readWithoutMoving(resultSet);

                mUserProfileList.add(userProfileModel.getProfile());
            } while (resultSet.moveToNext());
        }

        return this;
    }

    @Override
    public String getTableName() {
        return ProfileEntry.TABLE_NAME;
    }

    @Override
    public String orderBy() {

        if (isLatestCreated) {
            return String.format(Locale.US, "order by %s desc", ProfileEntry.COLUMN_NAME_CREATED_AT);
        }

        return String.format(Locale.US, "order by %s asc", ProfileEntry.COLUMN_NAME_HANDLE);
    }

    @Override
    public String filterForRead() {
        return mFilterCondition;
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "";
    }

    public List<Profile> getProfileList() {
        return mUserProfileList;
    }

}
