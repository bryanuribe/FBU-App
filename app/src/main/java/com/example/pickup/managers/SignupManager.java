package com.example.pickup.managers;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

import com.example.pickup.navigation.Navigation;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupManager {

    private static final String TAG = "SignupManager";

    public static void signupUser(final Activity currentActivity, EditText etFullname, EditText etUsername, EditText etPassword) {

        String fullname = etFullname.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        // Create the ParseUser
        ParseUser user = new ParseUser();

        // Set core properties
        user.setUsername(username);
        user.setPassword(password);

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Navigation.goMainActivity(currentActivity);
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.i(TAG, "done: Signup failed");
                }
            }
        });
    }
}
