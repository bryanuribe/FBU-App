package com.example.pickup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pickup.CountListener;
import com.example.pickup.R;
import com.example.pickup.adapters.TimelineAdapter;
import com.example.pickup.managers.TimelineManager;
import com.example.pickup.models.ParseUserToEvent;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple Fragment subclass.
 */
public class TimelineFragment extends Fragment {

    private static final String TAG = "TimelineFragment";

    public static final String QUERY_ALL = "all";
    public static final String QUERY_USER = "userSpecific";

    public static final String AVAILABILITY_GOING = "Going";
    public static final String AVAILABILITY_MAYBE = "Maybe";
    public static final String AVAILABILITY_NO = "No";
    public static final String AVAILABILITY_NA = "NA";

    private TabLayout tabLayout;
    private RecyclerView rvEvents;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvTimelineStatus;
    protected TimelineAdapter adapter;
    protected List<ParseUserToEvent> userToEvents;

    private CountListener countListener;

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
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvEvents = view.findViewById(R.id.rvEvents);
        tvTimelineStatus = view.findViewById(R.id.tvTimelineStatus);

        // Create layout for one row in list
        // Create the adapter
        userToEvents = new ArrayList<>();
        adapter = new TimelineAdapter(getContext(), userToEvents);

        // Create the data source
        // Set the adapter on the recycler view
        rvEvents.setAdapter(adapter);
        rvEvents.setItemAnimator(new DefaultItemAnimator());

        // Set the layout manager on the recycler view
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));

        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        userToEvents = TimelineManager.queryUserToEvents(QUERY_ALL, AVAILABILITY_NA);
                        TimelineManager.populateTimeline(adapter, userToEvents, tvTimelineStatus);
                        break;
                    case 1:
                        userToEvents = TimelineManager.queryUserToEvents(QUERY_USER, AVAILABILITY_GOING);
                        TimelineManager.populateTimeline(adapter, userToEvents, tvTimelineStatus);
                        break;
                    case 2:
                        userToEvents = TimelineManager.queryUserToEvents(QUERY_USER, AVAILABILITY_MAYBE);
                        TimelineManager.populateTimeline(adapter, userToEvents, tvTimelineStatus);
                        break;
                    case 3:
                        userToEvents = TimelineManager.queryUserToEvents(QUERY_USER, AVAILABILITY_NO);
                        TimelineManager.populateTimeline(adapter, userToEvents, tvTimelineStatus);
                        break;
                    case 4:
                        userToEvents = TimelineManager.queryUserToEvents(QUERY_USER, AVAILABILITY_NA);
                        TimelineManager.populateTimeline(adapter, userToEvents, tvTimelineStatus);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        userToEvents = TimelineManager.queryUserToEvents(QUERY_ALL, AVAILABILITY_NA);
                        TimelineManager.populateTimeline(adapter, userToEvents, tvTimelineStatus);
                        break;
                    case 1:
                        userToEvents = TimelineManager.queryUserToEvents(QUERY_USER, AVAILABILITY_GOING);
                        TimelineManager.populateTimeline(adapter, userToEvents, tvTimelineStatus);
                        break;
                    case 2:
                        userToEvents = TimelineManager.queryUserToEvents(QUERY_USER, AVAILABILITY_MAYBE);
                        TimelineManager.populateTimeline(adapter, userToEvents, tvTimelineStatus);
                        break;
                    case 3:
                        userToEvents = TimelineManager.queryUserToEvents(QUERY_USER, AVAILABILITY_NO);
                        TimelineManager.populateTimeline(adapter, userToEvents, tvTimelineStatus);
                        break;
                    case 4:
                        userToEvents = TimelineManager.queryUserToEvents(QUERY_USER, AVAILABILITY_NA);
                        TimelineManager.populateTimeline(adapter, userToEvents, tvTimelineStatus);
                        break;
                }
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Default value of tab layout
        userToEvents = TimelineManager.queryUserToEvents(QUERY_ALL, AVAILABILITY_NA);
        TimelineManager.populateTimeline(adapter, userToEvents, tvTimelineStatus);
    }
}