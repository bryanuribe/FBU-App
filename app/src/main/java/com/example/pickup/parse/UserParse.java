package com.example.pickup.parse;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("_User")
public class UserParse extends ParseUser {

    private static final String TAG = "UserUI";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULLNAME = "fullname";

    public UserParse() {

    }

    public void query() {
        ParseQuery<UserParse> query = ParseQuery.getQuery(UserParse.class);
        query.include("username");
        query.include("fullname");
        query.findInBackground(new FindCallback<UserParse>() {
            @Override
            public void done(List<UserParse> user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Issue getting user", e);
                    return;
                }
                Log.i(TAG, "done: " + user.toString());
                getString("username");
                Log.i(TAG, "done: " + getString("username"));
                Log.i(TAG, "done: " + getString("fullname"));
                Log.i(TAG, "done: ");
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
