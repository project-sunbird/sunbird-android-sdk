package org.ekstep.genieservices.commons;

/**
 * Created on 18/4/17.
 */
public interface ILogger {

    boolean isDebugEnabled();

    boolean isErrorEnabled();

    boolean isInfoEnabled();

    void debug(String tag, String msg);

    void debug(String tag, String msg, Throwable tr);

    void info(String tag, String msg);

    void info(String tag, String msg, Throwable tr);

    void error(String tag, String msg);

    void error(String tag, String msg, Throwable tr);

}
