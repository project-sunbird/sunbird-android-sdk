package org.ekstep.genieresolvers.telemetry;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.db.contract.TelemetryEntry;

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
    protected String execute() {
        Gson gson = new Gson();
        ContentValues event = getContentValues();
        event.put(TelemetryEntry.COLUMN_NAME_EVENT, eventString);
        Uri response = contentResolver.insert(getUri(), event);
        if (response == null) {
            String errorMessage = "Not able to send event";
            String logMessage = "Empty response(URI) when sending telemetry event";
            GenieResponse processing_error = GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR, errorMessage, logMessage);
            return gson.toJson(processing_error);

        }
        GenieResponse successResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.ProviderResolver.SUCCESSFUL);
        return gson.toJson(successResponse);
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
