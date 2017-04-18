package org.ekstep.genieservices.commons.utils;

import org.ekstep.genieservices.commons.AppContext;

/**
 * Created on 5/17/2016.
 *
 * @author anil
 */
public class Logger {

    public static void v(AppContext appContext, String tag, String msg) {
        if (appContext.getLogger().isVerboseEnabled()) {
            appContext.getLogger().verbose(tag, msg);
        }
    }

    public static void v(AppContext appContext, String tag, String msg, Throwable tr) {
        if (appContext.getLogger().isVerboseEnabled()) {
            appContext.getLogger().verbose(tag, msg, tr);
        }
    }

    public static void d(AppContext appContext, String tag, String msg) {
        if (appContext.getLogger().isDebugEnabled()) {
            appContext.getLogger().debug(tag, msg);
        }
    }

    public static void d(AppContext appContext, String tag, String msg, Throwable tr) {
        if (appContext.getLogger().isDebugEnabled()) {
            appContext.getLogger().debug(tag, msg, tr);
        }
    }

    public static void i(AppContext appContext, String tag, String msg) {
        if (appContext.getLogger().isInfoEnabled()) {
            appContext.getLogger().info(tag, msg);
        }
    }

    public static void i(AppContext appContext, String tag, String msg, Throwable tr) {
        if (appContext.getLogger().isInfoEnabled()) {
            appContext.getLogger().info(tag, msg, tr);
        }
    }

    public static void w(AppContext appContext, String tag, String msg) {
        if (appContext.getLogger().isWarningEnabled()) {
            appContext.getLogger().warn(tag, msg);
        }
    }

    public static void w(AppContext appContext, String tag, String msg, Throwable tr) {
        if (appContext.getLogger().isWarningEnabled()) {
            appContext.getLogger().warn(tag, msg, tr);
        }
    }

    public static void w(AppContext appContext, String tag, Throwable tr) {
        if (appContext.getLogger().isWarningEnabled()) {
            appContext.getLogger().warn(tag, null, tr);
        }
    }

    public static void e(AppContext appContext, String tag, String msg) {
        if (appContext.getLogger().isErrorEnabled()) {
            appContext.getLogger().error(tag, msg);
        }
    }

    public static void e(AppContext appContext, String tag, String msg, Throwable tr) {
        if (appContext.getLogger().isErrorEnabled()) {
            appContext.getLogger().error(tag, msg, tr);
        }
    }

}
