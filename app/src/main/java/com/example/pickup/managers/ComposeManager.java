package com.example.pickup.managers;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.pickup.models.ParseEvent;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import okhttp3.Headers;

public class ComposeManager {

    private static final String TAG = "ComposeManager";

    public static final String GEOPOINT_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    public static final String API_KEY = "&key=AIzaSyDAqSxvNJg6ricJ-kNRgkrqqOuvW8cf5z4";

    public static boolean allFieldsFilled(HorizontalCalendar horizontalCalendar, EditText etTime, EditText etLocation, EditText etNotes) {

        Boolean timeEmpty = etTime.getText().toString().isEmpty();
        Boolean locationEmpty = etLocation.getText().toString().isEmpty();
        Boolean notesEmpty = etNotes.getText().toString().isEmpty();

        // Any fields empty
        if (timeEmpty || locationEmpty || notesEmpty) {
            return false;
        }
        return true;
    }

    public static void saveEvent(final Context currentContext, final HorizontalCalendar horizontalCalendar, final EditText etTime, RadioButton radioBtnSport, final EditText etLocation, final EditText etNotes) {

        // Get field from calendar
        final String year = String.valueOf(horizontalCalendar.getSelectedDate().get(Calendar.YEAR));
        String month = String.valueOf(horizontalCalendar.getSelectedDate().get(Calendar.MONTH));
        String day = String.valueOf(horizontalCalendar.getSelectedDate().get(Calendar.DAY_OF_MONTH));
        final String date = month + "/" + day + "/" + year;

        // Get time field
        final String time = etTime.getText().toString();

        // Get sport field
        final String sportSelected = radioBtnSport.getText().toString();

        // Get notes field
        final String notes = etNotes.getText().toString();

        // Get location field
        final String userInputLocation = etLocation.getText().toString();
        final String inputURL = formatUrl(userInputLocation);

        final String[] formattedLocation = new String[1];
        // [lat, lng]
        final double[] geopointLocation = new double[2];

        AsyncHttpClient client = new AsyncHttpClient();
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(inputURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject results = jsonObject.getJSONArray("results").getJSONObject(0);

                    // Get geo point location
                    geopointLocation[0] = results.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    geopointLocation[1] = results.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    ParseGeoPoint geoPoint = new ParseGeoPoint(geopointLocation[0], geopointLocation[1]);

                    Log.i(TAG, "onSuccess: location lat " + geopointLocation[0]);
                    Log.i(TAG, "onSuccess: location lng " + geopointLocation[1]);

                    // Get formatted location
                    formattedLocation[0] = results.getString("formatted_address");
                    Log.i(TAG, "onSuccess: formatted location " + formattedLocation[0]);

                    // Update fields
                    ParseEvent newEvent = new ParseEvent();

                    newEvent.setDate(date);
                    newEvent.setTime(time);
                    newEvent.setSport(sportSelected);
                    newEvent.setFormattedLocation(formattedLocation[0]);
                    newEvent.setGeopointLocation(geoPoint);
                    newEvent.setNotes(notes);

                    newEvent.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "done: Error while saving", e);
                            }
                            Log.i(TAG, "done: Save event successful!");
                            Toast.makeText(currentContext, "Event Created!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch (JSONException e) {
                    Log.e(TAG, "onSuccess: hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure: Failure to get api response" + response + throwable);
            }
        });
    }

    private static String formatUrl(String userLocation) {

        // Remove commas
        String formattedLocation = userLocation.replace(",", "");

        // Replace spaces with '+'
        formattedLocation = formattedLocation.replace(" ", "+");

        // Combine elements of URL
        return GEOPOINT_URL + formattedLocation + API_KEY;
    }

    public static void resetFields (HorizontalCalendar horizontalCalendar, EditText etTime, RadioButton radioBtnSoccer, EditText etLocation, EditText etNotes) {
        horizontalCalendar.goToday(false);
        etTime.setText("");
        radioBtnSoccer.setChecked(true);
        etLocation.setText("");
        etNotes.setText("");
    }
}
