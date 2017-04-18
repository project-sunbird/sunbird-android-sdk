package org.ekstep.genieservices.commons.utils;


import android.util.Log;

import org.ekstep.genieservices.commons.BuildConfig;

/**
 * Created on 5/17/2016.
 *
 * @author anil
 */
public class Logger {

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg, tr);
        }
    }

    public static void w(String tag, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg, tr);
        }
    }

}
