package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.network.IConnectionInfo;
import org.ekstep.genieservices.commons.network.IHttpClientFactory;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public abstract class AppContext<C> {

    private C context;

    protected AppContext(C context) {
        this.context = context;
    }

    public C getContext() {
        return context;
    }

    public abstract IParams getParams();

    public abstract IDBSession getDBSession();

    public abstract Void setDBSession(IDBSession session);

    public abstract IKeyValueStore getKeyValueStore();

    public abstract IConnectionInfo getConnectionInfo();

    public abstract IHttpClientFactory getHttpClientFactory();

    public abstract IDeviceInfo getDeviceInfo();

    public abstract ILocationInfo getLocationInfo();

    public abstract IDownloadManager getDownloadManager();

    public abstract IAPKInstaller getAPKInstaller();

}
