package org.juhnkim.controllers;

import org.juhnkim.services.LogService;


public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    public void logConsumedToProducerRatio() {
        logService.logConsumedToProducerRatio();
    }

    public void logProducerWarnings(double balancePercentage) {
        logService.logProducerWarnings(balancePercentage);
    }
}
