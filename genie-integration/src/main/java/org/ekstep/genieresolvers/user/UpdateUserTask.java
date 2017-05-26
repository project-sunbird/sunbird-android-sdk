package org.ekstep.genieresolvers.user;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class UpdateUserTask extends BaseTask {
    private String appQualifier;
    private Profile profile;

    public UpdateUserTask(Context context, String appQualifier, Profile profile) {
        super(context);
        this.appQualifier = appQualifier;
        this.profile = profile;
    }

    @Override
    protected String getLogTag() {
        return UpdateUserTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse execute() {
        ContentValues profileValues = getContentValues();
        profileValues.put(Constants.PROFILE, GsonUtil.toJson(profile));
        int response = contentResolver.update(getUri(), profileValues, null, null);

        if (response != 1) {
            String logMessage = "Could not update the user!";
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), logMessage);

        }

        GenieResponse successResponse = getSuccessResponse(Constants.SUCCESSFUL);
        return successResponse;
    }

    @Override
    protected String getErrorMessage() {
        return null;
    }


    protected ContentValues getContentValues() {
        return new ContentValues();
    }

    private Uri getUri() {
        String authority = String.format("content://%s.profiles", appQualifier);
        return Uri.parse(authority);
    }
}
