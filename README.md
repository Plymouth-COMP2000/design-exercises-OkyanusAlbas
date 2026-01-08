# COMP2000 – Restaurant Management Application

This repository contains the full implementation for the **COMP2000 Software Engineering 2 coursework**. The project consists of two main components:

- A **native Android application** (Guest and Staff interfaces)
- A **Java RESTful API server** used for user authentication

The system allows staff to manage menu items and reservations, while guests can browse the menu and create and manage their own reservations.

---

## Features

### Guest Features
- User authentication (register and log in)
- Browse the restaurant menu
- Reservation management:
  - Create a reservation
  - View personal reservations
  - Edit an existing reservation
  - Cancel a reservation
- Local notifications for reservation updates
- Persistent login between app sessions

---

### Staff Features
- Staff account registration and login
- Menu management (CRUD):
  - View menu items
  - Add new menu items
  - Edit item details (name, description, price)
  - Delete menu items
- Reservation management:
  - View all customer reservations
  - Cancel customer reservations
- Notifications for new reservations
- Persistent login between sessions

---

### Technical Features
- Adaptive UI:
  - BottomNavigationView for phones
  - NavigationRailView for tablets (600dp+)
- Local data persistence using SQLite
- RESTful API communication with a Java Spring Boot server
- Asynchronous network requests using Volley

---

## Project Structure


Java Server

```

PS C:\Users\albao\DOCUMENTS\DEV\YEAR2\COMP2000\java-api-server> tree /F
Folder PATH listing for volume Windows-SSD
Volume serial number is BCDB-A4E1
C:.
│   pom.xml
│
├───src
│   └───main
│       ├───java
│       │   └───com
│       │       └───example
│       │           └───api
│       │                   ApiApplication.java
│       │                   User.java
│       │                   UserController.java
│       │
│       └───resources
│               application.properties
│
└───target
    │   java-api-server-1.0.0.jar
    │   java-api-server-1.0.0.jar.original
    │
    ├───classes
    │   │   application.properties
    │   │
    │   └───com
    │       └───example
    │           └───api
    │                   ApiApplication.class
    │                   User.class
    │                   UserController.class
    │
    ├───generated-sources
    │   └───annotations
    ├───maven-archiver
    │       pom.properties
    │
    └───maven-status
        └───maven-compiler-plugin
            └───compile
                └───default-compile
                        createdFiles.lst
                        inputFiles.lst

```

Android Studio

andorid studio bit large to put it here and if i limit tree interaction it looks bad










---

## Setup and Running Guide

To run the full system, start the API server first and then run the Android application.

---

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Apache Maven
- Android Studio
- Android emulator or physical Android device

---

## Part 1: Running the Java API Server

1. Open a terminal (PowerShell on Windows or Terminal on macOS/Linux)

2. Navigate to the API directory:
   ```sh
   cd java-api-server


mvn clean install


java -jar target/java-api-server-1.0.0.jar

Tomcat started on port(s): 8080 (http)






## Running the Android Application

This section explains how to run the Android client application once the Java API server is active.

### 1. Open the Project in Android Studio
- Launch **Android Studio**
- Select **File > Open**
- Navigate to the `restaurantapp` directory
- Open the project and wait for **Gradle sync** to complete

---

### 2. Verify API Configuration
The Android app is configured to communicate with the local API server using the Android emulator’s loopback address.

Check the following files:
- `LoginActivity.java`
- `RegisterActivity.java`

Ensure the base URL is set as follows:
```java
private static final String API_BASE_URL = "http://10.0.2.2:8080/";



Run The Application in Android studio

Once Logged in application stores user session
Closing and reopening the app will automatically redirect the user to their dashboard unless they explicitly log out.
wipe the data from the virtual device if something buggy happens to reset