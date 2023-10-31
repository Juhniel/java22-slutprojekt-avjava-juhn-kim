package org.juhnkim.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.juhnkim.interfaces.LogEventListener;

import java.util.ArrayList;
import java.util.List;


public class Log {

    private static Log log;
    private final Logger logger;
    private final List<LogEventListener> logEventListenerList = new ArrayList<>();

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

    public void addLogEventListener(LogEventListener listener) {
        logEventListenerList.add(listener);
    }

    public void logInfo(String message) {
        logger.info(message);
        notifyListeners(message);
    }

    private void notifyListeners(String message) {
        for (LogEventListener listener : logEventListenerList) {
            listener.onLogEvent(message);
        }
    }
}
