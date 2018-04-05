package org.ekstep.genieservices.page;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.IPageService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PageAssemble;
import org.ekstep.genieservices.commons.bean.PageAssembleCriteria;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.page.network.PageAPI;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by souvikmondal on 21/3/18.
 */

public class PageServiceImpl extends BaseService implements IPageService {

    private static final String TAG = PageServiceImpl.class.getName();

    private static final String KEY_PAGE_ASSEMBLE = "pageAssemble";

    private IAuthSession<Session> authSession;

    public PageServiceImpl(AppContext appContext, IAuthSession<Session> authSession) {
        super(appContext);
        this.authSession = authSession;
    }

    private <T> GenieResponse<T> isValidAuthSession(String methodName, Map<String, Object> params) {
        if (authSession == null || authSession.getSessionData() == null) {
            GenieResponse<T> response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.AUTH_SESSION,
                    ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN, TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN);
            return response;
        }

        return null;
    }

    @Override
    public GenieResponse<PageAssemble> getPageAssemble(PageAssembleCriteria pageAssembleCriteria) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(pageAssembleCriteria));
        String methodName = "getPageAssemble@PageServiceImpl";

        GenieResponse<PageAssemble> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        String key = getKeyForDB(pageAssembleCriteria);

        NoSqlModel pageData = NoSqlModel.findByKey(mAppContext.getDBSession(), key);

        if (pageData == null) {
            Map<String, String> headers = new HashMap<>();
            headers.put("X-Authenticated-User-Token", authSession.getSessionData().getAccessToken());

            PageAPI pageAPI = new PageAPI(mAppContext, headers, pageAssembleCriteria);
            GenieResponse pageAssembleResponse = pageAPI.post();
            if (pageAssembleResponse.getStatus()) {
                String jsonResponse = pageAssembleResponse.getResult().toString();
                if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                    pageData = NoSqlModel.build(mAppContext.getDBSession(), key, jsonResponse);
                    pageData.save();
                }
            } else {
                response = GenieResponseBuilder.getErrorResponse(pageAssembleResponse.getError(),
                        pageAssembleResponse.getMessage(), TAG);

                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, pageAssembleResponse.getMessage());
                return response;
            }
        }

        LinkedTreeMap map = GsonUtil.fromJson(pageData.getValue(), LinkedTreeMap.class);
        LinkedTreeMap responseMap = (LinkedTreeMap) ((LinkedTreeMap) map.get("result")).get("response");

        PageAssemble pageAssemble = new PageAssemble();
        pageAssemble.setId((String) responseMap.get("id"));
        pageAssemble.setName((String) responseMap.get("name"));
        pageAssemble.setSections(GsonUtil.toJson(responseMap.get("sections")));

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(pageAssemble);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    private String getKeyForDB(PageAssembleCriteria pageAssembleCriteria) {
        return KEY_PAGE_ASSEMBLE + pageAssembleCriteria.getName() + this.authSession.getSessionData().getUserToken();
    }

}
