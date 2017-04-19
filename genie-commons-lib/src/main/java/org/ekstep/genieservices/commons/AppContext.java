package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.db.cache.ICacheOperation;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

/**
 * Created by shriharsh on 14/4/17.
 */
public abstract class AppContext<C,L extends ILogger> {

    private C mContext;
    private L mLogger;

    private String mAppPackage;
    private String mKey;

    protected AppContext(C context, String appPackage, String key, L logger) {
        this.mContext = context;
        this.mLogger = logger;
        this.mAppPackage = appPackage;
        this.mKey = key;
    }

    public C getContext() {
        return mContext;
    }

    public L getLogger() {
        return mLogger;
    }

    public String getAppPackage() {
        return mAppPackage;
    }

    public String getKey() {
        return mKey;
    }

    public abstract IDBSession getDBSession();

    public abstract IDBSession getSummarizerDBSession();

    public abstract ICacheOperation getPreferenceCache();

    public abstract Void setDBSession(IDBSession dbSession);

    public abstract Void setSummarizerDBSession(IDBSession dbSession);

    public abstract Void setPreferenceCache(ICacheOperation cacheOperarion);

 }
