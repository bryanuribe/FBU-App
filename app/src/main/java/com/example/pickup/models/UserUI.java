package com.example.pickup.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserUI {

    private static final String TAG = "UserUI";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULLNAME = "fullname";

    public UserUI() {

    }

    public void query() {

        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        //query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Issue getting user", e);
                    return;
                }
                Log.i(TAG, "done: " + user.get(0).toString());
            }
        });
    }

    // Getters
    public void getFullName() {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        String fullName = "";

    }

    // Setters


}
