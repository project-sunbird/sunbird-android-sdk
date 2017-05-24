package org.ekstep.genieresolvers.user;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class GetCurrentUserTask extends BaseTask {

    private String appQualifier;

    public GetCurrentUserTask(Context context, String appQualifier) {
        super(context);
        this.appQualifier = appQualifier;
    }

    @Override
    protected String getLogTag() {
        return GetCurrentUserTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse execute() {
        Cursor cursor = contentResolver.query(getUri(), null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR, getErrorMessage(), "No Response for current user!");
        }

        GenieResponse successResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.ProviderResolver.SUCCESSFUL);
        // TODO: 23/5/17 Need to send response with the result here and need to check how do we have to send the response
//        response.setResult(getContent);
        return successResponse;
    }

    @Override
    protected String getErrorMessage() {
        return "Could not find any current user!";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.profiles/currentUser", appQualifier);
        return Uri.parse(authority);
    }

}
