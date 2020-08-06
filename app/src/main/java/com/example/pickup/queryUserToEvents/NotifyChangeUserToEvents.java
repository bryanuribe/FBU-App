package com.example.pickup.queryUserToEvents;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.example.pickup.models.ParseUserToEvent;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class NotifyChangeUserToEvents implements Runnable {

    private static final String TAG = "Callback";

    private MutableLiveData<List<Pair<ParseUserToEvent, Integer>>> mutable;
    private Future<List<Pair<ParseUserToEvent, Integer>>> futureUserToEvents;

    public NotifyChangeUserToEvents(Future<List<Pair<ParseUserToEvent, Integer>>> futureUserToEvents, MutableLiveData<List<Pair<ParseUserToEvent, Integer>>> mutable) {
        this.futureUserToEvents = futureUserToEvents;
        this.mutable = mutable;
    }

    public void notifyUserToEvents() {
        try {
            mutable.postValue(futureUserToEvents.get());
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