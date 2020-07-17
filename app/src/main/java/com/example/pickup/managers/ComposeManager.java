package com.example.pickup.managers;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pickup.models.EventParse;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;

public class ComposeManager {

    private static final String TAG = "ComposeManager";

    public static final String KEY_DATE = "eventDate";
    public static final String KEY_TIME = "eventTime";
    public static final String KEY_SPORT = "sport";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_NOTES = "notes";

    public static boolean allFieldsFilled(HorizontalCalendar horizontalCalendar, EditText etTime, EditText etLocation, EditText etNotes) {

        Boolean timeEmpty = etTime.getText().toString().isEmpty();
        Boolean locationEmpty = etLocation.getText().toString().isEmpty();
        Boolean notesEmpty = etNotes.getText().toString().isEmpty();

        // All fields empty
        if (timeEmpty || locationEmpty || notesEmpty) {
            return false;
        }
        return true;
    }

    public static void saveEvent(final Context currentContext, final HorizontalCalendar horizontalCalendar, final EditText etTime, final EditText etLocation, final EditText etNotes) {

        EventParse newEvent = new EventParse();

        // Get fields from text views
        String year = String.valueOf(horizontalCalendar.getSelectedDate().get(Calendar.YEAR));
        String month = String.valueOf(horizontalCalendar.getSelectedDate().get(Calendar.MONTH));
        String day = String.valueOf(horizontalCalendar.getSelectedDate().get(Calendar.DAY_OF_MONTH));

        String date = month + "/" + day + "/" + year;
        String time = etTime.getText().toString();
        String notes = etNotes.getText().toString();

        // Update fields
        newEvent.put(KEY_DATE, date);
        newEvent.put(KEY_TIME, time);
        newEvent.put(KEY_NOTES, notes);

        newEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Error while saving", e);
                }
                Log.i(TAG, "done: Save user successful!");
                Toast.makeText(currentContext, "Event Created!", Toast.LENGTH_SHORT).show();
                clearTextFields(horizontalCalendar, etTime, etLocation, etNotes);
            }
        });

    }

    public static void clearTextFields (HorizontalCalendar horizontalCalendar, EditText etTime, EditText etLocation, EditText etNotes) {
        horizontalCalendar.goToday(false);
        etTime.setText("");
        etLocation.setText("");
        etNotes.setText("");
    }


}
