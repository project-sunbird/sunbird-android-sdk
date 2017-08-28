package org.ekstep.genieresolvers.language;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;

import java.util.Map;

/**
 * This is the {@link LanguageService} with all the required APIs for performing Language related
 * operations.
 */

public class LanguageService extends BaseService {
    private String appQualifier;
    private Context context;

    public LanguageService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    /**
     * This api is used to get the traversal rule for the language
     * <p>
     * <p>
     * On successful retrieval of the traversal rule from the server, the response will be with status TRUE and the result will be set to {@link String}
     * <p>
     * <p>
     * On failing to retrieve the rule,the response with return status as FALSE with one of the following errors
     * <p>CONNECTION_ERROR
     * <p>SERVER_ERROR
     * <p>NETWORK_ERROR
     *
     * @param languageId
     * @param responseHandler
     */
    public void getTraversalRule(String languageId, IResponseHandler<Map> responseHandler) {
        GetTraversalRuleTask getTraversalRuleTask = new GetTraversalRuleTask(context, appQualifier, languageId);
        createAndExecuteTask(responseHandler, getTraversalRuleTask);
    }

    /**
     * This api is used to get the particular language if is present in the languages list supported.
     *
     * @param searchRequest
     * @param responseHandler
     */
    public void getLanguageSearch(String searchRequest, IResponseHandler<Map> responseHandler) {
        GetLanguageSearchTask getLanguageSearchTask = new GetLanguageSearchTask(context, appQualifier, searchRequest);
        createAndExecuteTask(responseHandler, getLanguageSearchTask);
    }

    /**
     * This api is used to get all the languages that are supported.
     *
     * @param responseHandler
     */
    public void getAllLanguages(IResponseHandler<Map> responseHandler) {
        GetAllLanguagesTask getAllLanguagesTask = new GetAllLanguagesTask(context, appQualifier);
        createAndExecuteTask(responseHandler, getAllLanguagesTask);
    }

}
