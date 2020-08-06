package com.example.pickup.queryUserToEvents;

import android.location.Location;
import android.util.Pair;

import com.example.pickup.enums.Availability;
import com.example.pickup.enums.QueryType;
import com.example.pickup.models.ParseUserToEvent;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class QueryUserToEvents implements Callable<List<Pair<ParseUserToEvent, Integer>>> {

    private static final String TAG = "QueryUserToEvents";

    private QueryType queryType;
    private Availability availability;
    private Location userLocation;

    public QueryUserToEvents(QueryType queryType, Availability availability, Location userLocation) {
        this.queryType = queryType;
        this.availability = availability;
        this.userLocation = userLocation;
    }

    private List<Pair<ParseUserToEvent, Integer>> queryUserToEvents() {

        List<ParseUserToEvent> userToEvents;

        // Specify which class to query
        final ParseQuery<ParseUserToEvent> query = ParseQuery.getQuery(ParseUserToEvent.class);
        query.include(ParseUserToEvent.KEY_USER);
        query.include(ParseUserToEvent.KEY_EVENT);

        if (queryType == QueryType.ALL) {
        }

        else if (queryType == QueryType.USER) {
            query.whereEqualTo(ParseUserToEvent.KEY_USER, ParseUser.getCurrentUser());
            if (availability != Availability.NA) {
                query.whereEqualTo(ParseUserToEvent.KEY_AVAILABILITY, availability.text());
            }
        }

        try {
            userToEvents = query.find();
        } catch (ParseException e) {
            userToEvents = new ArrayList<>();
            e.printStackTrace();
        }

        return sortByDistanceToEvent(userLocation, userToEvents);
    }

    public static List<Pair<ParseUserToEvent, Integer>> sortByDistanceToEvent(Location userLocation, List<ParseUserToEvent> userToEvents) {

        List<Pair<ParseUserToEvent, Integer>> userToEventsUnsorted = new ArrayList<>();

        for (ParseUserToEvent userToEvent : userToEvents) {
            int distance = findDistance(userLocation, userToEvent.getEvent().getGeopoint());
            Pair<ParseUserToEvent, Integer> pair = new Pair<>(userToEvent, distance);
            userToEventsUnsorted.add(pair);
        }

        return mergeSort(userToEventsUnsorted);
    }

    public static List<Pair<ParseUserToEvent, Integer>> mergeSort(List<Pair<ParseUserToEvent, Integer>> userToEvents) {
        return userToEvents;
    }

    public int compare(Pair p1, Pair p2) {
        Integer p1Second = (Integer) p1.second;
        Integer p2Second = (Integer) p2.second;

        return p1Second.compareTo(p2Second);
    }

    public static int findDistance(Location userLocation, ParseGeoPoint eventLocation) {

        double lon1 = Math.toRadians(userLocation.getLongitude());
        double lon2 = Math.toRadians(eventLocation.getLongitude());
        double lat1 = Math.toRadians(userLocation.getLatitude());
        double lat2 = Math.toRadians(eventLocation.getLatitude());

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in miles
        double r = 3956;

        return (int) Math.ceil(c * r);

    }

    @Override
    public List<Pair<ParseUserToEvent, Integer>> call() throws Exception {
        return queryUserToEvents();
    }
}
