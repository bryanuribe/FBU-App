package com.example.pickup.models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Event")
public class EventParse extends ParseObject {

    public static final String KEY_DATE = "eventDate";
    public static final String KEY_TIME = "eventTime";
    public static final String KEY_SPORT = "sport";
    public static final String KEY_FORMATTED_LOCATION = "formattedLocation";
    public static final String KEY_GEOPOINT_LOCATION = "geopointLocation";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_CREATED_AT = "createdAt";


    // no-arg, empty constructor required for Parceler
    public EventParse() {}

    // Getters
    public String getDate() {
        return getString(KEY_DATE);
    }
    public String getTime() {
        return getString(KEY_TIME);
    }
    public String getSport() {
        return getString(KEY_SPORT);
    }
    public String getFormattedLocation() {
        return getString(KEY_FORMATTED_LOCATION);
    }
    public ParseGeoPoint getGeopoint() {
        return getParseGeoPoint(KEY_GEOPOINT_LOCATION);
    }
    public String getNotes() {
        return getString(KEY_NOTES);
    }

    // Setters
    public void setDate(String date) {
        put(KEY_DATE, date);
    }
    public void setTime(String time) {
        put(KEY_TIME, time);
    }
    public void setSport(String sport) {
        put(KEY_SPORT, sport);
    }
    public void setFormattedLocation(String formattedLocation) {
        put(KEY_FORMATTED_LOCATION, formattedLocation);
    }
    public void setGeopointLocation(ParseGeoPoint geopointLocation) {
        put(KEY_GEOPOINT_LOCATION, geopointLocation);
    }
    public void setNotes(String notes) {
        put(KEY_NOTES, notes);
    }

}
