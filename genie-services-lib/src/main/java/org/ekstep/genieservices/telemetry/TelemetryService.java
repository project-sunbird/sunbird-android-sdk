package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.exception.InvalidDataException;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.telemetry.model.Event;

import java.util.HashMap;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryService extends BaseService {
    private static final  String SERVICE_NAME=TelemetryService.class.getName();

    public TelemetryService(AppContext appContext) {
        super(appContext);
    }

    public void saveTelemetry(String eventString, IResponseHandler responseHandler) {
        String errorMessage = "Not able to save event";
        HashMap params = new HashMap();
        params.put("Event",eventString);
        params.put("logLevel", "3");

        try {
            Event event = Event.build(mAppContext,eventString);
            event.save();
            Logger.i(mAppContext,SERVICE_NAME, "Event saved successfully");
            GenieResponse response= GenieResponse.getSuccessResponse("Event Saved Successfully");
            TelemetryLogger.logSuccess(mAppContext,response,new HashMap(),SERVICE_NAME,"saveTelemetry@"+SERVICE_NAME,params);
        } catch (DbException e) {
            String logMessage = "Event save failed" + e.toString();
            GenieResponse response= GenieResponse.getErrorResponse(mAppContext, "PROCESSING_ERROR",errorMessage, logMessage);
            TelemetryLogger.logFailure(mAppContext,response,SERVICE_NAME,"","saveTelemetry@"+SERVICE_NAME,params);
        } catch (InvalidDataException e) {
            String logMessage = "Event save failed" + e.toString();
            GenieResponse response= GenieResponse.getErrorResponse(mAppContext, "PROCESSING_ERROR",errorMessage, logMessage);
            TelemetryLogger.logFailure(mAppContext,response,SERVICE_NAME,"","saveTelemetry@"+SERVICE_NAME,params);
        }

    }
}
