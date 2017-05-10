package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.telemetry.BaseTelemetry;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.exception.InvalidDataException;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.profile.db.model.UserSessionModel;
import org.ekstep.genieservices.telemetry.cache.TelemetryTagCache;
import org.ekstep.genieservices.telemetry.model.EventModel;

import java.util.HashMap;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryService extends BaseService {

    private static final String SERVICE_NAME = TelemetryService.class.getSimpleName();

    public TelemetryService(AppContext appContext) {
        super(appContext);
    }

    public void saveTelemetry(BaseTelemetry telemetry, IResponseHandler responseHandler) {
        saveTelemetry(telemetry.toString(),responseHandler);
    }

    public void saveTelemetry(String eventString, IResponseHandler<Void> responseHandler) {
        String errorMessage = "Not able to save event";
        HashMap params = new HashMap();
        params.put("Event", eventString);
        params.put("logLevel", "3");

        try {
            EventModel event = EventModel.build(mAppContext.getDBSession(), eventString, TelemetryTagCache.activeTags(mAppContext));

            //Stamp the event with current Sid and Uid
            UserSessionModel userSession = UserSessionModel.findUserSession(mAppContext);
            if (userSession != null) {
                UserSession currentSession = userSession.find();
                if (currentSession.isValid()) {
                    event.updateSessionDetails(currentSession.getSid(), currentSession.getUid());
                }
            }

            //Stamp the event with did
            event.updateDeviceInfo(mAppContext.getDeviceInfo().getDeviceID());

            //Stamp the event with proper timestamp
            String version = event.getVersion();
            if (version.equals("1.0")) {
                event.updateTs(DateUtil.getCurrentTimestamp());
            } else if (version.equals("2.0")) {
                event.updateEts(DateUtil.getEpochTime());
            }
            event.save();

            Logger.i(SERVICE_NAME, "Event saved successfully");
            GenieResponse response = GenieResponseBuilder.getSuccessResponse("Event Saved Successfully", Void.class);
            responseHandler.onSuccess(response);

            TelemetryLogger.logSuccess(mAppContext, response, new HashMap(), SERVICE_NAME, "saveTelemetry@TelemetryService", params);
        } catch (DbException e) {
            String logMessage = "Event save failed" + e.toString();
            GenieResponse response = GenieResponseBuilder.getErrorResponse("PROCESSING_ERROR", errorMessage, logMessage, Void.class);
            responseHandler.onError(response);
            TelemetryLogger.logFailure(mAppContext, response, SERVICE_NAME, "", "saveTelemetry@TelemetryService", params);
        } catch (InvalidDataException e) {
            String logMessage = "Event save failed" + e.toString();
            GenieResponse response = GenieResponseBuilder.getErrorResponse("PROCESSING_ERROR", errorMessage, logMessage, Void.class);
            responseHandler.onError(response);
            TelemetryLogger.logFailure(mAppContext, response, SERVICE_NAME, "", "saveTelemetry@TelemetryService", params);
        }

    }

    public void sync(IResponseHandler responseHandler){
        HashMap params = new HashMap();
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));
    }


}
