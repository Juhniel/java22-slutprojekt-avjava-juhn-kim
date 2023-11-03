# java22-slutprojekt-avjava-juhn-kim

Project Overview
Welcome to the README for our dynamic producer-consumer simulation project! This project is a multi-threaded Java application that models the classic problem of coordinating producers and consumers using a shared buffer. Our simulation is not only a technical representation but also a visual one, enabling users to interact with the system, adjust parameters in real-time, and observe the results of their changes.

Introduction
The producer-consumer problem is a standard example of a multi-process synchronization issue, involving coordinating producers (who generate data) and consumers (who use that data). In our simulation, we ensure thread safety and manage state between these actors to prevent race conditions and deadlocks.

The core components of this project are modeled in separate packages:

Models: Define the essential objects such as Producer, Consumer, and Message.
Threads: Encapsulate the running threads for producers and consumers.
Controllers: Handle the business logic, tying the models and views together.
Views: Provide the graphical user interface (GUI) for user interaction.
Services: Offer utility functions such as state management and property change notification.
Utils: Include helper functions, such as logging mechanisms.
Features
Multithreading
At the heart of the application lies the multithreading logic. Producer threads generate Message objects and deposit them into a Buffer, while Consumer threads retrieve these messages for processing. Thread synchronization is critical here, and we utilize Java’s concurrency utilities to manage it efficiently.

Dynamic Interaction
The application boasts a GUI, the ProductionRegulatorGUI, which allows users to add or remove producers, save or load the application state, and toggle an auto-adjust feature that dynamically changes the production rate based on consumer performance.

Logging and Monitoring
An essential feature of the application is the ability to log and monitor activities in real-time. A dedicated LogController facilitates this, which, along with the Log utility, ensures that all significant events are captured and displayed to the user through the GUI.

State Management
State persistence is handled through the StateController, which can save and load the application's state, allowing for pausing and resuming the simulation without loss of data. This is particularly useful for analyzing the system's behavior over time or after adjusting parameters.

Property Change Notification
A PropertyChangeService enables different parts of the application to listen for and respond to changes in the system's state, ensuring that all components remain in sync.

Auto-Adjustment Logic
An intelligent auto-adjust feature uses the property change system to automatically adjust the rate of production based on the consumption rate, maintaining an efficient balance between producers and consumers.

Installation and Running the Simulation
The project requires Java 8 or higher due to the use of lambda expressions and other recent language features. To run the simulation:

Clone the repository to your local machine.
Compile the source code using your preferred Java IDE or the command line.
Execute the main class to launch the GUI and begin the simulation.
Usage
Once the application is running, the main window of the ProductionRegulatorGUI allows you to:

Add/Remove Producers: Dynamically add or remove producer threads from the simulation.
Save/Load State: Save the current simulation state to a file or load a previous state.
Adjust Intervals: Modify the production interval for producers and the consumption interval for consumers.
Auto-Adjust: Enable or disable the auto-adjust feature which automatically optimizes production rates.
Monitor Logs: View real-time logs for producer-consumer activities and system warnings.
Contributing
We welcome contributions and suggestions! Feel free to fork the repository, make changes, and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.

License
This project is licensed under the MIT License - see the LICENSE.md file for details.

This README provides a comprehensive overview for potential users and contributors to get started with the project. It outlines the purpose, structure, and features of the application while providing instructions for setup and usage.
