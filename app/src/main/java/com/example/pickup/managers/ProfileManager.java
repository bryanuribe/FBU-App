package com.example.pickup.managers;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class ProfileManager {

    private static final String TAG = "ProfileManager";

    public static final String KEY_PROFILE_PICTURE = "profilePicture";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_TEAM = "team";
    public static final String KEY_BIO = "bio";
    
    // Getter
    public static void setProfileFields(final Context context, final ImageView ivProfilePic, final EditText etFullname, final EditText etUsername, final EditText etTeam, final EditText etBio) {

        // Create query to find fields equal to current user
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

                ParseFile image = currentUser.getParseFile(KEY_PROFILE_PICTURE);
                Glide.with(context).load(image.getUrl()).into(ivProfilePic);
                etFullname.setText(currentUser.getString(KEY_FULLNAME));
                etUsername.setText(currentUser.getString(KEY_USERNAME));
                etTeam.setText(currentUser.getString(KEY_TEAM));
                etBio.setText(currentUser.getString(KEY_BIO));
            }
        });
    }

    private static boolean requiredFieldsEmpty(EditText etUsername, EditText etFullname) {

        // Get fields from text views
        boolean usernameEmpty = etUsername.getText().toString().isEmpty();
        boolean fullnameEmpty = etFullname.getText().toString().isEmpty();

        // Username or Fullname not filled
        if (usernameEmpty || fullnameEmpty) {
            return true;
        }
        return false;
    }

    // Setter
    public static void saveProfile(final Context currentContext, ParseUser user, File photoFile, EditText etUsername, EditText etFullname, EditText etTeam, EditText etBio) {

        if (requiredFieldsEmpty(etUsername, etFullname)) {
            return;
        }
        // Get fields from text views
        String username = etUsername.getText().toString();
        String fullname = etFullname.getText().toString();
        String team = etTeam.getText().toString();
        String bio = etBio.getText().toString();

        // Update fields
        if (photoFile != null) {
            user.put(KEY_PROFILE_PICTURE, new ParseFile(photoFile));
        }
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
                Toast.makeText(currentContext, "Saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
