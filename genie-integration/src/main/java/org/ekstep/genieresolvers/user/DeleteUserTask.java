package org.ekstep.genieresolvers.user;

import android.content.Context;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class DeleteUserTask extends BaseTask {
    private String appQualifier;
    private String userId;

    public DeleteUserTask(Context context, String appQualifier, String userId) {
        super(context);
        this.appQualifier = appQualifier;
        this.userId = userId;
    }

    @Override
    protected String getLogTag() {
        return DeleteUserTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse execute() {
        int response = contentResolver.delete(getUri(), null, new String[]{userId});

        if (response != 1) {
            String logMessage = "Could not delete the user!";
           return GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR, getErrorMessage(), logMessage);
        }

        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.ProviderResolver.SUCCESSFUL);
    }

    @Override
    protected String getErrorMessage() {
        return null;
    }

    private Uri getUri() {
        String authority = String.format("content://%s.profiles", appQualifier);
        return Uri.parse(authority);
    }

}
