package com.example.pickup.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserToEvent")
public class ParseUserToEvent extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_EVENT = "event";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String KEY_DISTANCE = "distance";

    // no-arg, empty constructor required for Parceler
    public ParseUserToEvent() {}

    // Getters
    public ParseUser getUser() {
        return getParseUser(KEY_USER) ;
    }
    public ParseEvent getEvent() {
        return (ParseEvent) getParseObject(KEY_EVENT);
    }
    public String getAvailability() {
        return getString(KEY_AVAILABILITY);
    }

    // Setters
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
    public void setEvent(ParseObject event) {
        put(KEY_EVENT, event);
    }
    public void setAvailability(String availability) {
        put(KEY_AVAILABILITY, availability);
    }
}
