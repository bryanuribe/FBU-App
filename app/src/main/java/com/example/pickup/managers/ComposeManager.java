package com.example.pickup.managers;

import android.widget.EditText;

import devs.mulham.horizontalcalendar.HorizontalCalendar;

public class ComposeManager {

    private static final String TAG = "ComposeManager";

    public static boolean allFieldsFilled(HorizontalCalendar horizontalCalendar, EditText etTime, EditText etLocation, EditText etNotes) {

        // date =
        Boolean timeEmpty = etTime.getText().toString().isEmpty();
        Boolean locationEmpty = etLocation.getText().toString().isEmpty();
        Boolean notesEmpty = etNotes.getText().toString().isEmpty();

        // All fields empty
        if (timeEmpty || locationEmpty || notesEmpty) {
            return false;
        }
        return true;
    }




}
