package org.juhnkim.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductionRegulatorGUI {

    private JButton addButton, removeButton, saveButton, loadButton;

    public void initializeUI() {
        // Create the main window (frame)
        JFrame frame = new JFrame("My Swing App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 200);

        // Create a panel to hold all other components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create buttons and add them to button panel
        addButton = new JButton("+");
        removeButton = new JButton("-");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        // Create a progress bar
        JProgressBar progressBar = new JProgressBar(0, 100);

        // Create a text field
        JTextField textField = new JTextField(20);

//        // Add listeners to buttons (You can replace these with your own actions)
//        addButton.addActionListener(e -> {
//            // Example: Increase progress bar value
//            progressBar.setValue(progressBar.getValue() + 10);
//        });
//
//        removeButton.addActionListener(e -> {
//            // Example: Decrease progress bar value
//            progressBar.setValue(progressBar.getValue() - 10);
//        });
//
//        saveButton.addActionListener(e -> {
//            // Example: Save text field value
//            System.out.println("Saved: " + textField.getText());
//        });
//
//        loadButton.addActionListener(e -> {
//            // Example: Load value into text field
//            textField.setText("Loaded Value");
//        });

        // Add components to panel
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(textField, BorderLayout.EAST);

        frame.add(panel);

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


    //    public static void main(String[] args) {
//        Buffer buffer = new Buffer();
//
//        Producer producer = new Producer(buffer);
//        Thread producerThread = new Thread(producer);
//        producerThread.start();
//
//        Consumer consumer = new Consumer(buffer);
//        Thread consumerThread = new Thread(consumer);
//        consumerThread.start();
//
//    }
}


