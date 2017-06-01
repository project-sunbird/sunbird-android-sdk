package org.ekstep.genieproviders.util;

import android.util.Log;

import org.ekstep.genieservices.commons.ILogger;

/**
 * Created on 18/4/17.
 *
 * @author mathew
 */
public class AndroidLogger implements ILogger {

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void debug(String tag, String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void debug(String tag, String msg, Throwable tr) {
        Log.d(tag, msg, tr);
    }

    @Override
    public void info(String tag, String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void info(String tag, String msg, Throwable tr) {
        Log.i(tag, msg, tr);
    }

    @Override
    public void error(String tag, String msg) {
        Log.e(tag, msg);
    }

    @Override
    public void error(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
    }
}
