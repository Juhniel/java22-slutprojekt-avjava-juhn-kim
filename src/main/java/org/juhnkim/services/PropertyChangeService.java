package org.juhnkim.services;

import org.juhnkim.views.ProductionRegulatorGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PropertyChangeService implements PropertyChangeListener {
    private final ProductionRegulatorGUI productionRegulatorGUI;
    private double balancePercentage;
    private final Buffer buffer;

    public PropertyChangeService(ProductionRegulatorGUI productionRegulatorGUI, Buffer buffer) {
        this.productionRegulatorGUI = productionRegulatorGUI;
        this.buffer = buffer;
        this.buffer.addPropertyChangeListener(this);
    }

    public double getBalancePercentage() {
        return balancePercentage;
    }

    /**
     * Updates the progress bar on the GUI based on buffer fill level.
     */
    public void updateProgressBar() {
        balancePercentage = (double) buffer.getMessageCount() / buffer.getCapacity() * 100;
        balancePercentage = Math.min(balancePercentage, 100.0);
        productionRegulatorGUI.updateProgressBar((int)balancePercentage);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("messageCount".equals(evt.getPropertyName())) {
            updateProgressBar();
        }
    }
}
