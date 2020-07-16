package com.example.pickup.models;

public class UserUI {

    private static final String TAG = "UserUI";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_FULLNAME = "fullName";

    public UserUI() { }

    /*
    public String getFullName() {
        return getString("fullName");
    }

    // Getters
    public String getFullName() {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        String fullName = "";
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Issue getting user", e);
                    return;
                }
                ParseUser currentUser = users.get(0);
                String fullName = currentUser.getString("fullName");
            }
        });

        return fullName;
    }
*/
    // Setters


}
