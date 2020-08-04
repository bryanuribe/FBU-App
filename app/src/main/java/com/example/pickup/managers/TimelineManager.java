package com.example.pickup.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.pickup.adapters.TimelineAdapter;
import com.example.pickup.models.ParseUserToEvent;
import com.example.pickup.queryUserToEvents.NotifyChangeUserToEvents;
import com.example.pickup.queryUserToEvents.QueryUserToEvents;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TimelineManager {

    private static final String TAG = "TimelineManager";

    public static final String QUERY_ALL = "all";
    public static final String QUERY_USER = "userSpecific";

    public static final String AVAILABILITY_GOING = "Going";
    public static final String AVAILABILITY_MAYBE = "Maybe";
    public static final String AVAILABILITY_NO = "No";
    public static final String AVAILABILITY_NA = "NA";

    @SuppressLint("MissingPermission")
    public static void updateUserToEvents(final Context currentContext, final FusedLocationProviderClient mFusedLocationClient, final MutableLiveData<List<Pair<ParseUserToEvent, Integer>>> mutable, final String currentTab, final Integer filterDistance) {

        String[] queryParameters = getQueryParameters(currentTab);

        final String queryType = queryParameters[0];
        final String availability = queryParameters[1];

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
                    Runnable getQuery = new NotifyChangeUserToEvents(futureUserToEvents, mutable, filterDistance);
                    executor.submit(getQuery);
                    executor.shutdown();
                }
            }
        });
    }

    public static List<Pair<ParseUserToEvent, Integer>> filterUserToEvents(List<Pair<ParseUserToEvent, Integer>> userToEvents, final int filterDistance) {
        Pair<ParseUserToEvent, Integer> lastPair = userToEvents.get(userToEvents.size() -1);
        Log.i(TAG, "filterUserToEvents: " + lastPair.second.toString());
        Integer distance =  lastPair.second;

        while (distance > filterDistance) {
            lastPair = userToEvents.get(userToEvents.size() -2);
            distance = lastPair.second;
            userToEvents.remove(userToEvents.size() -1);
        }

        return userToEvents;
    }

    public static void setTimelineStatus(TimelineAdapter adapter, TextView tvTimelineStatus) {
        if (adapter.getItemCount() == 0) {
            tvTimelineStatus.setVisibility(View.VISIBLE);
            Log.i(TAG, "setTimelineStatus: set visible");
        }
        else {
            tvTimelineStatus.setVisibility(View.INVISIBLE);
            Log.i(TAG, "setTimelineStatus: set invisible");
        }
    }

    private static String[] getQueryParameters(String currentTab) {
        String queryParameters[] = new String[2];
        if (currentTab.equals("ALL")) {
            queryParameters[0] = QUERY_ALL;
            queryParameters[1] = AVAILABILITY_NA;
        }
        else if (currentTab.equals("GOING")) {
            queryParameters[0] = QUERY_USER;
            queryParameters[1] = AVAILABILITY_GOING;
        }
        else if (currentTab.equals("MAYBE")) {
            queryParameters[0] = QUERY_USER;
            queryParameters[1] = AVAILABILITY_MAYBE;
        }
        else if (currentTab.equals("NO")) {
            queryParameters[0] = QUERY_USER;
            queryParameters[1] = AVAILABILITY_NO;
        }
        else {
            queryParameters[0] = QUERY_USER;
            queryParameters[1] = AVAILABILITY_NA;
        }

        return queryParameters;
    }
}
