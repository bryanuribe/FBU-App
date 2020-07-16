package com.example.pickup.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pickup.R;
import com.example.pickup.models.UserUI;
import com.example.pickup.parse.EventParse;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

/**
 * A simple Fragment subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    EditText etFullname;
    EditText etUsername;
    Button btnSave;

    EventParse event;
    UserUI user;

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

        event = new EventParse();

        user = new UserUI();

        user.query();

        ParseQuery<EventParse> query = ParseQuery.getQuery(EventParse.class);
        query.findInBackground(new FindCallback<EventParse>() {
            @Override
            public void done(List<EventParse> events, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Issue getting user", e);
                    return;
                }

                //Log.i(TAG, "done: " + events.get(0).getTime());
            }
        });


        Log.i(TAG, "onViewCreated: " + query.toString());

        //etUsername.setText(user.getUsername());
        //etFullname.setText(user.getFullname());

        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}