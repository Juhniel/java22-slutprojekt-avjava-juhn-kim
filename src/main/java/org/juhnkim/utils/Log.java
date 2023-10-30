package org.juhnkim.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class Log {

    private static Log log;
    private final Logger logger;

    private Log() {
        logger = (Logger) LogManager.getLogger(Log.class);
    }

    public static Log getInstance() {
        if (log == null) {
            log = new Log();
        }
        return log;
    }

    public Logger getLogger() {
        return logger;
    }
}
