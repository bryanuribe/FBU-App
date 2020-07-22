package com.example.pickup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickup.R;
import com.example.pickup.adapters.TimelineAdapter;
import com.example.pickup.managers.TimelineManager;
import com.example.pickup.models.ParseEvent;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple Fragment subclass.
 */
public class TimelineFragment extends Fragment {

    private static final String TAG = "TimelineFragment";

    private TabLayout tabLayout;
    private RecyclerView rvEvents;
    protected TimelineAdapter adapter;
    protected List<ParseEvent> allEvents;

    public TimelineFragment() {
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
        allEvents = new ArrayList<ParseEvent>();
        adapter = new TimelineAdapter(getContext(), allEvents);

        // Create the data source
        // Set the adapter on the recycler view
        rvEvents.setAdapter(adapter);

        // Set the layout manager on the recycler view
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));

        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        TimelineManager.queryEvents(adapter, TimelineManager.QUERY_TYPE_ALL, TimelineManager.AVAILABILITY_NA);
                        break;
                    case 1:
                        TimelineManager.queryEvents(adapter, TimelineManager.QUERY_TYPE_USER_SPECIFIC, TimelineManager.AVAILABILITY_GOING);
                        break;
                    case 2:
                        TimelineManager.queryEvents(adapter, TimelineManager.QUERY_TYPE_USER_SPECIFIC, TimelineManager.AVAILABILITY_MAYBE);
                        break;
                    case 3:
                        TimelineManager.queryEvents(adapter, TimelineManager.QUERY_TYPE_USER_SPECIFIC, TimelineManager.AVAILABILITY_NO);
                        break;
                    case 4:
                    default:
                        TimelineManager.queryEvents(adapter, TimelineManager.QUERY_TYPE_USER_SPECIFIC, TimelineManager.AVAILABILITY_NA);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Default value of tab layout
        TimelineManager.queryEvents(adapter, TimelineManager.QUERY_TYPE_ALL, TimelineManager.AVAILABILITY_NA);
    }
}