package com.example.pickup.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pickup.R;
import com.example.pickup.managers.ComposeManager;
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
    private RadioGroup radioGroupSport;
    private RadioButton radioBtnSport;
    private RadioButton radioBtnSoccer;
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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        horizontalCalendar = MyCalendar.makeHorizontalCalendar(view.getRootView(), R.id.calendar);
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {}
        });
        
        etTime = view.findViewById(R.id.etTime);
        radioGroupSport = view.findViewById(R.id.radioGroupSport);
        radioGroupSport.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioBtnSport = view.findViewById(radioGroup.getCheckedRadioButtonId());

                Log.i(TAG, "onCheckedChanged: " + radioBtnSport.getText().toString());
            }
        });

        radioBtnSoccer = view.findViewById(R.id.radioBtnSoccer);
        // Set to default value of soccer
        radioBtnSport = view.findViewById(R.id.radioBtnSoccer);
        Log.i(TAG, "onViewCreated: " + radioBtnSport.getText().toString());

        etLocation = view.findViewById(R.id.etLocation);
        etNotes = view.findViewById(R.id.etNotes);

        btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if all fields filled out
                boolean allFieldsFilled = ComposeManager.allFieldsFilled(horizontalCalendar, etTime, etLocation, etNotes);

                if (allFieldsFilled) {

                    // Save event in database
                    ComposeManager.saveEvent(getContext(), horizontalCalendar, etTime, radioBtnSport, etLocation, etNotes);

                    // Clear text fields
                    ComposeManager.resetFields(horizontalCalendar, etTime, radioBtnSoccer, etLocation, etNotes);

                    // On success save to database submit to google maps

                    // On success submit to google map locally put pin on google maps

                    // Change camera location

                    // Handle errors if any one fails

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