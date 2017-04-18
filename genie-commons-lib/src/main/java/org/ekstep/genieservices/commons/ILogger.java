package org.ekstep.genieservices.commons;

/**
 * Created by mathew on 18/4/17.
 */

public interface ILogger {
    public boolean isDebugEnabled();
    public boolean isErrorEnabled();
    public boolean isVerboseEnabled();
    public boolean isInfoEnabled();
    public boolean isWarningEnabled();

    public void verbose(String tag, String msg);
    public void verbose(String tag, String msg, Throwable tr);
    public void debug(String tag, String msg);
    public void debug(String tag, String msg, Throwable tr);
    public void info(String tag, String msg);
    public void info(String tag, String msg, Throwable tr);
    public void warn(String tag, String msg);
    public void warn(String tag, String msg, Throwable tr);
    public void error(String tag, String msg);
    public void error(String tag, String msg, Throwable tr);
}
