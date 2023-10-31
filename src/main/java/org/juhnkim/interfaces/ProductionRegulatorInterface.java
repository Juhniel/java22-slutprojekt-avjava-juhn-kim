package org.juhnkim.interfaces;

public interface ProductionRegulatorInterface {
    void addProducer();
    void removeProducer();
    void saveCurrentState();
    void loadSavedState();
    void toggleAutoAdjust();
    void autoAdjustProducers();
    void logProducerWarnings();
}
