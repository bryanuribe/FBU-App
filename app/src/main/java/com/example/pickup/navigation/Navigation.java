package com.example.pickup.navigation;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.pickup.activities.MainActivity;
import com.example.pickup.activities.PreviewComposeActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Navigation {

    private static final String TAG = "Navigation";

    public static void goMainActivity(Activity currentActivity) {
        Intent i = new Intent(currentActivity, MainActivity.class);
        currentActivity.startActivity(i);
        currentActivity.finish();
    }

    public static void goSignupActivity(Activity currentActivity) {
        Intent i = new Intent(currentActivity, MainActivity.class);
        currentActivity.startActivity(i);
        currentActivity.finish();
    }


    public static void goPreviewComposeScreen(Activity currentActivity) {
        Intent i = new Intent(currentActivity, PreviewComposeActivity.class);
        currentActivity.startActivity(i);
    }

    public static boolean checkUserLoggedin() {
        if (ParseUser.getCurrentUser() != null) {
            // Go from login activity to main activity
            return true;
        }
        return false;
    }

    public static void loginUser(final Activity currentActivity, String username, String password) {
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
                goMainActivity(currentActivity);
            }
        });
    }
}
