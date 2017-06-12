package org.ekstep.genieresolvers.user;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Map;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class CreateUserTask extends BaseTask {

    private Profile profile;
    private String appQualifier;

    public CreateUserTask(Context context, String appQualifier, Profile profile) {
        super(context);
        this.appQualifier = appQualifier;
        this.profile = profile;
    }

    @Override
    protected String getLogTag() {
        return CreateUserTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse<Map> execute() {
        ContentValues profileValues = new ContentValues();
        profileValues.put(Constants.PROFILE, GsonUtil.toJson(profile));
        Uri response = contentResolver.insert(getUri(), profileValues);
        if (response == null) {
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), CreateUserTask.class.getSimpleName());

        }
        //Send the uuid back to the caller
        String uuid = response.toString();
        GenieResponse successResponse = getSuccessResponse(Constants.SUCCESSFUL);
        successResponse.setResult(uuid);
        return successResponse;
    }

    @Override
    protected String getErrorMessage() {
        return "Unable to create profile";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.profiles", appQualifier);
        return Uri.parse(authority);
    }
}
