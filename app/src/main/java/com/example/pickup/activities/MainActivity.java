package com.example.pickup.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pickup.R;
import com.example.pickup.fragments.ComposeFragment;
import com.example.pickup.fragments.MapsFragment;
import com.example.pickup.fragments.ProfileFragment;
import com.example.pickup.fragments.TimelineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Fragment currentFragment;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        switchContent("mapsFragment");
                        break;
                    case R.id.action_list:
                        switchContent("timelineFragment");
                        break;
                    case R.id.action_compose:
                        switchContent("composeFragment");
                        break;
                    case R.id.action_profile:
                        switchContent("profileFragment");
                        break;
                }
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    public void switchContent(String fragmentTag) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment nextFragment = fragmentManager.findFragmentByTag(fragmentTag);

        Log.i(TAG, "switchContent: " + nextFragment);
        Log.i(TAG, "switchContent: " + currentFragment);

        if (nextFragment != null) {
            if (currentFragment != null) {
                transaction.detach(currentFragment).attach(nextFragment).commit();
            }
            else {
                transaction.attach(nextFragment).commit();
            }
            currentFragment = nextFragment;
        }

        else {
            if (fragmentTag.equals("mapsFragment")) {
                nextFragment = new MapsFragment();
            } else if (fragmentTag.equals("timelineFragment")) {
                nextFragment = new TimelineFragment();
            } else if (fragmentTag.equals("composeFragment")) {
                nextFragment = new ComposeFragment();
            } else if (fragmentTag.equals("profileFragment")) {
                nextFragment = new ProfileFragment();
            }
            if (currentFragment != null) {
                transaction.detach(currentFragment).add(R.id.flContainer, nextFragment, fragmentTag).commit();
            }
            else {
                transaction.add(R.id.flContainer, nextFragment, fragmentTag).commit();
            }
            currentFragment = nextFragment;
        }
    }
}