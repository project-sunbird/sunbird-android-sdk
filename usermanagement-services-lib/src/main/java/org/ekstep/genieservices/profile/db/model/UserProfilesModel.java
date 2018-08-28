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

    private UserProfilesModel(IDBSession dbSession, String filterCondition) {
        this.mDBSession = dbSession;
        this.mFilterCondition = filterCondition;
    }

    public static UserProfilesModel find(IDBSession dbSession) {
        UserProfilesModel userProfilesModel = new UserProfilesModel(dbSession, "");
        dbSession.read(userProfilesModel);
        if (userProfilesModel.getProfileList() == null) {
            return null;
        } else {
            return userProfilesModel;
        }
    }

    public static UserProfilesModel find(IDBSession dbSession, String filterCondition) {
        UserProfilesModel userProfilesModel = new UserProfilesModel(dbSession, filterCondition);
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
