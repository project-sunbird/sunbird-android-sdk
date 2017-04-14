package org.ekstep.genieservices.commons;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by shriharsh on 14/4/17.
 */

public class AppContext {

    private Context mContext;
    private String mAppPackage;
    private String mKey;

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

            return new AppContext(context, appPackage, key);
        }
    }

}
