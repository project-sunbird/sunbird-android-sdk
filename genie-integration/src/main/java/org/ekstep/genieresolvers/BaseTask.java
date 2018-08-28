package org.ekstep.genieresolvers;

import android.content.ContentResolver;
import android.content.Context;

import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Collections;
import java.util.Map;

public abstract class BaseTask {
    protected ContentResolver contentResolver;
    private Context context;

    public BaseTask(Context context) {
        this.context = context;
    }

    /**
     * It returns the error {@link GenieResponse} object.
     *
     * @param error
     * @param errorMessage
     * @param logTag
     * @return
     */
    public static GenieResponse<Map> getErrorResponse(String error, String errorMessage, String logTag) {
        GenieResponse<Map> response = new GenieResponse<>();
        response.setStatus(false);
        response.setErrorMessages(Collections.singletonList(errorMessage));
        response.setError(error);
        return response;
    }

    /**
     * It returns the success {@link GenieResponse} object.
     *
     * @param message
     * @return
     */
    public static GenieResponse<Map> getSuccessResponse(String message) {
        GenieResponse<Map> response = new GenieResponse<>();
        response.setStatus(true);
        response.setMessage(message);
        return response;
    }

    protected abstract String getLogTag();

    protected abstract GenieResponse<Map> execute();

    protected abstract String getErrorMessage();

    protected GenieResponse<Map> perform() {
        contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            String errorMessage = "Not able to resolve content provider, " + getErrorMessage();
            return getErrorResponse(Constants.PROCESSING_ERROR, errorMessage, BaseTask.class.getSimpleName());
        }
        return execute();
    }
}
