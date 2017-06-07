package org.ekstep.genieresolvers.content;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;

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
    protected GenieResponse execute() {
        ContentValues event = new ContentValues();
        event.put("event", feedbackString);
        Uri response = contentResolver.insert(getUri(), event);
        if (response == null) {
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), SendFeedbackEventTask.class.getSimpleName());

        }
        GenieResponse successResponse = getSuccessResponse(Constants.SUCCESSFUL);
        return successResponse;
    }

    @Override
    protected String getErrorMessage() {
        return "feedback not sent";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.content/feedback", appQualifier);
        return Uri.parse(authority);
    }

}
