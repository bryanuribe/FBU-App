package com.example.pickup.functionality;

import android.widget.EditText;

import devs.mulham.horizontalcalendar.HorizontalCalendar;

public class ComposeFunctionality {

    private static final String TAG = "ComposeFunctionality";

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
