package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.AppContext;

/**
 * Created on 4/14/2017.
 *
 * @author anil
 */
public class GenieService {

    private static GenieService instance;
    private static AppContext appContext;

    private GenieService(AppContext context) {
        appContext = context;
    }

    public static GenieService getInstance() {
        return instance;
    }

    public static void init(AppContext context) {
        instance = new GenieService(context);
    }

    public AppContext getAppContext() {
        return appContext;
    }
}
