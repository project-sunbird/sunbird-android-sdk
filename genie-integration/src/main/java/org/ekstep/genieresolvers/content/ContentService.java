package org.ekstep.genieresolvers.content;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;

import java.util.Map;

/**
 * This is the {@link ContentService} class with all the required APIs for performing content related operations.
 */
public class ContentService extends BaseService {
    private String appQualifier;
    private Context context;

    public ContentService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    /**
     * This api is used to get the content details about a specific content.
     * <p>
     * <p>
     * On successful fetching the content details, the response will return status as TRUE and with Content in the result.
     * <p>
     * <p>
     * On failing to fetch the content details, the response will return status as FALSE with the following error code
     * <p>NO_DATA_FOUND
     *
     * @param contentId
     * @param responseHandler
     */
    public void getContent(String contentId, IResponseHandler<Map> responseHandler) {
        GetContentTask getContentTask = new GetContentTask(context, appQualifier, contentId);
        createAndExecuteTask(responseHandler, getContentTask);
    }

    /**
     * This api is used to get the related contents as similar to the identifier passed.
     * <p>
     * <p>
     * On successful finding the contents, matching with the language preferred, the response will return status as TRUE
     * <p>
     * <p>
     * On failing to find the contents, the response with return status as FALSE with one of the following errors
     * <p>CONNECTION_ERROR
     * <p>SERVER_ERROR
     * <p>NETWORK_ERROR
     *
     * @param hierarchyData
     * @param currentContentIdentifier
     * @param userId
     * @param responseHandler
     */
    public void getRelatedContent(Map hierarchyData, String currentContentIdentifier, String userId, IResponseHandler<Map> responseHandler) {
        GetRelatedContentTask getRelatedContentTask = new GetRelatedContentTask(context, appQualifier, hierarchyData, currentContentIdentifier, userId);
        createAndExecuteTask(responseHandler, getRelatedContentTask);
    }

    /**
     *  This api is used to send the feedback about content.
     *
     * @param feedbackString
     * @param responseHandler
     */
    public void sendFeedback(String feedbackString, IResponseHandler<Map> responseHandler) {
        SendFeedbackEventTask sendFeedbackEventTask = new SendFeedbackEventTask(context, appQualifier, feedbackString);
        createAndExecuteTask(responseHandler, sendFeedbackEventTask);
    }

}
