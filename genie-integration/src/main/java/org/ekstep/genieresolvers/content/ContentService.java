package org.ekstep.genieresolvers.content;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;

import java.util.Map;

/**
 * Created on 24/5/17.
 * shriharsh
 */
public class ContentService extends BaseService {
    private String appQualifier;
    private Context context;

    public ContentService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    public void getContent(String contentId, IResponseHandler<Map> responseHandler) {
        GetContentTask getContentTask = new GetContentTask(context, appQualifier, contentId);
        createAndExecuteTask(responseHandler, getContentTask);
    }

    public void getRelatedContent(Map hierarchyData, String currentContentIdentifier, String userId, IResponseHandler<Map> responseHandler) {
        GetRelatedContentTask getRelatedContentTask = new GetRelatedContentTask(context, appQualifier, hierarchyData, currentContentIdentifier, userId);
        createAndExecuteTask(responseHandler, getRelatedContentTask);
    }

    public void sendFeedback(String feedbackString, IResponseHandler<Map> responseHandler) {
        SendFeedbackEventTask sendFeedbackEventTask = new SendFeedbackEventTask(context, appQualifier, feedbackString);
        createAndExecuteTask(responseHandler, sendFeedbackEventTask);
    }

}
