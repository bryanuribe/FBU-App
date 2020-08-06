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
import com.example.pickup.enums.Availability;
import com.example.pickup.enums.QueryType;
import com.example.pickup.enums.TimelineTab;
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

    @SuppressLint("MissingPermission")
    public static void updateData(final Context currentContext, final FusedLocationProviderClient mFusedLocationClient, final MutableLiveData<List<Pair<ParseUserToEvent, Integer>>> mutable, final TimelineTab currentTab) {

        Object[] queryParameters = getQueryParameters(currentTab);

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

    public static List<Pair<ParseUserToEvent, Integer>> filterUserToEvents(List<Pair<ParseUserToEvent, Integer>> userToEvents, final int filterDistance) {

        int currentIndex = userToEvents.size() -1;
        if (currentIndex == -1) {
            return userToEvents;
        }
        else {
            Pair<ParseUserToEvent, Integer> lastPair = userToEvents.get(currentIndex);
            Integer distance =  lastPair.second;
            Log.i(TAG, "filterUserToEvents: " + distance);
            while (distance > filterDistance) {
                userToEvents.remove(currentIndex);
                Log.i(TAG, "filterUserToEvents: " + currentIndex);
                --currentIndex;
                Log.i(TAG, "filterUserToEvents: " + currentIndex);
                if (currentIndex == -1){
                    break;
                }
                else {
                    lastPair = userToEvents.get(currentIndex);
                    distance = lastPair.second;
                }
            }
            Log.i(TAG, "filterUserToEvents: " + userToEvents.size());
            return userToEvents;
        }
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

    public static Object[] getQueryParameters(TimelineTab currentTab) {
        Object queryParameters[] = new Object[2];
        if (currentTab == TimelineTab.ALL) {
            queryParameters[0] = QueryType.USER;
            queryParameters[1] = Availability.NA;
        }
        else if (currentTab == TimelineTab.GOING) {
            queryParameters[0] = QueryType.USER;
            queryParameters[1] = Availability.GOING;
        }
        else if (currentTab == TimelineTab.MAYBE) {
            queryParameters[0] = QueryType.USER;
            queryParameters[1] = Availability.MAYBE;
        }
        else if (currentTab == TimelineTab.NO) {
            queryParameters[0] = QueryType.USER;
            queryParameters[1] = Availability.NO;
        }
        else {
            queryParameters[0] = QueryType.USER;
            queryParameters[1] = Availability.NA;
        }

        return queryParameters;
    }
}
