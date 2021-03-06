package com.example.pickup.queryUserToEvents;

import android.location.Location;
import android.util.Log;
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

        ArrayList<Pair<ParseUserToEvent, Integer>> userToEventsSorted = new ArrayList<>();

        for (ParseUserToEvent userToEvent : userToEvents) {
            int distance = findDistance(userLocation, userToEvent.getEvent().getGeopoint());
            Pair<ParseUserToEvent, Integer> pair = new Pair<>(userToEvent, distance);
            userToEventsSorted.add(pair);
        }

        mergeSort(userToEventsSorted, userToEventsSorted.size());

        return userToEventsSorted;
    }

    public static void mergeSort(ArrayList<Pair<ParseUserToEvent, Integer>> input, int size) {

        Log.i(TAG, "mergeSort: " + size);
        if (size <= 1) {
            return;
        }

        int mid = size / 2;

        ArrayList<Pair<ParseUserToEvent, Integer>> left = new ArrayList<>();
        ArrayList<Pair<ParseUserToEvent, Integer>> right = new ArrayList<>();

        for (int i = 0; i < mid; i++) {
            left.add(input.get(i));
        }
        for (int i = mid; i < size; i++) {
            right.add(input.get(i));
        }
        mergeSort(left, mid);
        mergeSort(right, size - mid);

        merge(input, left, right, mid, size - mid);
    }

    public static void merge(
            ArrayList<Pair<ParseUserToEvent, Integer>> input, ArrayList<Pair<ParseUserToEvent, Integer>> l, ArrayList<Pair<ParseUserToEvent, Integer>> r, int left, int right) {

        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l.get(i).second <= r.get(j).second) {
                input.set(k++, l.get(i++));
            }
            else {
                input.set(k++, r.get(j++));
            }
        }
        while (i < left) {
            input.set(k++, l.get(i++));
        }
        while (j < right) {
            input.set(k++, r.get(j++));
        }
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
