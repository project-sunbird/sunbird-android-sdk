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

    protected void putToKeyValueStore(String key, boolean value) {
        mAppContext.getKeyValueStore().putBoolean(key, value);
    }

    protected boolean getBooleanFromKeyValueStore(String key) {
        return mAppContext.getKeyValueStore().getBoolean(key, false);
    }

    protected void putToKeyValueStore(String key, long value) {
        mAppContext.getKeyValueStore().putLong(key, value);
    }

    protected long getLongFromKeyValueStore(String key) {
        return mAppContext.getKeyValueStore().getLong(key, 0);
    }

}
