package com.example.pickup.managers;

import android.util.Log;

import com.example.pickup.adapters.TimelineAdapter;
import com.example.pickup.models.ParseEvent;
import com.example.pickup.models.ParseUserToEvent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TimelineManager {

    private static final String TAG = "TimelineManager";

    public static final String QUERY_TYPE_ALL = "all";
    public static final String QUERY_TYPE_USER_SPECIFIC = "userSpecific";

    public static final String AVAILABILITY_GOING = "Going";
    public static final String AVAILABILITY_MAYBE = "Maybe";
    public static final String AVAILABILITY_NO = "No";
    public static final String AVAILABILITY_NA = "NA";

    public static void queryEvents(final TimelineAdapter adapter, String queryType, String availability) {
        // Specify which class to query
        ParseQuery<ParseUserToEvent> query = ParseQuery.getQuery(ParseUserToEvent.class);
        query.include(ParseUserToEvent.KEY_USER);
        query.include(ParseUserToEvent.KEY_EVENT);
        query.addDescendingOrder(ParseUserToEvent.KEY_CREATED_AT);

        if (queryType == QUERY_TYPE_ALL) {}

        else if (queryType == QUERY_TYPE_USER_SPECIFIC) {
            query.whereEqualTo(ParseUserToEvent.KEY_USER, ParseUser.getCurrentUser());

            if (availability == AVAILABILITY_GOING) {
                query.whereEqualTo(ParseUserToEvent.KEY_AVAILABILITY, AVAILABILITY_GOING);
            }
            else if (availability == AVAILABILITY_MAYBE) {
                query.whereEqualTo(ParseUserToEvent.KEY_AVAILABILITY, AVAILABILITY_MAYBE);
            }
            else if (availability == AVAILABILITY_NO) {
                query.whereEqualTo(ParseUserToEvent.KEY_AVAILABILITY, AVAILABILITY_NO);
            }
        }

        query.findInBackground(new FindCallback<ParseUserToEvent>() {
            @Override
            public void done(List<ParseUserToEvent> userToEvents, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Issue getting posts", e);
                    return;
                }
                List<ParseEvent> events = new ArrayList<>();
                for (ParseUserToEvent userToEvent : userToEvents) {
                    Log.i(TAG, "done: " + userToEvent.getAvailability());
                    ParseEvent event = userToEvent.getEvent();
                    events.add(event);
                }
                adapter.clear();
                adapter.addAll(events);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
