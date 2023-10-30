package org.juhnkim.views;

import javax.swing.*;
import java.awt.*;


public class ProductionRegulatorGUI {
    private JProgressBar progressBar;
    private JFrame frame;
    private JButton addButton, removeButton, saveButton, loadButton, autoAdjustButton;

    private JTextArea textArea;

    public void initializeUI() {
        // Create the main window (frame)
        frame = new JFrame("Production Regulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 200);

        // Create a panel for the logs
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());

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
        autoAdjustButton = new JButton("Auto-Adjust");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(autoAdjustButton);

        // Create a progress bar and add it to the center panel
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, progressBar.getPreferredSize().height));
        centerPanel.add(progressBar);


        // Create a text area for logs
        textArea = new JTextArea(5, 20); // 5 rows, 20 columns
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Add components to main panel
        logPanel.add(scrollPane, BorderLayout.CENTER);

        // Add components to main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(logPanel, BorderLayout.SOUTH);  // Changed this from EAST to SOUTH

        frame.add(mainPanel);

        frame.setVisible(true);
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getLoadButton() {
        return loadButton;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JButton getAutoAdjustButton() {
        return autoAdjustButton;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void updateProgressBar(double balancePercentage) {
        progressBar.setValue((int) balancePercentage);
        progressBar.setString(balancePercentage + "%");

        if (balancePercentage >= 0 && balancePercentage <= 20 || balancePercentage >= 80 && balancePercentage <= 100) {
            progressBar.setForeground(Color.RED);
        } else if (balancePercentage >= 21 && balancePercentage <= 39 || balancePercentage >= 61 && balancePercentage <= 79) {
            progressBar.setForeground(Color.ORANGE);
        } else if (balancePercentage >= 40 && balancePercentage <= 60) {
            progressBar.setForeground(Color.GREEN);
        }
    }

    public void autoAdjustColor(boolean autoAdjust) {
        if(autoAdjust) {
            autoAdjustButton.setBackground(Color.GREEN);
        } else {
            autoAdjustButton.setBackground(null);
        }
    }
}


