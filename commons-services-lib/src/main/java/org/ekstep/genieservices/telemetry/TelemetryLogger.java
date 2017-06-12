package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GEServiceAPICall;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.network.IConnectionInfo;

import java.util.HashMap;

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

    private static TelemetryLogger sTelemetryLogger;

    private ITelemetryService mTelemetryService;

    private TelemetryLogger(ITelemetryService telemetryService) {
        this.mTelemetryService = telemetryService;
    }

    public static void init(ITelemetryService telemetryService) {
        if (sTelemetryLogger == null) {
            sTelemetryLogger = new TelemetryLogger(telemetryService);
        }
    }

    public static void logSuccess(AppContext appContext, GenieResponse response, String service, String method, HashMap params) {
        appLoggingLevel = appContext.getParams().getLogLevel();
        int parsedLogLevel = 3;
        if (params != null & params.get("logLevel") != null) {
            try {
                parsedLogLevel = Integer.valueOf(params.get("logLevel").toString());
            } catch (Exception ex) {
            }
        }
        if (parsedLogLevel >= appLoggingLevel) {
            log(appContext, response, service, method, params, new HashMap());
        } else {
            //No logging as the log level requested was below the app logging level
        }
    }

    public static void logFailure(AppContext appContext, GenieResponse response, String service, String method, HashMap params, Exception e) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("error", e.getMessage());
        log(appContext, response, service, method, params, result);
    }

    public static void logFailure(AppContext appContext, GenieResponse response, String service, String method, HashMap params, String message) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("message", message);
        log(appContext, response, service, method, params, result);
    }

    public static void log(AppContext appContext, GenieResponse response, String service, String method, HashMap params, HashMap result) {
        save(create(appContext, response, result, service, method, params));
    }

    public static void log(Telemetry telemetry) {
        save(telemetry);
    }

    public static Telemetry create(AppContext appContext, GenieResponse response, HashMap result, String service, String method, HashMap params) {
        GEServiceAPICall.Builder eventBuilder = new GEServiceAPICall.Builder(new GameData(appContext.getParams().getGid(), appContext.getParams().getVersionName()));
        return eventBuilder.service(service)
                .method(method)
                .mode(getNetworkMode(appContext.getConnectionInfo()))
                .request(params)
                .response(response)
                .result(result)
                .build();
    }

    private static void save(Telemetry event) {
        //This could throw a NPE if the telemetry service is not injected via the init method.
        // Have purposefully kept it this way so that the caller knows that init has not happened during testing.
        sTelemetryLogger.mTelemetryService.saveTelemetry(event);
    }


    public static String getNetworkMode(IConnectionInfo connectionInfo) {
        if (connectionInfo.isConnectedOverWifi()) {
            return ServiceConstants.APIExecutionMode.MODE_WIFI;
        } else if (connectionInfo.isConnected()) {
            return ServiceConstants.APIExecutionMode.MODE_MDATA;
        }
        return ServiceConstants.APIExecutionMode.MODE_NO_NETWORK;
    }
}
