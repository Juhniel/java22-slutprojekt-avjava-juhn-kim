package org.juhnkim.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.juhnkim.interfaces.LogEventListenerInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Singleton class for Log4j
 */

public class Log {
    private static Log log;
    private final Logger logger;
    private final List<LogEventListenerInterface> logEventListenerInterfaceList = new ArrayList<>();

    /**
     * Private constructor for Singleton pattern
     */
    private Log() {
        logger = (Logger) LogManager.getLogger(Log.class);

    }

    /**
     * Method to get the singleton instance of Log
     * @return instance of Log
     */
    public static Log getInstance() {
        if (log == null) {
            log = new Log();
        }
        return log;
    }

    public Logger getLogger() {
        return logger;
    }

    /**
     * Method to add a new LogEventListener
     * @param listener instance of a class that implements LogEventListener
     */
    public void addLogEventListener(LogEventListenerInterface listener) {
        logEventListenerInterfaceList.add(listener);
    }

    /**
     * Method to log informational messages.
     * It also notifies all the registered log event listeners.
     * @param message the message to be logged
     */
    public void logInfo(String message) {
        // For creating date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss.SSS");
        String dateString = sdf.format(new Date());
        String messageWithDate = "[" + dateString + "] - " + message;

        // Log the message and notify listeners
        logger.info(messageWithDate);
        notifyListeners(messageWithDate);
    }

    /**
     * Private method to notify all registered log event listeners
     * @param message the message to be sent to listeners
     */
    private void notifyListeners(String message) {
        for (LogEventListenerInterface listener : logEventListenerInterfaceList) {
            listener.onLogEvent(message);
        }
    }
}
