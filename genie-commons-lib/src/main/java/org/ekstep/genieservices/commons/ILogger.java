package org.ekstep.genieservices.commons;

/**
 * Created on 18/4/17.
 *
 * @author mathew
 */
public interface ILogger {

    boolean isDebugEnabled();

    boolean isErrorEnabled();

    boolean isVerboseEnabled();

    boolean isInfoEnabled();

    boolean isWarningEnabled();

    void verbose(String tag, String msg);

    void verbose(String tag, String msg, Throwable tr);

    void debug(String tag, String msg);

    void debug(String tag, String msg, Throwable tr);

    void info(String tag, String msg);

    void info(String tag, String msg, Throwable tr);

    void warn(String tag, String msg);

    void warn(String tag, String msg, Throwable tr);

    void error(String tag, String msg);

    void error(String tag, String msg, Throwable tr);

}
