package org.ekstep.genieresolvers.content;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;

import java.util.List;
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

    public void getContents(IResponseHandler<Map> responseHandler) {
        GetContentsTask getContentsTask = new GetContentsTask(context, appQualifier);
        createAndExecuteTask(responseHandler, getContentsTask);
    }

    public void getRelatedContent(List<String> contentIdentifiers, String userId, IResponseHandler<Map> responseHandler) {
        GetRelatedContentTask getRelatedContentTask = new GetRelatedContentTask(context, appQualifier, contentIdentifiers, userId);
        createAndExecuteTask(responseHandler, getRelatedContentTask);
    }

    public void sendFeedback(String feedbackString, IResponseHandler<Map> responseHandler) {
        SendFeedbackEventTask sendFeedbackEventTask = new SendFeedbackEventTask(context, appQualifier, feedbackString);
        createAndExecuteTask(responseHandler, sendFeedbackEventTask);
    }

}
