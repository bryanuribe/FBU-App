package com.example.pickup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pickup.R;
import com.example.pickup.functionality.ComposeFunctionality;
import com.example.pickup.models.MyCalendar;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * A simple Fragment subclass.
 */
public class ComposeFragment extends Fragment {

    private static final String TAG = "ComposeFragment";

    private HorizontalCalendar horizontalCalendar;
    private EditText etTime;
    private RadioGroup radioSport;
    private EditText etLocation;
    private EditText etNotes;
    private Button btnNext;

    public ComposeFragment() {
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
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        horizontalCalendar = MyCalendar.makeHorizontalCalendar(view.getRootView(), R.id.calendar);
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {}
        });
        
        etTime = view.findViewById(R.id.etTime);
        etLocation = view.findViewById(R.id.etLocation);
        etNotes = view.findViewById(R.id.etNotes);

        btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if all fields filled out
                boolean allFieldsFilled = ComposeFunctionality.allFieldsFilled(horizontalCalendar, etTime, etLocation, etNotes);

                if (allFieldsFilled) {

                    // Save event in database

                    // On success submit to google maps

                    // On success locally put pin on google maps

                    // Change camera location

                    // Handle errors if any one fails

                    // Clear text fields

                    // TODO: preview screen
                    //Navigation.goPreviewComposeScreen(getActivity());
                }

                else {
                    Toast.makeText(getContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}