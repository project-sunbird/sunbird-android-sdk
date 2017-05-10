package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.BaseTelemetry;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.exception.InvalidDataException;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.telemetry.cache.TelemetryTagCache;
import org.ekstep.genieservices.telemetry.model.EventModel;

import java.util.HashMap;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryServiceImpl extends BaseService implements ITelemetryService {

    private static final String SERVICE_NAME = TelemetryServiceImpl.class.getSimpleName();
    private IUserService mUserService=null;

    public TelemetryServiceImpl(AppContext appContext, IUserService userService) {
        super(appContext);
        this.mUserService=userService;
    }

    @Override
    public GenieResponse<Void> saveTelemetry(String eventString) {
        String errorMessage = "Not able to save event";
        HashMap params = new HashMap();
        params.put("Event", eventString);
        params.put("logLevel", "3");

        try {
            EventModel event = EventModel.build(mAppContext.getDBSession(), eventString, TelemetryTagCache.activeTags(mAppContext));

            //Stamp the event with current Sid and Uid
            if(mUserService!=null){
                UserSession currentUserSession = mUserService.getCurrentUserSession().getResult();
                if (currentUserSession.isValid()) {
                    event.updateSessionDetails(currentUserSession.getSid(), currentUserSession.getUid());
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
            TelemetryLogger.logSuccess(mAppContext, response, new HashMap(), SERVICE_NAME, "saveTelemetry@TelemetryServiceImpl", params);
            return response;
        } catch (DbException e) {
            String logMessage = "Event save failed" + e.toString();
            GenieResponse response = GenieResponseBuilder.getErrorResponse("PROCESSING_ERROR", errorMessage, logMessage, Void.class);
            TelemetryLogger.logFailure(mAppContext, response, SERVICE_NAME, "", "saveTelemetry@TelemetryServiceImpl", params);
            return response;
        } catch (InvalidDataException e) {
            String logMessage = "Event save failed" + e.toString();
            GenieResponse response = GenieResponseBuilder.getErrorResponse("PROCESSING_ERROR", errorMessage, logMessage, Void.class);
            TelemetryLogger.logFailure(mAppContext, response, SERVICE_NAME, "", "saveTelemetry@TelemetryServiceImpl", params);
            return response;
        }

    }

    @Override
    public GenieResponse<Void> saveTelemetry(BaseTelemetry event) {
        return saveTelemetry(event.toString());
    }
}
