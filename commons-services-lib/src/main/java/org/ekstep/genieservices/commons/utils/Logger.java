package org.ekstep.genieservices.commons.utils;

import org.ekstep.genieservices.commons.ILogger;

import javax.management.InstanceNotFoundException;

/**
 * Created on 5/17/2016.
 *
 * @author anil
 */
public class Logger {

    private static Logger INSTANCE;

    private ILogger logger;
    private Logger(ILogger logger) {
        this.logger = logger;
    }

    public static void init(ILogger logger) {
        if (INSTANCE == null) {
            INSTANCE = new Logger(logger);
        }
    }

    public static boolean isDebugEnabled() {
        if (INSTANCE != null && INSTANCE.logger != null & INSTANCE.logger.isDebugEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    public static void d(String tag, String msg) {
        if (INSTANCE != null && INSTANCE.logger != null & INSTANCE.logger.isDebugEnabled()) {
            INSTANCE.logger.debug(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (INSTANCE != null && INSTANCE.logger != null & INSTANCE.logger.isDebugEnabled()) {
            INSTANCE.logger.debug(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (INSTANCE != null && INSTANCE.logger != null & INSTANCE.logger.isInfoEnabled()) {
            INSTANCE.logger.info(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (INSTANCE != null && INSTANCE.logger != null & INSTANCE.logger.isInfoEnabled()) {
            INSTANCE.logger.info(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (INSTANCE != null && INSTANCE.logger != null & INSTANCE.logger.isErrorEnabled()) {
            INSTANCE.logger.error(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (INSTANCE != null && INSTANCE.logger != null & INSTANCE.logger.isErrorEnabled()) {
            INSTANCE.logger.error(tag, msg, tr);
        }
    }

}
