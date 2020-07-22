package com.example.pickup;

import android.app.Application;

import com.example.pickup.models.ParseEvent;
import com.example.pickup.models.ParseUserToEvent;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(ParseEvent.class);
        ParseObject.registerSubclass(ParseUserToEvent.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("bryan-pickup") // should correspond to APP_ID env variable
                .clientKey("BryanAppGrind")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://bryan-pickup.herokuapp.com/parse/").build());
    }
}
