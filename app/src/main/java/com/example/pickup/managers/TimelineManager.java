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


    public static void queryAllEvents(final TimelineAdapter adapter) {
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
                    Log.i(TAG, "done: Event " + event.getTime() + ", Username " + event.getDate());
                }
                adapter.clear();
                adapter.addAll(events);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public static void queryGoingEvents(final TimelineAdapter adapter) {
        // Specify which class to query
        ParseQuery<ParseUserToEvent> query = ParseQuery.getQuery(ParseUserToEvent.class);
        query.include(ParseUserToEvent.KEY_USER);
        query.include(ParseUserToEvent.KEY_EVENT);
        query.addDescendingOrder(ParseUserToEvent.KEY_CREATED_AT);
        query.whereEqualTo(ParseUserToEvent.KEY_USER, ParseUser.getCurrentUser());
        query.whereEqualTo(ParseUserToEvent.KEY_AVAILABILITY, "Going");
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

    public static void queryMaybeEvents(final TimelineAdapter adapter) {
        // Specify which class to query
        ParseQuery<ParseUserToEvent> query = ParseQuery.getQuery(ParseUserToEvent.class);
        query.include(ParseUserToEvent.KEY_USER);
        query.include(ParseUserToEvent.KEY_EVENT);
        query.addDescendingOrder(ParseUserToEvent.KEY_CREATED_AT);
        query.whereEqualTo(ParseUserToEvent.KEY_USER, ParseUser.getCurrentUser());
        query.whereEqualTo(ParseUserToEvent.KEY_AVAILABILITY, "Maybe");
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

    public static void queryNoEvents(final TimelineAdapter adapter) {
        // Specify which class to query
        ParseQuery<ParseUserToEvent> query = ParseQuery.getQuery(ParseUserToEvent.class);
        query.include(ParseUserToEvent.KEY_USER);
        query.include(ParseUserToEvent.KEY_EVENT);
        query.addDescendingOrder(ParseUserToEvent.KEY_CREATED_AT);
        query.whereEqualTo(ParseUserToEvent.KEY_USER, ParseUser.getCurrentUser());
        query.whereEqualTo(ParseUserToEvent.KEY_AVAILABILITY, "No");
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

    public static void queryYourEvents(final TimelineAdapter adapter) {
        // Specify which class to query
        ParseQuery<ParseUserToEvent> query = ParseQuery.getQuery(ParseUserToEvent.class);
        query.include(ParseUserToEvent.KEY_USER);
        query.include(ParseUserToEvent.KEY_EVENT);
        query.addDescendingOrder(ParseUserToEvent.KEY_CREATED_AT);
        query.whereEqualTo(ParseUserToEvent.KEY_USER, ParseUser.getCurrentUser());
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
