package com.example.pickup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pickup.R;
import com.example.pickup.managers.ProfileManager;
import com.example.pickup.models.EventParse;
import com.parse.ParseUser;

/**
 * A simple Fragment subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    EditText etFullname;
    EditText etUsername;
    EditText etTeam;
    EditText etBio;
    Button btnSave;

    EventParse event;
    ParseUser user;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etFullname = view.findViewById(R.id.etFullname);
        etUsername = view.findViewById(R.id.etUsername);
        etTeam = view.findViewById(R.id.etTeam);
        etBio = view.findViewById(R.id.etBio);

        event = new EventParse();
        user = ParseUser.getCurrentUser();

        ProfileManager.setProfileFields(etUsername, etFullname, etTeam, etBio);

        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Safe profile to database
                ProfileManager.saveProfile(getContext(), user, etFullname, etUsername, etTeam, etBio);
            }
        });
    }
}