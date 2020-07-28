package com.example.pickup.managers;

import android.view.View;
import android.widget.TextView;

import com.example.pickup.adapters.TimelineAdapter;
import com.example.pickup.models.ParseUserToEvent;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TimelineManager {

    private static final String TAG = "TimelineManager";

    public static final String QUERY_ALL = "all";
    public static final String QUERY_USER = "userSpecific";

    public static final String AVAILABILITY_GOING = "Going";
    public static final String AVAILABILITY_MAYBE = "Maybe";
    public static final String AVAILABILITY_NO = "No";
    public static final String AVAILABILITY_NA = "NA";

    public static List<ParseUserToEvent> queryUserToEvents(String queryType, String availability) {

        List<ParseUserToEvent> queryUserToEvents = new ArrayList<>();

        // Specify which class to query
        final ParseQuery<ParseUserToEvent> query = ParseQuery.getQuery(ParseUserToEvent.class);
        query.include(ParseUserToEvent.KEY_USER);
        query.include(ParseUserToEvent.KEY_EVENT);
        query.addDescendingOrder(ParseUserToEvent.KEY_CREATED_AT);

        if (queryType == QUERY_ALL) {}

        else if (queryType == QUERY_USER) {
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
        try {
            queryUserToEvents = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return queryUserToEvents;
    }

    public static void populateTimeline(TimelineAdapter adapter, List<ParseUserToEvent> userToEvents, TextView tvTimelineStatus) {
        setTimelineStatus(userToEvents, tvTimelineStatus);
        adapter.clear();
        adapter.addAll(userToEvents);
        adapter.notifyDataSetChanged();
    }

    public static void setTimelineStatus(List<ParseUserToEvent> userToEvents, TextView tvTimelineStatus) {
        if (userToEvents.size() == 0) {
            tvTimelineStatus.setVisibility(View.VISIBLE);
        }
        else {
            tvTimelineStatus.setVisibility(View.INVISIBLE);
        }
    }
}
