package org.ekstep.genieproviders.user;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.ekstep.genieproviders.BaseContentProvider;
import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieproviders.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;

import java.util.List;

/**
 * Created on 19/5/17.
 * shriharsh
 */

public abstract class AbstractUserProvider extends BaseContentProvider {
    private final String TAG = AbstractUserProvider.class.getSimpleName();

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        List<IUriHandler> handlers = ProfileUriHandlerFactory.uriHandlers(getCompletePath(), getContext(), selection, selectionArgs, getService());
        for (IUriHandler handler : handlers) {
            if (handler.canProcess(uri)) {
                return handler.process();
            }
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return String.format("vnd.android.cursor.item/%s.provider.profiles", getPackageName());
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (values != null && values.getAsString(Constants.PROFILE) != null) {
            Logger.i(TAG, "Inserting profile - " + values.getAsString(Constants.PROFILE));
            Profile profile = GsonUtil.fromJson(values.getAsString(Constants.PROFILE), Profile.class);
            GenieResponse<Profile> response = getService().getUserService().createUserProfile(profile);
            if (response != null && response.getStatus() && response.getResult() != null) {
                return Uri.parse(response.getResult().getUid());
            }

            return null;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        if (selectionArgs[0].isEmpty()) {
            Logger.i(TAG, "Deleting profile - Selection Id is empty");
            return 0;
        } else {
            Logger.i(TAG, "Deleting profile - Selection Id - " + selectionArgs[0]);
            GenieResponse response = getService().getUserService().deleteUser(selectionArgs[0]);
            if (response.getStatus()) {
                return 1;
            }

            return 0;
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values != null && values.getAsString(Constants.PROFILE) != null) {
            Profile profile = GsonUtil.fromJson(values.getAsString(Constants.PROFILE), Profile.class);
            GenieResponse response = getService().getUserService().updateUserProfile(profile);
            if (response != null && response.getStatus()) {
                return 1;
            }

            return 0;
        } else {
            return 0;
        }
    }

    private String getCompletePath() {
        String USER_PROFILE_PATH = "profiles";
        return String.format("%s.%s", getPackageName(), USER_PROFILE_PATH);
    }
}
