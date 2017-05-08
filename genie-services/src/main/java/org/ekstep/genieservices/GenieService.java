package org.ekstep.genieservices;

import android.content.Context;

import org.ekstep.genieservices.commons.AndroidAppContext;
import org.ekstep.genieservices.commons.AndroidLogger;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.config.ConfigService;
import org.ekstep.genieservices.profile.UserProfileService;
import org.ekstep.genieservices.telemetry.TelemetryService;

/**
 * Created on 4/14/2017.
 *
 * @author anil
 */
public class GenieService {

    private static AppContext<Context, AndroidLogger>  applicationContext;
    private static ConfigService sConfigService;
    private static TelemetryService sTelemetryService;

    public static GenieService init(Context context, String packageName, String apiKey, String gDataId) {
        AppContext<Context, AndroidLogger>  applicationContext = AndroidAppContext.buildAppContext(context, packageName, apiKey, new AndroidLogger(), gDataId);
        GenieService instance = new GenieService(applicationContext);
        return instance;
    }

    private GenieService(AppContext<Context, AndroidLogger> applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ConfigService getConfigService() {
        if (sConfigService == null) {
            sConfigService = new ConfigService(applicationContext);
        }
        return sConfigService;
    }

    public UserProfileService getUserProfileService() {
        return new UserProfileService(applicationContext);
    }

    public TelemetryService getTelemetryService() {
        if (sTelemetryService == null) {
            sTelemetryService = new TelemetryService(applicationContext);
        }
        return sTelemetryService;
    }
}
