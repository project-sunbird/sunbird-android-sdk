package org.ekstep.genieservices.commons;

/**
 * Created by mathew on 18/4/17.
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
    public boolean isVerboseEnabled() {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public boolean isWarningEnabled() {
        return false;
    }

    @Override
    public void verbose(String tag, String msg) {

    }

    @Override
    public void verbose(String tag, String msg, Throwable tr) {

    }

    @Override
    public void debug(String tag, String msg) {

    }

    @Override
    public void debug(String tag, String msg, Throwable tr) {

    }

    @Override
    public void info(String tag, String msg) {

    }

    @Override
    public void info(String tag, String msg, Throwable tr) {

    }

    @Override
    public void warn(String tag, String msg) {

    }

    @Override
    public void warn(String tag, String msg, Throwable tr) {

    }

    @Override
    public void error(String tag, String msg) {

    }

    @Override
    public void error(String tag, String msg, Throwable tr) {

    }
}
