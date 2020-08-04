package com.example.pickup.managers;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.example.pickup.activities.LoginActivity;
import com.example.pickup.activities.MainActivity;
import com.example.pickup.activities.PreviewComposeActivity;
import com.example.pickup.activities.SignupActivity;

import java.util.concurrent.TimeUnit;

public class Navigation {

    private static final String TAG = "Navigation";

    public static void goMainActivity(Activity currentActivity) {
        Intent i = new Intent(currentActivity, MainActivity.class);
        currentActivity.startActivity(i);
        currentActivity.finish();
    }

    public static void goSignupActivity(Activity currentActivity) {
        Intent i = new Intent(currentActivity, SignupActivity.class);
        currentActivity.startActivity(i);
    }

    public static void goPreviewComposeScreen(Activity currentActivity) {
        Intent i = new Intent(currentActivity, PreviewComposeActivity.class);
        currentActivity.startActivity(i);
    }

    public static void goLoginActivity(FragmentActivity currentActivity) {
        Intent i = new Intent(currentActivity, LoginActivity.class);
        currentActivity.startActivity(i);
        currentActivity.finish();
    }

    public static void sleepApp(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
