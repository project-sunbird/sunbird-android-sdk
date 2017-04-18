package org.ekstep.genieservices.commons;

import android.content.Context;

import org.ekstep.genieservices.commons.db.SummarizerDBContext;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.impl.SQLiteSession;

/**
 * Created on 18/4/17.
 */

public class AndroidAppContext extends AppContext<Context, AndroidLogger> {

    private IDBSession mDBSession;
    private IDBSession mSummarizerDBSession;

    public static AppContext buildAppContext(Context context, String appPackage, String key, AndroidLogger logger) {
        AppContext<Context, AndroidLogger> appContext = new AndroidAppContext(context, appPackage,key, logger);
        appContext.setDBSession(new SQLiteSession(appContext));
        appContext.setSummarizerDBSession(new SQLiteSession(appContext, new SummarizerDBContext()));
        return appContext;
    }

    private AndroidAppContext(Context context, String appPackage, String key, AndroidLogger logger) {
        super(context, appPackage, key, logger);
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
    public Void setDBSession(IDBSession dbSession) {
        this.mDBSession = dbSession;
        return null;
    }

    @Override
    public Void setSummarizerDBSession(IDBSession dbSession) {
        this.mDBSession = dbSession;
        return null;
    }

}
