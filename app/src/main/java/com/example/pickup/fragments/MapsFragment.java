package com.example.pickup.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.pickup.R;
import com.example.pickup.activities.MarkerPopupWindow;
import com.example.pickup.enums.TimelineTab;
import com.example.pickup.managers.MapsManager;
import com.example.pickup.managers.UserManager;
import com.example.pickup.models.ParseUserToEvent;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment implements MarkerPopupWindow.MarkerDialogueListener {

    private static final String TAG = "MapsFragment";
    public static final String apiKey = "AIzaSyDAqSxvNJg6ricJ-kNRgkrqqOuvW8cf5z4";

    private GoogleMap map;

    UserManager userManager;

    private MutableLiveData mutable;
    Map<Marker, Pair<ParseUserToEvent, Integer>> markerToData;

    // initializing FusedLocationProviderClient object
    private FusedLocationProviderClient mFusedLocationClient;

    int PERMISSION_ID = 42;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(final GoogleMap googleMap) {

            map = googleMap;
            googleMap.clear();

            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    Pair<ParseUserToEvent, Integer> markerDetails = markerToData.get(marker);

                    openMarkerDialogue(markerDetails);
                }
            });

            // Move user to current location
            MapsManager.moveCameraToUser(map, mFusedLocationClient);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        // Initialize the SDK
        Places.initialize(getContext(), apiKey);

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                Log.i(TAG, "onPlaceSelected: " + place.getLatLng());
                LatLng placeLatLng = place.getLatLng();
                //Your LatLong
                LatLng Your_Location = new LatLng(placeLatLng.latitude, placeLatLng.longitude);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(Your_Location, 12));
            }

            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        markerToData = new HashMap<>();

        mutable = new MutableLiveData<>();
        mutable.setValue(null);
        mutable.observe(this, new Observer<List<Pair<ParseUserToEvent, Integer>>>() {
            @Override
            public void onChanged(List<Pair<ParseUserToEvent, Integer>> pairs) {
                MapsManager.populateGoogleMaps(map, pairs, markerToData);
            }
        });

        MapsManager.updateData(getContext(), mFusedLocationClient, mutable, TimelineTab.ALL);
    }

    private void openMarkerDialogue(Pair<ParseUserToEvent, Integer> markerDetails) {

        Intent i = new Intent(getActivity(), MarkerPopupWindow.class);

        Log.i(TAG, "openMarkerDialogue: " + markerDetails.first);
        Log.i(TAG, "openMarkerDialogue: " + markerToData.size());

        // serialize the post using parceler, use its short name as a key
        i.putExtra("markerDetails", Parcels.wrap(markerDetails.first));
        i.putExtra("distance", markerDetails.second);

        startActivity(i);
    }

    @Override
    public void applyUserAction(MarkerPopupWindow markerDialogue) {
        Log.i(TAG, "applyUserAction: ");
        Log.i(TAG, "applyUserAction: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update Data
        //MapsManager.updateData(getContext(), mFusedLocationClient, mutable, TimelineTab.ALL);
        if (userManager.checkPermissions(getContext())) {
            //getLastLocation();
        }
    }

    // result if everything is okay
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //getLastLocation();
            }
        }
    }
}

