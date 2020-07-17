package com.example.pickup.navigation;

import android.app.Activity;
import android.content.Intent;

import com.example.pickup.activities.MainActivity;
import com.example.pickup.activities.PreviewComposeActivity;

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

}
