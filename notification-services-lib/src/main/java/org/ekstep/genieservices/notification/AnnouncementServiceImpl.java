package org.ekstep.genieservices.notification;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAnnouncementService;
import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.Announcement;
import org.ekstep.genieservices.commons.bean.AnnouncementRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.UpdateAnnouncementStateRequest;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.AnnouncementList;
import org.ekstep.genieservices.commons.bean.AnnouncementListRequest;
import org.ekstep.genieservices.commons.bean.enums.AnnouncementStatus;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.notification.network.GetAnnouncementAPI;
import org.ekstep.genieservices.notification.network.ReadAPI;
import org.ekstep.genieservices.notification.network.ReceivedAPI;
import org.ekstep.genieservices.notification.network.UserInboxAPI;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 3/22/2018.
 *
 * @author IndrajaMachani
 */

public class AnnouncementServiceImpl extends BaseService implements IAnnouncementService {

    private static final String TAG = AnnouncementServiceImpl.class.getSimpleName();

    private IAuthSession<Session> authSession;

    public AnnouncementServiceImpl(AppContext appContext, IAuthSession<Session> authSession) {
        super(appContext);

        this.authSession = authSession;
    }

    private Map<String, String> getCustomHeaders(Session authSession) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Authenticated-User-Token", authSession.getAccessToken());
        return headers;
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
    public GenieResponse<Announcement> getAnnouncementDetails(AnnouncementRequest announcementRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(announcementRequest));
        String methodName = "getAnnouncementDetails@AnnouncementServiceImpl";

        GenieResponse<Announcement> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GetAnnouncementAPI getAnnouncementAPI = new GetAnnouncementAPI(mAppContext, getCustomHeaders(authSession.getSessionData()),
                announcementRequest.getAnnouncementId());
        GenieResponse genieResponse = getAnnouncementAPI.get();

        if (genieResponse.getStatus()) {
            LinkedTreeMap map = GsonUtil.fromJson(genieResponse.getResult().toString(), LinkedTreeMap.class);
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");
            Announcement announcement = GsonUtil.fromJson(GsonUtil.toJson(result.get("announcement")),
                    Announcement.class);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(announcement);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(genieResponse.getError(), genieResponse.getMessage(), TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
        }

        return response;
    }

    @Override
    public GenieResponse<AnnouncementList> getAnnouncementList(AnnouncementListRequest announcementListRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(announcementListRequest));
        String methodName = "getAnnouncementList@AnnouncementServiceImpl";

        GenieResponse<AnnouncementList> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        UserInboxAPI userInboxAPI = new UserInboxAPI(mAppContext, getCustomHeaders(authSession.getSessionData()),
                announcementHandler.getUserInboxRequestMap(announcementListRequest));
        GenieResponse genieResponse = userInboxAPI.post();

        if (genieResponse.getStatus()) {
            LinkedTreeMap map = GsonUtil.fromJson(genieResponse.getResult().toString(), LinkedTreeMap.class);
            AnnouncementList userInboxAnnouncements = GsonUtil.fromJson(GsonUtil.toJson(map.get("result")),
                    AnnouncementList.class);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(userInboxAnnouncements);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(genieResponse.getError(), genieResponse.getMessage(), TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
        }
        return response;
    }

    @Override
    public GenieResponse<Void> updateAnnouncementState(UpdateAnnouncementStateRequest updateAnnouncementStateRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(updateAnnouncementStateRequest));
        String methodName = "updateAnnouncementState@AnnouncementServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        AnnouncementStatus status = updateAnnouncementStateRequest.getAnnouncementStatus();
        GenieResponse genieResponse = null;
        if (status.getValue().equalsIgnoreCase(AnnouncementStatus.RECEIVED.getValue())) {
            ReceivedAPI receivedAPI = new ReceivedAPI(mAppContext, getCustomHeaders(authSession.getSessionData()),
                    announcementHandler.getUpdateAnnouncementRequestMap(updateAnnouncementStateRequest));
            genieResponse = receivedAPI.post();
        } else if (status.getValue().equalsIgnoreCase(AnnouncementStatus.READ.getValue())) {
            ReadAPI readAPI = new ReadAPI(mAppContext, getCustomHeaders(authSession.getSessionData()),
                    announcementHandler.getUpdateAnnouncementRequestMap(updateAnnouncementStateRequest));
            genieResponse = readAPI.post();
        }

        if (genieResponse != null && genieResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.UPDATE_ANNOUNCEMENT_FAILED,
                    ServiceConstants.ErrorMessage.UNABLE_TO_UPDATE_ANNOUNCEMENT + "-" + status.getValue(), TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params,
                    ServiceConstants.ErrorMessage.UNABLE_TO_UPDATE_ANNOUNCEMENT + "-" + status.getValue());
        }
        return response;
    }

}