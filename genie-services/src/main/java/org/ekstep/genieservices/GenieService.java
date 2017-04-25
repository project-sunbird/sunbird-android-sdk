package org.ekstep.genieservices;

import android.content.Context;

import org.ekstep.genieservices.commons.AndroidAppContext;
import org.ekstep.genieservices.commons.AndroidLogger;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.config.ConfigService;

/**
 * Created on 4/14/2017.
 *
 * @author anil
 */
public class GenieService {

    private AppContext<Context, AndroidLogger>  applicationContext;

    public static GenieService init(Context context, String packageName, String apiKey) {
        AppContext<Context, AndroidLogger>  applicationContext = AndroidAppContext.buildAppContext(context, packageName, apiKey, new AndroidLogger());
        GenieService instance = new GenieService(applicationContext);
        return instance;
    }

    private GenieService(AppContext<Context, AndroidLogger> applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ConfigService getConfigService() {
        return new ConfigService(applicationContext);
    }
}
