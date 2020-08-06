package com.example.pickup.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.pickup.enums.Availability;
import com.example.pickup.enums.QueryType;
import com.example.pickup.enums.TimelineTab;
import com.example.pickup.models.ParseEvent;
import com.example.pickup.models.ParseUserToEvent;
import com.example.pickup.queryUserToEvents.NotifyChangeUserToEvents;
import com.example.pickup.queryUserToEvents.QueryUserToEvents;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.parse.ParseGeoPoint;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MapsManager {

    private static final String TAG = "MapsManager";

    public static void populateGoogleMaps(final GoogleMap googleMap, final List<Pair<ParseUserToEvent, Integer>> userToEventsPair, final Map<Marker, Pair<ParseUserToEvent, Integer>> markerToData) {

        if (userToEventsPair == null) {
            return;
        }

        for (Pair<ParseUserToEvent, Integer> userToEventPair : userToEventsPair) {

            ParseUserToEvent userToEvent = userToEventPair.first;
            ParseEvent event = userToEvent.getEvent();
            Log.i(TAG, "done: Event geopoint " + event.getGeopoint());

            // Add event to map
            ParseGeoPoint geopoint = event.getGeopoint();
            LatLng eventLocation = new LatLng(geopoint.getLatitude(), geopoint.getLongitude());

            int bitmapColor = getMarkerColor(userToEvent.getAvailability());
            BitmapDescriptor markerColor = BitmapDescriptorFactory.defaultMarker(bitmapColor);
            MarkerOptions markerOptions = new MarkerOptions().position(eventLocation).title(event.getSport()).icon(markerColor);
            Marker newMarker = googleMap.addMarker(markerOptions);
            markerToData.put(newMarker, userToEventPair);
        }
        Log.i(TAG, "populateGoogleMaps: " + markerToData.size());
    }

    private static int getMarkerColor(String availability) {
        int bitmapGreen = 110;
        int bitmapYellow = 50;
        int bitmapRed = 0;
        int bitmapDefault = 320;

        Log.i(TAG, "getMarkerColor: " + availability);

        if (availability.equals(Availability.GOING.text())) {
            return bitmapGreen;
        }
        else if (availability.equals(Availability.MAYBE.text())) {
            return bitmapYellow;
        }
        else if (availability.equals(Availability.NO.text())) {
            return bitmapRed;
        }
        else {
            return bitmapDefault;
        }
    }

    @SuppressLint("MissingPermission")
    public static void updateData(final Context currentContext, final FusedLocationProviderClient mFusedLocationClient, final MutableLiveData<List<Pair<ParseUserToEvent, Integer>>> mutable, final TimelineTab currentTab) {

        Object[] queryParameters = TimelineManager.getQueryParameters(currentTab);

        final QueryType queryType = (QueryType) queryParameters[0];
        final Availability availability = (Availability) queryParameters[1];

        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location == null) {
                    UserManager.requestNewLocationData(mFusedLocationClient, currentContext);
                }
                else {
                    Log.i(TAG, "onComplete: " + location.getLongitude());
                    ExecutorService executor = Executors.newFixedThreadPool(3);
                    Callable<List<Pair<ParseUserToEvent, Integer>>> queryUserToEvents = new QueryUserToEvents(queryType, availability, location);
                    Future<List<Pair<ParseUserToEvent, Integer>>> futureUserToEvents = executor.submit(queryUserToEvents);
                    Runnable getQuery = new NotifyChangeUserToEvents(futureUserToEvents, mutable);
                    executor.submit(getQuery);
                    executor.shutdown();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    public static void moveCameraToUser(final GoogleMap map, FusedLocationProviderClient mFusedLocationClient) {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            }
        });
    }
}
