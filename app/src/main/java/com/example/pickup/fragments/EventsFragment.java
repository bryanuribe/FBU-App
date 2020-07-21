package com.example.pickup.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickup.R;
import com.example.pickup.adapters.EventsAdapter;
import com.example.pickup.models.EventParse;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple Fragment subclass.
 */
public class EventsFragment extends Fragment {

    private static final String TAG = "EventsFragment";

    private RecyclerView rvEvents;
    protected EventsAdapter adapter;
    protected List<EventParse> allEvents;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvEvents = view.findViewById(R.id.rvEvents);

        // Create layout for one row in list
        // Create the adapter
        allEvents = new ArrayList<EventParse>();
        adapter = new EventsAdapter(getContext(), allEvents);

        // Create the data source
        // Set the adapter on the recycler view
        rvEvents.setAdapter(adapter);

        // Set the layout manager on the recycler view
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPosts();


    }
    protected void queryPosts(){
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
                    Log.i(TAG, "done: Post " + event.getTime() + ", Username " + event.getDate());
                }
                adapter.clear();
                allEvents.addAll(events);
                adapter.notifyDataSetChanged();
            }
        });
    }
}