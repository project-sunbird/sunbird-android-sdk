package org.ekstep.genieresolvers.user;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.utils.GsonUtil;

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
    protected String execute() {
        Gson gson = new Gson();
        ContentValues profileValues = getContentValues();
        profileValues.put(ServiceConstants.PROFILE, GsonUtil.toJson(profile));
        Uri response = contentResolver.insert(getUri(), profileValues);
        if (response == null) {
            String logMessage = "Empty response(URI) when creating Profile";
            GenieResponse processing_error = GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR, getErrorMessage(), logMessage);
            return gson.toJson(processing_error);

        }
        GenieResponse successResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.ProviderResolver.SUCCESSFUL);
        return gson.toJson(successResponse);
    }

    @Override
    protected String getErrorMessage() {
        return "Not able to create profile";
    }

    protected ContentValues getContentValues() {
        return new ContentValues();
    }

    private Uri getUri() {
        String authority = String.format("content://%s.profiles", appQualifier);
        return Uri.parse(authority);
    }
}
