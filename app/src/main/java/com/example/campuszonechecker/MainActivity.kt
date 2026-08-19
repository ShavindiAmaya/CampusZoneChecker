package com.example.campuszonechecker

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : AppCompatActivity() {

    // 1. Add hardcoded constants for reference coordinates and zone radius.
    // Replace these values with your campus, office, or home coordinates.
    private val REFERENCE_LATITUDE = 6.9727071 // DIM Building Latitude
    private val REFERENCE_LONGITUDE = 79.9157605 // DIM Building Longitude
    private val ZONE_RADIUS_METERS = 100.0 // 100 meters radius
    // 2. Fused Location Provider Client: The main entry point for interacting with the location services.
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Variables for UI elements
    private lateinit var checkButton: Button
    private lateinit var statusText: TextView
    private lateinit var distanceText: TextView

    // Request code for runtime permission request
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enables edge-to-edge display (system bars are translucent)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize our UI elements by their IDs from activity_main.xml
        checkButton = findViewById(R.id.checkButton)
        statusText = findViewById(R.id.statusText)
        distanceText = findViewById(R.id.distanceText)

        // Adjusts the padding of the root layout to account for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 3. When checkButton is clicked, we trigger the permission and location logic.
        checkButton.setOnClickListener {
            checkLocationPermissionAndGetLocation()
        }
    }

    /**
     * permission checking: Before accessing location, we must check if the user has granted permission.
     * if permission is not granted, we request location permission at runtime.
     */
    private fun checkLocationPermissionAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // permission request: Trigger the system dialog to ask for location access.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // If we already have permission, go ahead and get the location.
        getLocation()
    }

    /**
     * Fused Location Provider: Gets a fresh location fix for the device.
     */
    private fun getLocation() {
        // Update status to show we are actively searching.
        statusText.text = "Status: Requesting location..."
        
        try {
            // getCurrentLocation is more reliable than lastLocation as it attempts to get a fresh fix
            // if the location cache is empty (which often happens on emulators or after reboots).
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location: Location? ->
                    // Handle the result: location can still be null if location services are disabled.
                    if (location != null) {
                        calculateDistanceAndDisplay(location)
                    } else {
                        // 5. If the location is null, inform the user.
                        statusText.text = "Status: Location unavailable"
                        distanceText.text = "Distance: N/A"
                        // Tip: Make sure Location Services (GPS) is turned on in device settings.
                    }
                }
                .addOnFailureListener {
                    // Handle location errors gracefully without crashing.
                    statusText.text = "Status: Error getting location"
                    distanceText.text = "Distance: N/A"
                }
        } catch (e: SecurityException) {
            // SecurityException could be thrown if permissions were revoked at the last second.
            statusText.text = "Status: Security error"
        }
    }

    /**
     * Calculates the distance and updates the UI.
     */
    private fun calculateDistanceAndDisplay(currentLocation: Location) {
        // creating the reference Location: We create a Location object for our target point.
        val referenceLocation = Location("reference").apply {
            latitude = REFERENCE_LATITUDE
            longitude = REFERENCE_LONGITUDE
        }

        // distanceTo(): This method calculates the distance in meters between two points.
        val distance = currentLocation.distanceTo(referenceLocation)

        // 9. Display the distance formatted to exactly 1 decimal place.
        distanceText.text = String.format("Distance: %.1f m", distance)

        // radius comparison: Compare calculated distance with our hardcoded radius.
        if (distance <= ZONE_RADIUS_METERS) {
            statusText.text = "Status: Inside Zone"
            findViewById<android.view.View>(R.id.rootLayout).setBackgroundColor(android.graphics.Color.GREEN)
        } else {
            statusText.text = "Status: Outside Zone"
            findViewById<android.view.View>(R.id.rootLayout).setBackgroundColor(android.graphics.Color.RED)
        }
    }

    /**
     * handle the permission result: This is called after the user responds to the permission request.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, proceed to get location.
                getLocation()
            } else {
                // 11. If permission is denied, inform the user.
                statusText.text = "Status: Permission denied"
            }
        }
    }
}
