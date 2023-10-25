package org.juhnkim.views;

import javax.swing.*;
import java.awt.*;


public class ProductionRegulatorGUI {
    private JProgressBar progressBar;
    private JFrame frame;
    private JButton addButton, removeButton, saveButton, loadButton;

    public void initializeUI() {
        // Create the main window (frame)
        frame = new JFrame("Production Regulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 200);

        // Create a panel to hold all other components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        // Create buttons and add them to button panel
        addButton = new JButton("+");
        removeButton = new JButton("-");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        // Create a progress bar and add it to the center panel
        progressBar = new JProgressBar(0, 100);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, progressBar.getPreferredSize().height));
        centerPanel.add(progressBar);


        // Create a text area for logs
        JTextArea textArea = new JTextArea(5, 20); // 5 rows, 20 columns
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Add components to main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.EAST);

        frame.add(mainPanel);

        frame.setVisible(true);
    }

    public JButton getAddButton() {
        return addButton;
    }

    public void setAddButton(JButton addButton) {
        this.addButton = addButton;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public void setRemoveButton(JButton removeButton) {
        this.removeButton = removeButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(JButton saveButton) {
        this.saveButton = saveButton;
    }

    public JButton getLoadButton() {
        return loadButton;
    }

    public void setLoadButton(JButton loadButton) {
        this.loadButton = loadButton;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void updateProgressBar(int balancePercentage) {
        progressBar.setValue(balancePercentage);
    }
}


