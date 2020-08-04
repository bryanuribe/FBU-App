package com.example.pickup.fragments;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pickup.R;
import com.example.pickup.adapters.TimelineAdapter;
import com.example.pickup.dialogues.FilterDialogue;
import com.example.pickup.enums.TimelineTab;
import com.example.pickup.managers.TimelineManager;
import com.example.pickup.models.ParseUserToEvent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple Fragment subclass.
 */
public class TimelineFragment extends Fragment implements FilterDialogue.FilterDialogueListener {

    private static final String TAG = "TimelineFragment";

    private Button btnFilter;
    private TabLayout tabLayout;
    private RecyclerView rvEvents;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvTimelineStatus;
    private MutableLiveData<List<Pair<ParseUserToEvent, Integer>>> mutable;
    private int filterDistance;
    private TimelineTab currentTab;
    protected TimelineAdapter adapter;
    protected List<Pair<ParseUserToEvent, Integer>> userToEvents;

    // initializing FusedLocationProviderClient object
    private FusedLocationProviderClient mFusedLocationClient;
    private Location currentLocation;
    private int PERMISSION_RESULT = 42;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        // default values
        filterDistance = 100;
        currentTab = TimelineTab.ALL;

        btnFilter = view.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilterDialogue();
            }
        });

        rvEvents = view.findViewById(R.id.rvEvents);
        tvTimelineStatus = view.findViewById(R.id.tvTimelineStatus);

        // Create the adapter
        userToEvents = new ArrayList<>();
        adapter = new TimelineAdapter(getContext(), userToEvents);

        mutable = new MutableLiveData<>();
        mutable.setValue(null);
        mutable.observe(this, new Observer<List<Pair<ParseUserToEvent, Integer>>>() {
            @Override
            public void onChanged(List<Pair<ParseUserToEvent, Integer>> pairs) {
                adapter.clear();
                if (pairs != null) {
                    adapter.addAll(pairs);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                TimelineManager.setTimelineStatus(adapter, tvTimelineStatus);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                if (positionStart == 0 && itemCount == 1) {
                    TimelineManager.setTimelineStatus(adapter, tvTimelineStatus);
                }
            }
        };

        adapter.registerAdapterDataObserver(dataObserver);

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
                    default:
                        currentTab = TimelineTab.ALL;
                        break;
                    case 1:
                        currentTab = TimelineTab.GOING;
                        break;
                    case 2:
                        currentTab = TimelineTab.MAYBE;
                        break;
                    case 3:
                        currentTab = TimelineTab.NO;
                        break;
                    case 4:
                        currentTab = TimelineTab.YOUR_EVENTS;
                        break;
                }
                Log.i(TAG, "onTabSelected: " + currentTab);
                TimelineManager.updateUserToEvents(getContext(), mFusedLocationClient, mutable, currentTab, filterDistance);
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
                TimelineManager.updateUserToEvents(getContext(), mFusedLocationClient, mutable, currentTab, filterDistance);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        TimelineManager.updateUserToEvents(getContext(), mFusedLocationClient, mutable, currentTab, filterDistance);
    }

    private void openFilterDialogue() {
        FilterDialogue filterDialogue = new FilterDialogue(filterDistance);
        filterDialogue.setTargetFragment(this, 0);
        filterDialogue.show(getActivity().getSupportFragmentManager(), "Filter Dialogue");
    }

    @Override
    public void onDialogPositiveClick(FilterDialogue dialog, int distance) {
        Log.i(TAG, "onDialogPositiveClick: " + distance);
        filterDistance = distance;
        TimelineManager.updateUserToEvents(getContext(), mFusedLocationClient, mutable, currentTab, filterDistance);
    }

    @Override
    public void onDialogNegativeClick(FilterDialogue dialog) {

    }
}