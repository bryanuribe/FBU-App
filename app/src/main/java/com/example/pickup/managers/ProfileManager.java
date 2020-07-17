package com.example.pickup.managers;

import android.util.Log;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class ProfileManager {

    private static final String TAG = "ProfileManager";
    
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_TEAM = "team";
    public static final String KEY_BIO = "bio";
    
    // Getters
    public static void setProfileFields(final EditText etUsername, final EditText etFullname, final EditText etTeam, final EditText etBio) {

        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Issue getting user", e);
                    return;
                }
                ParseUser currentUser = user.get(0);
                etUsername.setText(currentUser.getString(KEY_USERNAME));
                etFullname.setText(currentUser.getString(KEY_FULLNAME));
                etTeam.setText(currentUser.getString(KEY_TEAM));
                etBio.setText(currentUser.getString(KEY_BIO));
            }
        });
    }

    // Setters
    public static void saveProfile(ParseUser user, EditText etUsername, EditText etFullname, EditText etTeam, EditText etBio) {

        // Get fields from text views
        String username = etUsername.getText().toString();
        String fullname = etFullname.getText().toString();
        String team = etTeam.getText().toString();
        String bio = etBio.getText().toString();

        user.put(KEY_USERNAME, username);
        user.put(KEY_FULLNAME, fullname);
        user.put(KEY_TEAM, team);
        user.put(KEY_BIO, bio);

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Error while saving", e);
                }
                Log.i(TAG, "done: Save user successful!");
            }
        });
    }
}
