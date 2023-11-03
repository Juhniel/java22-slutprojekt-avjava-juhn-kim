# java22-slutprojekt-avjava-juhn-kim


# Production Regulator 
This project is a multithreaded Java application that models the classic problem of coordinating producers and consumers using a shared buffer. 
Our simulation is not only a technical representation but also a visual one, enabling users to interact with the system, adjust parameters in real-time, 
and observe the results of their changes.

# Introduction
The producer-consumer problem is a standard example of a multiprocess synchronization issue, 
involving coordinating producers (who generate data) and consumers (who use that data). In my simulation, 
we ensure thread safety and manage state between these actors.

The core components of this project are modeled in separate packages:

**Models:** Define the essential objects such as Producer, Consumer, and Message.

**Interfaces:** Re-usable log interface for custom logging events.

**Threads:** Encapsulate the running threads for producers and consumers.

**Controllers:** Handle the business logic, tying the models and views together.

**Views:** Provide the graphical user interface (GUI) for user interaction.

**Services:** Offer utility functions such as state management and property change notification.

**Utils:** Include helper functions, such as logging mechanisms.

# Features
**Multithreading**
At the heart of the application lies the multithreading logic. Producer threads generate Message objects and deposit them into a Buffer, while Consumer threads retrieve these messages for processing. Thread synchronization is critical here, and we utilize Java’s concurrency utilities to manage it efficiently.

**Dynamic Interaction**
The application boasts a GUI, the ProductionRegulatorGUI, which allows users to add or remove producers, save or load the application state, and toggle an auto-adjust feature that dynamically changes the production rate based on consumer performance.

**Logging and Monitoring**
An essential feature of the application is the ability to log and monitor activities in real-time. A dedicated LogController facilitates this, which, along with the Log utility, ensures that all significant events are captured and displayed to the user through the GUI.

**State Management**
State persistence is handled through the StateController, which can save and load the application's state, allowing for pausing and resuming the simulation without loss of data. This is particularly useful for analyzing the system's behavior over time or after adjusting parameters.

**Property Change Notification**
A PropertyChangeService enables different parts of the application to listen for and respond to changes in the system's state, ensuring that all components remain in sync.

**Auto-Adjustment Logic**
An intelligent auto-adjust feature uses the property change system to automatically adjust the rate of production based on the consumption rate, maintaining an efficient balance between producers and consumers.



# Functions
**Add/Remove Producers:** Dynamically add or remove producer threads from the simulation. 

**Save/Load State:** Save the current simulation state to a file or load a previous state.

**Randomized Intervals:** The production interval for producers and the consumer intervals are randomized when added.

**Auto-Adjust:** Enable or disable the auto-adjust feature which automatically optimizes production rates.

**Monitor Logs:** View real-time logs for producer-consumer activities and system warnings.


# Patterns
**MVC Pattern**

**Singleton Pattern**

**Observer Pattern** (With PropertyChangeService since Observer is deprecated)

**Factory Pattern**

**Dependency Injection**
