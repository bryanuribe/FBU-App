package com.example.pickup.managers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class ProfileManager {

    private static final String TAG = "ProfileManager";
    
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_TEAM = "team";
    public static final String KEY_BIO = "bio";
    
    // Getter
    public static void setProfileFields(final EditText etFullname, final EditText etUsername, final EditText etTeam, final EditText etBio) {

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
    public static void saveProfile(final Context currentContext, ParseUser user, EditText etUsername, EditText etFullname, EditText etTeam, EditText etBio) {

        if (requiredFieldsEmpty(etUsername, etFullname)) {
            return;
        }
        // Get fields from text views
        String username = etUsername.getText().toString();
        String fullname = etFullname.getText().toString();
        String team = etTeam.getText().toString();
        String bio = etBio.getText().toString();

        // Update fields
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

    // Returns the File for a photo stored on disk given the fileName
    public static File getPhotoFileUri(Context currentContext, String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(currentContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    public static void launchCamera(Context currentContext, File photoFile, String photoFileName, int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(currentContext, photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(currentContext, "com.codepath.fileprovider.pickup", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(currentContext.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            //startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }
}
