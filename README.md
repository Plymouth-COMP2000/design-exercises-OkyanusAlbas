# COMP2000 - Restaurant Management Application

This repository contains the full implementation for the COMP2000 Software Engineering 2 coursework. The project consists of two main components: a native Android application and a corresponding Java RESTful API server for user authentication.

## Features

### Guest Features
- **User Authentication:** Register for a new account and log in.
- **Browse Menu:** View a list of all available menu items.
- **Reservation Management:**
    - Create a new table reservation.
    - View a list of personal reservations.
    - Edit the details of an existing reservation.
    - Cancel a reservation.
- **Notifications:** Receive local notifications for reservation updates.
- **Session Management:** Users remain logged in between app sessions.

### Staff Features
- **User Authentication:** Register for a staff account and log in.
- **Menu Management (CRUD):**
    - View all menu items.
    - Add new items to the menu.
    - Edit the details (name, description, price) of existing items.
    - Delete items from the menu.
- **Reservation Management:**
    - View a list of **all** customer reservations.
    - Cancel any customer's reservation.
- **Notifications:** Receive local notifications for new reservations.
- **Session Management:** Users remain logged in between app sessions.

### Technical Features
- **Adaptive UI:** The user interface automatically adapts for both phone (Bottom Navigation) and tablet (Navigation Rail) screen sizes.
- **Local Data Persistence:** Menu and reservation data is stored locally in a SQLite database, ensuring data is saved even when the app is closed.
- **RESTful API Communication:** User authentication is handled by making network requests to a dedicated Java API server.

---

## Project Structure

The project is divided into two main parts located in separate folders:

1.  `restaurantapp/`
    - The client-side Android application project, to be opened in Android Studio.
2.  `java-api-server/`
    - The server-side Java REST API, built with Spring Boot. This must be run from the command line.

---

## Setup and Running Guide

To run the complete system, you must first run the Java API server and then run the Android application.

### Prerequisites
- **Java Development Kit (JDK):** Version 11 or higher.
- **Apache Maven:** Required to build the Java API server.
- **Android Studio:** For running the Android application.
- **Android Emulator or Device:** To install and test the app.

### Part 1: Running the Java API Server

1.  **Open a Terminal:**
    Open a new command line terminal (PowerShell on Windows, or Terminal on macOS/Linux).

2.  **Navigate to the API Directory:**
    Use the `cd` command to navigate into the `java-api-server` folder.
    ```sh
    cd to java-api-server
    ```

3.  **Build the Project with Maven:**
    Run the following command. This will download all necessary dependencies and compile the server into a runnable `.jar` file.
    ```sh
    mvn clean install
    ```
    On success, you will see a `BUILD SUCCESS` message.

4.  **Run the Server:**
    Now, run the `.jar` file that was created in the `target` directory.
    ```sh
    java -jar target/java-api-server-1.0.0.jar
    ```

5.  **Verify the Server is Running:**
    The terminal will show the Spring Boot startup logo and log messages. The final line should say something like:
    `Tomcat started on port(s): 8080 (http)`

    **Important:** Keep this terminal window open. Closing it will shut down the server.

### Part 2: Running the Android Application

1.  **Open the Project in Android Studio:**
    - Launch Android Studio.
    - Select `File > Open...` and navigate to the `restaurantapp` folder.
    - Wait for Gradle to sync and build the project.

2.  **Verify the API URL:**
    The code is already configured to connect to the local server running on your machine. Open `LoginActivity.java` and `RegisterActivity.java` and ensure the `API_BASE_URL` is set correctly for the Android emulator:
    ```java
    private static final String API_BASE_URL = "http://10.0.2.2:8080/";
    ```

3.  **Run the App:**
    - Select an emulator or connect a physical device.
    - Click the "Run" button (the green play icon) in Android Studio.

---

## How to Test the Full Application

1.  **Ensure the Java API server is running in its terminal.**
2.  **Run the Android app from Android Studio.**
3.  **Register a User:**
    - From the Login screen, tap **Register**.
    - Fill out all the fields.
    - To create a staff account, toggle the "Create Staff Account" switch **on**.
    - To create a guest account, leave the switch **off**.
    - Tap **Create Account**. You should see a "Registration successful!" message.
4.  **Log In:**
    - On the Login screen, enter the **username** and **password** for the user you just created.
    - Tap **Login**.
    - You will be navigated to the appropriate dashboard based on the user's role. The app will remember you are logged in if you close and reopen it.
5.  **Test Features:**
    - As a guest, try making a reservation and see the local notification.
    - As a staff member, add a new item to the menu and then log in as a guest to see if it appears.
