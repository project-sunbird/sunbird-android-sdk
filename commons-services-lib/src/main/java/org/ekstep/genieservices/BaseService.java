package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.AppContext;

/**
 * Created on 20/4/17.
 *
 * @author swayangjit
 */
public class BaseService {

    protected AppContext mAppContext;

    public BaseService(AppContext appContext) {
        mAppContext = appContext;
    }

    protected long getLongFromKeyValueStore(String key) {
        return mAppContext.getKeyValueStore().getLong(key, 0);
    }

}
