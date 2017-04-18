package org.ekstep.genieservices.commons;

import android.content.Context;
import android.text.TextUtils;

import org.ekstep.genieservices.commons.db.SummarizerDBContext;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.impl.SQLiteSession;
import org.ekstep.genieservices.commons.utils.BuildConfigUtil;

/**
 * Created by shriharsh on 14/4/17.
 */

public class AppContext {

    private Context mContext;
    private String mAppPackage;
    private String mKey;
    private IDBSession mIdbSession;

    public AppContext(Context context, String appPackage, String key) {
        mContext = context;
        mAppPackage = appPackage;
        mKey = key;
    }

    public String getAppPackage() {
        return mAppPackage;
    }

    public Context getContext() {
        return mContext;
    }

    public String getKey() {
        return mKey;
    }

    public IDBSession getDBSession() {
        if (mIdbSession == null) {
            mIdbSession = new SQLiteSession(this);
        }

        return mIdbSession;
    }

    public IDBSession getSummarizerDBSession() {
        if (mIdbSession == null) {
            mIdbSession = new SQLiteSession(this, new SummarizerDBContext());
        }

        return mIdbSession;
    }

    public static class Builder {
        private Context context;
        private String appPackage;
        private String key;

        public Builder(Context context, String appPackage) {
            this.context = context;
            this.appPackage = appPackage;

        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder context(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context cannot be null.");
            }
            this.context = context;
            return this;
        }

        public Builder appPackage(String appPackage) {
            // TODO: Check if guava has to be used for utils

            if (appPackage == null) {
                throw new IllegalArgumentException("appPackage cannot be null.");
            }

            if (TextUtils.isEmpty(appPackage)) {
                throw new IllegalArgumentException("appPackage cannot be empty.");
            }

            try {
                BuildConfigUtil.loadBuildConfigClass(appPackage);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();

                throw new IllegalArgumentException("appPackage should be same as defined in Manifest.xml");
            }

            this.appPackage = appPackage;
            return this;
        }

        public AppContext build() {
            if (context == null) {
                throw new IllegalStateException("Context required.");
            }

            if (appPackage == null) {
                throw new IllegalStateException("appPackage required.");
            }

            if (TextUtils.isEmpty(appPackage)) {
                throw new IllegalStateException("appPackage cannot be empty.");
            }

            try {
                BuildConfigUtil.loadBuildConfigClass(appPackage);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();

                throw new IllegalStateException("appPackage should be same as defined in Manifest.xml");
            }

            return new AppContext(context, appPackage, key);
        }
    }

}
