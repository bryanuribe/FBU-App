package com.example.pickup;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("_User")
public class User extends ParseObject {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULLNAME = "fullName";

    public User() {}

    // Getters
    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public String getFullName() {
        return getString(KEY_FULLNAME);
    }

    // Setters

}
