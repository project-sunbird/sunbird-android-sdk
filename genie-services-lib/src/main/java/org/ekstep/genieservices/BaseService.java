package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.cache.IKeyValueOperation;

/**
 * Created by swayangjit on 20/4/17.
 */

public class BaseService {

    protected AppContext mAppContext;
    private IKeyValueOperation mkeyValueStore;

    public BaseService(AppContext appContext){
        mAppContext=appContext;
    }

    private IKeyValueOperation getKeyValueStore(){
        if(mkeyValueStore==null){
            return mAppContext.getKeyValueStore();
        }
        return mkeyValueStore;
    }


    protected void putBoolean(String key,boolean value){
        getKeyValueStore().putBoolean(key,value);
    }

    protected boolean getBoolean(String key){
        return getKeyValueStore().getBoolean(key,false);
    }

    protected void putLong(String key,long value){
        getKeyValueStore().putLong(key,value);
    }

    protected long getLong(String key){
        return getKeyValueStore().getLong(key,0);
    }
}
