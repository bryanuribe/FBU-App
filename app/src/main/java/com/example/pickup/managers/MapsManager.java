package com.example.pickup.managers;

import android.util.Log;

import com.example.pickup.models.ParseEvent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.List;

public class MapsManager {

    private static final String TAG = "MapsManager";

    public static void populateGoogleMaps(final GoogleMap googleMap, String queryType) {
        // Specify which class to query
        ParseQuery<ParseEvent> query = ParseQuery.getQuery(ParseEvent.class);
        query.addDescendingOrder(ParseEvent.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseEvent>() {
            @Override
            public void done(List<ParseEvent> events, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Issue getting posts", e);
                    return;
                }
                for (ParseEvent event : events) {
                    Log.i(TAG, "done: Event geopoint " + event.getGeopoint());

                    // Add event to map
                    ParseGeoPoint geopoint = event.getGeopoint();
                    LatLng eventLocation = new LatLng(geopoint.getLatitude(), geopoint.getLongitude());
                    MarkerOptions marker = new MarkerOptions().position(eventLocation).title(event.getSport());
                    googleMap.addMarker(marker);
                }
            }
        });
    }
}
