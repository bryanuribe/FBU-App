package com.example.pickup.queryUserToEvents;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.example.pickup.managers.TimelineManager;
import com.example.pickup.models.ParseUserToEvent;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class NotifyChangeUserToEvents implements Runnable {

    private static final String TAG = "Callback";

    private MutableLiveData<List<Pair<ParseUserToEvent, Integer>>> mutable;
    private Future<List<Pair<ParseUserToEvent, Integer>>> futureUserToEvents;
    private Integer filterDistance;

    public NotifyChangeUserToEvents(Future<List<Pair<ParseUserToEvent, Integer>>> futureUserToEvents, MutableLiveData<List<Pair<ParseUserToEvent, Integer>>> mutable, Integer distance) {
        this.futureUserToEvents = futureUserToEvents;
        this.mutable = mutable;
        this.filterDistance = distance;
    }

    public void notifyUserToEvents() {
        try {
            Log.i(TAG, "complete: " + futureUserToEvents.get().get(0).first.getObjectId());
            mutable.postValue(TimelineManager.filterUserToEvents(futureUserToEvents.get(), filterDistance));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        notifyUserToEvents();
    }
}