package com.example.pickup.managers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UserManager {

    private static final String TAG = "UserManager";

    int PERMISSION_ID = 42;;

    @SuppressLint("MissingPermission")
    public static void requestLocation(final FusedLocationProviderClient mFusedLocationClient, final Context context, final Activity activity) {

        // check if permissions are given
        if (UserManager.checkPermissions(context)) {

            // check if location is enabled
            if (UserManager.isLocationEnabled(context)) {

                // getting last location from FusedLocationClient object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            UserManager.requestNewLocationData(mFusedLocationClient, context);
                        }
                        else {
                            Log.i(TAG, "onComplete my Location: " + location.getLatitude());
                            Log.i(TAG, "onComplete myLocation: " + location.getLongitude());
                        }
                    }
                });
            }
            else {
                UserManager.requestUserLocationSettings(context);
            }
        }
        else {
            // if permissions aren't available, request for permissions
            UserManager.requestPermissions(activity, context, 42);
        }
    }

    public static void requestUserLocationSettings(Context context) {
        Toast.makeText(context, "Please turn on your location...", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    public static void requestNewLocationData(FusedLocationProviderClient mFusedLocationClient, Context context) {
        // Initializing LocationRequest object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, UserManager.mLocationCallback, Looper.myLooper());
    }

    private static LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            //Location mLastLocation = locationResult.getLastLocation();
            Log.i(TAG, "onLocationResult: callback" + locationResult);
        }
    };

    // method to check for permissions
    public static boolean checkPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // method to request for permissions
    public static void requestPermissions(Activity activity, Context context, int PERMISSION_ID) {
        ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_ID);
    }

    // method to check if location is enabled
    public static boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /*
    public static void updateUserLocation(Location location) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseGeoPoint currentLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        currentUser.put("location", currentLocation);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Error while saving", e);
                }
                Log.i(TAG, "done: Save event successful!");
            }
        });
    }

     */
}
