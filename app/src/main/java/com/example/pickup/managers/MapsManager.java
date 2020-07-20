package com.example.pickup.managers;

import android.util.Log;

import com.example.pickup.models.EventParse;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MapsManager {

    private static final String TAG = "MapsManager";

    public static void queryEvents() {
        // Specify which class to query
        ParseQuery<EventParse> query = ParseQuery.getQuery(EventParse.class);
        query.addDescendingOrder(EventParse.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<EventParse>() {
            @Override
            public void done(List<EventParse> events, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Issue getting posts", e);
                    return;
                }
                for (EventParse event : events) {
                    Log.i(TAG, "done: Event geopoint " + event.getGeopoint());
                }
            }
        });
    }
}
