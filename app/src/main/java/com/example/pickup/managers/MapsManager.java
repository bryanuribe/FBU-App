package com.example.pickup.managers;

import android.util.Log;

import com.example.pickup.models.EventParse;
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
        ParseQuery<EventParse> query = ParseQuery.getQuery(EventParse.class);
        query.addDescendingOrder(EventParse.KEY_CREATED_AT);
        if (queryType == "UserEvents") {
            //query.whereEqualTo(ParseUser.getCurrentUser(), EventParse.getCreationUser());
        }
        query.findInBackground(new FindCallback<EventParse>() {
            @Override
            public void done(List<EventParse> events, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Issue getting posts", e);
                    return;
                }
                for (EventParse event : events) {
                    Log.i(TAG, "done: Event geopoint " + event.getGeopoint());

                    // Add event to map
                    ParseGeoPoint geopoint = event.getGeopoint();
                    LatLng eventLocation = new LatLng(geopoint.getLatitude(), geopoint.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(eventLocation).title("Event"));
                }
            }
        });
    }
}
