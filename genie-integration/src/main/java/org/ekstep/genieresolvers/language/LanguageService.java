package org.ekstep.genieresolvers.language;

import android.content.Context;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.TaskHandler;
import org.ekstep.genieservices.commons.IResponseHandler;

/**
 * Created on 24/5/17.
 * shriharsh
 */

public class LanguageService {
    private String appQualifier;
    private Context context;

    public LanguageService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    public void getTraversalRule(String languageId, IResponseHandler responseHandler) {
        GetTraversalRuleTask getTraversalRuleTask = new GetTraversalRuleTask(context, appQualifier, languageId);

        createAndExecuteTask(responseHandler, getTraversalRuleTask);
    }

    public void getAllLanguages(IResponseHandler responseHandler) {
        GetAllLanguagesTask getAllLanguagesTask = new GetAllLanguagesTask(context, appQualifier);

        createAndExecuteTask(responseHandler, getAllLanguagesTask);
    }

    public void getLanguageSearch(String searchRequest, IResponseHandler responseHandler) {
        GetLanguageSearchTask getLanguageSearchTask = new GetLanguageSearchTask(context, appQualifier, searchRequest);

        createAndExecuteTask(responseHandler, getLanguageSearchTask);
    }

    private void createAndExecuteTask(IResponseHandler responseHandler, BaseTask task) {
        new TaskHandler(responseHandler).execute(task);
    }

}
