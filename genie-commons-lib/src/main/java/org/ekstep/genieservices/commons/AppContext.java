package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.param.IParams;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.network.IConnectionInfo;
import org.ekstep.genieservices.commons.network.IHttpClient;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public abstract class AppContext<C, L extends ILogger> {

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

    public abstract Void setDBSession(IDBSession session);

    public abstract Void setSummarizerDBSession(IDBSession session);

    public abstract Void setBuildConfig(IParams buildConfig);

    public abstract IKeyValueStore getKeyValueStore();

    public abstract IConnectionInfo getConnectionInfo();

    public abstract IHttpClient getHttpClient();

    public abstract IParams getBuildConfig();

}
