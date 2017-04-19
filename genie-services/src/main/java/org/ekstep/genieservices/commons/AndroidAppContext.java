package org.ekstep.genieservices.commons;

import android.content.Context;

import org.ekstep.genieservices.commons.db.SummarizerDBContext;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.impl.SQLiteSession;
import org.ekstep.genieservices.commons.network.IConnectionInfo;
import org.ekstep.genieservices.commons.network.NetworkConnectivity;

/**
 * Created on 18/4/17.
 */

public class AndroidAppContext extends AppContext<Context, AndroidLogger> {

    private IDBSession mDBSession;
    private IDBSession mSummarizerDBSession;
    private IConnectionInfo mConnectionInfo;

    private AndroidAppContext(Context context, String appPackage, String key, AndroidLogger logger) {
        super(context, appPackage, key, logger);
    }

    public static AppContext buildAppContext(Context context, String appPackage, String key, AndroidLogger logger) {
        AppContext<Context, AndroidLogger> appContext = new AndroidAppContext(context, appPackage, key, logger);

        appContext.setDBSession(new SQLiteSession(appContext));
        appContext.setSummarizerDBSession(new SQLiteSession(appContext, new SummarizerDBContext()));
        appContext.setConnectionInfo(new NetworkConnectivity(appContext));

        return appContext;
    }

    @Override
    public IDBSession getDBSession() {
        return mDBSession;
    }

    @Override
    public IDBSession getSummarizerDBSession() {
        return mSummarizerDBSession;
    }

    @Override
    public IConnectionInfo getConnectionInfo() {
        return mConnectionInfo;
    }

    @Override
    public Void setDBSession(IDBSession dbSession) {
        this.mDBSession = dbSession;
        return null;
    }

    @Override
    public Void setSummarizerDBSession(IDBSession dbSession) {
        this.mDBSession = dbSession;
        return null;
    }

    @Override
    public Void setConnectionInfo(IConnectionInfo connectionInfo) {
        this.mConnectionInfo = connectionInfo;
        return null;
    }

}
