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
import org.ekstep.genieservices.commons.bean.ReceivedAnnouncementRequest;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.UserInboxRequest;
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
    public GenieResponse<Announcement> getAnnouncementById(AnnouncementRequest announcementRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(announcementRequest));
        String methodName = "getAnnouncementById@AnnouncementServiceImpl";

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
    public GenieResponse<Void> userInbox(UserInboxRequest userInboxRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(userInboxRequest));
        String methodName = "userInbox@AnnouncementServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        UserInboxAPI userInboxAPI = new UserInboxAPI(mAppContext, getCustomHeaders(authSession.getSessionData()),
                announcementHandler.getUserInboxRequestMap(userInboxRequest));
        GenieResponse genieResponse = userInboxAPI.post();

        if (genieResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(genieResponse.getError(), genieResponse.getMessage(), TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
        }
        return response;
    }

    @Override
    public GenieResponse<Void> receivedAnnouncement(ReceivedAnnouncementRequest receivedAnnouncementRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(receivedAnnouncementRequest));
        String methodName = "receivedAnnouncement@AnnouncementServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        ReceivedAPI receivedAPI = new ReceivedAPI(mAppContext, getCustomHeaders(authSession.getSessionData()),
                announcementHandler.getReceivedAnnouncementRequestMap(receivedAnnouncementRequest));
        GenieResponse genieResponse = receivedAPI.post();

        if (genieResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(genieResponse.getError(), genieResponse.getMessage(), TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
        }
        return response;
    }

    @Override
    public GenieResponse<Void> readAnnouncement(ReceivedAnnouncementRequest receivedAnnouncementRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(receivedAnnouncementRequest));
        String methodName = "readAnnouncement@AnnouncementServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        ReadAPI readAPI = new ReadAPI(mAppContext, getCustomHeaders(authSession.getSessionData()),
                announcementHandler.getReceivedAnnouncementRequestMap(receivedAnnouncementRequest));
        GenieResponse genieResponse = readAPI.post();

        if (genieResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(genieResponse.getError(), genieResponse.getMessage(), TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
        }
        return response;
    }

}