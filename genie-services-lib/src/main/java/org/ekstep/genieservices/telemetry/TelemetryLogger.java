package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.bean.GEServiceAPICall;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.network.IConnectionInfo;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.telemetry.cache.TelemetryTagCache;
import org.ekstep.genieservices.telemetry.model.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by swayangjit on 27/4/17.
 */

public class TelemetryLogger {

    private static final String TAG = TelemetryLogger.class.getSimpleName();

    /**
     * 1 indicates TRACE, 2 is DEBUG, 3 is INFO, 4 is ERROR
     * Default log level is INFO. If the params does not include a level, it will be assumed to be an attempt to log INFO.
     * At some point, the log level can be set from outside and be used to allow TRACE level logging on a device. The log level can be set by a push notification/config json that gets pulled from the server
     */
    public static int appLoggingLevel = 3;


    public static void success(AppContext appContext,GenieResponse response,String service,String method, HashMap params){
        int parsedLogLevel = 3;
        if (params != null & params.get("logLevel") != null) {
            try {
                parsedLogLevel = Integer.valueOf(params.get("logLevel").toString());
            } catch (Exception ex) {}
        }
        if (parsedLogLevel >= appLoggingLevel) {
            log(appContext,response,service,ServiceConstants.SUCCESS_RESPONSE, new HashMap<String, Object>(), method, params);
        } else {
            //No logging as the log level requested was below the app logging level
        }
    }

    public void error(AppContext appContext, GenieResponse response,String service, Exception e, String method, HashMap params){
        HashMap<String, Object> result = new HashMap<>();
        result.put("error", e.getMessage());
        log(appContext,response,service,ServiceConstants.FAILURE_RESPONSE, result, method, params);
    }

    public void error(AppContext appContext, GenieResponse response,String service,String message, String method, HashMap params){
        HashMap<String, Object> result = new HashMap<>();
        result.put("message", message);
        log(appContext,response,service,ServiceConstants.FAILURE_RESPONSE, result, method, params);
    }

    private static void log(AppContext appContext,GenieResponse response,String service, String status, Map<String, Object> result, String method, HashMap params) {
        GEServiceAPICall.Builder eventBuilder = new GEServiceAPICall.Builder();
        GEServiceAPICall event = eventBuilder.service(service)
                .method(method)
                .mode(getNetworkMode(appContext.getConnectionInfo()))
                .request(params)
                .response(response)
                .gameID(appContext.getBuildConfig().getGid())
                .gameVersion(appContext.getBuildConfig().getVersionName())
                .build();
        Set<String> telemetryTags = TelemetryTagCache.activeTags(appContext);
        Event eventWithWrapper = Event.build(appContext,event.getEID(), telemetryTags).withEvent(event.toString());
        try {
            eventWithWrapper.updateSessionDetails();
            eventWithWrapper.updateEventDetails();
            eventWithWrapper.save();
        } catch (DbException ex) {
            Logger.e(appContext,TAG, ex.getMessage());
        }
    }


    private static String  getNetworkMode(IConnectionInfo connectionInfo) {
        if (connectionInfo.isConnectedOverWifi()) {
            return GEServiceAPICall.MODE_WIFI;
        } else if (connectionInfo.isConnected()) {
            return GEServiceAPICall.MODE_MDATA;
        }
        return GEServiceAPICall.MODE_NO_NETWORK;
    }
}
