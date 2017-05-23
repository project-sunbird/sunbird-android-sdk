package org.ekstep.genieresolvers;

import android.content.ContentResolver;
import android.content.Context;

import com.google.gson.Gson;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;

public abstract class BaseTask {
    protected ContentResolver contentResolver;
    private Context context;

    public BaseTask(Context context) {
        this.context = context;
    }

    protected abstract String getLogTag();

    protected abstract String execute();

    protected abstract String getErrorMessage();

    protected String perform() {
        Gson gson = new Gson();
        try {
            contentResolver = context.getContentResolver();
            if (contentResolver == null) {
                String logMessage = "Content Resolver for games not resolved";
                String errorMessage = "Not able to resolve content provider, " + getErrorMessage();
                GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR, errorMessage, logMessage);
                return gson.toJson(errorResponse);
            }
            return execute();
        } catch (IllegalArgumentException e) {
            String errorMessage = "Latest Genie is not installed";
            String logMessage = getErrorMessage() + ", because latest genie is not installed";
            GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.GENIE_SERVICE_NOT_INSTALLED, errorMessage, logMessage);
            return gson.toJson(errorResponse);
        }
    }
}
