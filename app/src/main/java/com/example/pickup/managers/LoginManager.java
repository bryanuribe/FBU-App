package com.example.pickup.managers;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pickup.navigation.Navigation;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginManager {

    private static final String TAG = "LoginManager";

    public static boolean checkUserLoggedin() {
        if (ParseUser.getCurrentUser() != null) {
            // Go from login activity to main activity
            return true;
        }
        return false;
    }

    public static void loginUser(final Activity currentActivity, EditText etUsername, EditText etPassword) {

        // Grab user inputted username and password
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        Log.i(TAG, "loginUser: " + username);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                // TODO: Better error handling
                if (e != null) {
                    Log.e(TAG, "done: Issue with login", e);
                    Toast.makeText(currentActivity, "Error, issue with login", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Navigate to the main activity if user logs in successfully
                Navigation.goMainActivity(currentActivity);
            }
        });
    }
}
