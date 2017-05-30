package org.ekstep.genieresolvers.telemetry;

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

public class TelemetryEventTask extends BaseTask {
    private String appQualifier;
    private String eventString;

    public TelemetryEventTask(Context context, String appQualifier, String eventString) {
        super(context);
        this.appQualifier = appQualifier;
        this.eventString = eventString;
    }

    @Override
    protected String getLogTag() {
        return TelemetryEventTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse execute() {
        ContentValues event = getContentValues();
        event.put(Constants.EVENT, eventString);
        Uri response = contentResolver.insert(getUri(), event);
        if (response == null) {
            String errorMessage = "Not able to send event";
            String logMessage = "Empty response(URI) when sending telemetry event";
            return getErrorResponse(Constants.PROCESSING_ERROR, errorMessage, logMessage);
        }

        return getSuccessResponse(Constants.SUCCESSFUL);
    }

    @Override
    protected String getErrorMessage() {
        return "event not sent";
    }

    protected ContentValues getContentValues() {
        return new ContentValues();
    }

    private Uri getUri() {
        String authority = String.format("content://%s.telemetry/event", appQualifier);
        return Uri.parse(authority);
    }
}
