package org.ekstep.genieresolvers.language;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;

import java.util.Map;

/**
 * Created on 24/5/17.
 * shriharsh
 */

public class LanguageService extends BaseService {
    private String appQualifier;
    private Context context;

    public LanguageService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    public void getTraversalRule(String languageId, IResponseHandler<Map> responseHandler) {
        GetTraversalRuleTask getTraversalRuleTask = new GetTraversalRuleTask(context, appQualifier, languageId);
        createAndExecuteTask(responseHandler, getTraversalRuleTask);
    }

    public void getLanguageSearch(String searchRequest, IResponseHandler<Map> responseHandler) {
        GetLanguageSearchTask getLanguageSearchTask = new GetLanguageSearchTask(context, appQualifier, searchRequest);
        createAndExecuteTask(responseHandler, getLanguageSearchTask);
    }

    public void getAllLanguages(IResponseHandler<Map> responseHandler) {
        GetAllLanguagesTask getAllLanguagesTask = new GetAllLanguagesTask(context, appQualifier);
        createAndExecuteTask(responseHandler, getAllLanguagesTask);
    }

}
