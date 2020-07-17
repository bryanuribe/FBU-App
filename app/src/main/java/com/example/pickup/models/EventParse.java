package com.example.pickup.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Event")
public class EventParse extends ParseObject {

    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "eventTime";
    public static final String KEY_SPORT = "sport";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_NOTES = "eventNotes";


    // no-arg, empty constructor required for Parceler
    public EventParse() {}

    public String getTime() {
        return getString(KEY_TIME);
    }
    public String getNotes() {
        return getString(KEY_NOTES);
    }

    public void setTime(String time) { put(KEY_TIME, time); }
    public void setNotes(String notes) { put(KEY_NOTES, notes); }

}
