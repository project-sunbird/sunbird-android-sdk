package org.ekstep.genieproviders.user;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.ekstep.genieproviders.BaseContentProvider;
import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.List;

/**
 * Created on 19/5/17.
 * shriharsh
 */

public abstract class AbstractUserProvider extends BaseContentProvider {
    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        List<CurrentUserUriHandler> handlers = ProfileUriHandlerFactory.uriHandlers(getCompletePath(), getContext(), selection, selectionArgs, getService());
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
        Profile profile = GsonUtil.fromJson(values.getAsString(ServiceConstants.PROFILE), Profile.class);
        getService().getUserProfileService().createUserProfile(profile);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        if (selectionArgs[0].isEmpty()) {
            return 0;
        } else {
            GenieResponse response = getService().getUserProfileService().deleteUser(selectionArgs[0]);
            if (response.getStatus()) {
                return 1;
            }

            return 0;
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values != null) {
            Profile profile = GsonUtil.fromJson(values.getAsString(ServiceConstants.PROFILE), Profile.class);
            GenieResponse response = getService().getUserProfileService().updateUserProfile(profile);
            if (response.getStatus()) {
                return 1;
            }

            return 0;
        } else {
            return 0;
        }
    }

    private String getCompletePath() {
        String CONTENT_PATH = "profiles";
        return String.format("%s.%s", getPackageName(), CONTENT_PATH);
    }

    public abstract String getPackageName();

    @Override
    public String getPackage() {
        return getPackageName();
    }
}
