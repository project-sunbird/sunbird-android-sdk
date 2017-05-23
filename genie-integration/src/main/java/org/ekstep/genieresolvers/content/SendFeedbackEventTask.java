package org.ekstep.genieresolvers.content;

import android.content.ContentValues;
import android.content.Context;
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

public class SendFeedbackEventTask extends BaseTask {
    private String appQualifier;
    private String feedbackString;

    public SendFeedbackEventTask(Context context, String appQualifier, String feedbackString) {
        super(context);
        this.appQualifier = appQualifier;
        this.feedbackString = feedbackString;
    }

    @Override
    protected String getLogTag() {
        return SendFeedbackEventTask.class.getSimpleName();
    }

    @Override
    protected String execute() {
        ContentValues event = getContentValues();
        event.put("event", feedbackString);
        Uri response = contentResolver.insert(getUri(), event);
        if (response == null) {
            String errorMessage = "Not able to send event";
            String logMessage = "Empty response(URI) when sending feedback event";
            GenieResponse processing_error = GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR, errorMessage, logMessage);
            return GsonUtil.toJson(processing_error);

        }
        GenieResponse successResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.ProviderResolver.SUCCESSFUL);
        return GsonUtil.toJson(successResponse);
    }

    @Override
    protected String getErrorMessage() {
        return "feedback not sent";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.content/feedback", appQualifier);
        return Uri.parse(authority);
    }

    protected ContentValues getContentValues() {
        return new ContentValues();
    }

}
