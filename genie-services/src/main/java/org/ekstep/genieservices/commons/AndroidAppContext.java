package org.ekstep.genieservices.commons;

import android.content.Context;

import org.ekstep.genieservices.Constants;
import org.ekstep.genieservices.R;
import org.ekstep.genieservices.commons.db.SummarizerDBContext;
import org.ekstep.genieservices.commons.db.cache.ICacheOperation;
import org.ekstep.genieservices.commons.db.cache.PreferenceWrapper;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.impl.SQLiteSession;
import org.ekstep.genieservices.util.RawFileUtil;

/**
 * Created on 18/4/17.
 */

public class AndroidAppContext extends AppContext<Context, AndroidLogger> {

    private IDBSession mDBSession;
    private IDBSession mSummarizerDBSession;
    private ICacheOperation mCacheOperation;

    private AndroidAppContext(Context context, String appPackage, String key, AndroidLogger logger) {
        super(context, appPackage, key, logger);
    }

    public static AppContext buildAppContext(Context context, String appPackage, String key, AndroidLogger logger) {
        AppContext<Context, AndroidLogger> appContext = new AndroidAppContext(context, appPackage, key, logger);
        appContext.setDBSession(new SQLiteSession(appContext));
        appContext.setSummarizerDBSession(new SQLiteSession(appContext, new SummarizerDBContext()));
        appContext.setPreferenceCache(new PreferenceWrapper(appContext, Constants.SHARED_PREFERENCE_NAME));
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
    public ICacheOperation getPreferenceCache() {
        return mCacheOperation;
    }

    @Override
    public String getStoredResourceData() {
        String response = RawFileUtil.readRawResource(this.getContext(), R.raw.resource_bundle);
        return response;
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
    public Void setPreferenceCache(ICacheOperation cacheOperarion) {
        this.mCacheOperation=cacheOperarion;
        return null;
    }

}
