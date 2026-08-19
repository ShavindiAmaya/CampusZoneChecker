# Campus Zone Checker

## Project Overview
**Campus Zone Checker** is a Kotlin-based Android application designed to determine if a user is within a specific geographic boundary. Specifically, it checks if the user's current GPS location is within a 100-meter radius of the **Department of Industrial Management (DIM), University of Kelaniya**. This project serves as a practical implementation of location-based services in Android development.

## Features
*   **Real-time Location Fetching:** Uses Google Play Services' Fused Location Provider for accurate GPS coordinates.
*   **Proximity Detection:** Calculates the distance between the user and a fixed reference point.
*   **Visual Feedback:** Dynamically updates the UI background color and status text based on the user's zone status.
*   **Permission Handling:** Implements Android runtime permissions for accessing fine and coarse location.

## Technologies Used
*   **Language:** Kotlin
*   **Platform:** Android
*   **API:** Google Play Services Location (FusedLocationProviderClient)
*   **UI Components:** AppCompat, ConstraintLayout, Material Components

## How Location Checking Works
The application retrieves the user's current coordinates (latitude and longitude) and compares them against the hardcoded reference coordinates of the DIM building. It uses the distanceTo() method from the Android Location class to calculate the distance between the user's location and the reference location in meters.

## Reference Location
*   **Location:** Department of Industrial Management, University of Kelaniya
*   **Latitude:** `6.9727071`
*   **Longitude:** `79.9157605`

## Zone Radius
*   **Radius:** 100 meters

## How to Run the Application
1.  Clone the repository or download the source code.
2.  Open the project in **Android Studio**.
3.  Ensure you have the latest Android SDK and Build Tools installed.
4.  Connect a physical Android device or start an Emulator with Google Play Services enabled.
5.  Click the **Run** button in Android Studio.
6.  Grant location permissions when prompted by the app.

## How to Test Inside Zone and Outside Zone
### Testing on a Physical Device:
*   **Inside Zone:** Physically walk within 100 meters of the DIM building and tap the "Check My Zone" button.
*   **Outside Zone:** Move at least 100 meters away from the DIM building and tap the "Check My Zone" button.

### Testing on an Emulator:
1.  Open the **Extended Controls** (three dots) on the emulator sidebar.
2.  Navigate to the **Location** tab.
3.  **Inside Zone:** Set the coordinates to `6.9727071, 79.9157605` (or any point within 100m) and click "Send".
4.  **Outside Zone:** Set the coordinates to a distant location (e.g., `7.0000000, 80.0000000`) and click "Send".
5.  Tap the "Check My Zone" button in the app to see the status update.

## Expected Results
*   **Inside Zone:**
    *   Status Text: "Status: Inside Zone"
    *   Background Color: **Green**
    *   Distance: Displayed in meters (<= 100.0 m)
*   **Outside Zone:**
    *   Status Text: "Status: Outside Zone"
    *   Background Color: **Red**
    *   Distance: Displayed in meters (> 100.0 m)
