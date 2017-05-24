package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;

/**
 * Created by mathew on 22/5/17.
 */

public class GenieAsyncService {

    private static GenieService sService;
    private static ConfigService mConfigService;

    public void init(GenieService genieService) {
        sService = genieService;
    }

    public static ConfigService getConfigService() {
        if (mConfigService == null) {
            mConfigService = new ConfigService(sService);
        }
        return mConfigService;
    }
}
