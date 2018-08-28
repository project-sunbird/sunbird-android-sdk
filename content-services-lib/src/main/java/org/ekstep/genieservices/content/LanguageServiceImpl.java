package org.ekstep.genieservices.content;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ILanguageService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.content.network.LanguageSearchAPI;
import org.ekstep.genieservices.content.network.LanguageTraversalAPI;

/**
 * Created on 5/23/2017.
 *
 * @author anil
 */
public class LanguageServiceImpl extends BaseService implements ILanguageService {

    private static final String TAG = LanguageServiceImpl.class.getSimpleName();

    public LanguageServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<String> getLanguageTraversalRule(String languageId) {
        GenieResponse<String> response;
        LanguageTraversalAPI api = new LanguageTraversalAPI(mAppContext, languageId);
        GenieResponse apiResponse = api.get();

        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();

            LinkedTreeMap map = GsonUtil.fromJson(body, LinkedTreeMap.class);
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(GsonUtil.toJson(result));
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        return response;
    }

    @Override
    public GenieResponse<String> getLanguageSearch(String requestData) {
        GenieResponse<String> response;
        LanguageSearchAPI api = new LanguageSearchAPI(mAppContext, requestData);
        GenieResponse apiResponse = api.post();

        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();

            LinkedTreeMap map = GsonUtil.fromJson(body, LinkedTreeMap.class);
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(GsonUtil.toJson(result));
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        return response;
    }


}
