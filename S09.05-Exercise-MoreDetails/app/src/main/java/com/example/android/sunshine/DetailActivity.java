/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
//      COMPLETE (21) Implement LoaderManager.LoaderCallbacks<Cursor>

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

//  COMPLETE (18) Create a String array containing the names of the desired data columns from our ContentProvider
    public static final String[] WEATHER_DETAIL_PROJECTION = {
        WeatherContract.WeatherEntry.COLUMN_DATE,
        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
        WeatherContract.WeatherEntry.COLUMN_PRESSURE,
        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
        WeatherContract.WeatherEntry.COLUMN_DEGREES,
        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

//  COMPLETE (19) Create constant int values representing each column name's position above
    private static final int INDEX_WEATHER_DATE = 0;
    private static final int INDEX_WEATHER_MAX_TEMP = 1;
    private static final int INDEX_WEATHER_MIN_TEMP = 2;
    private static final int INDEX_WEATHER_HUMIDITY = 3;
    private static final int INDEX_WEATHER_PRESSURE = 4;
    private static final int INDEX_WEATHER_WIND_SPEED = 5;
    private static final int INDEX_WEATHER_DEGREES = 6;
    private static final int INDEX_WEATHER_CONDITION_ID = 7;

//  COMPLETE (20) Create a constant int to identify our loader used in DetailActivity
    private static final int ID_DETAIL_LOADER = 252;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

//  COMPLETE (15) Declare a private Uri field called mUri
    private Uri mUri;

//  COMPLETE (10) Remove the mWeatherDisplay TextView declaration

//  COMPLETE (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mDateDisplay;
    private TextView mDescriptionDisplay;
    private TextView mTempHighDisplay;
    private TextView mTempLowDisplay;
    private TextView mHumidityDisplay;
    private TextView mPressureDisplay;
    private TextView mWindDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      COMPLETE (12) Remove mWeatherDisplay TextView
//      COMPLETE (13) Find each of the TextViews by ID
        mDateDisplay = (TextView) findViewById(R.id.tv_display_date);
        mDescriptionDisplay = (TextView) findViewById(R.id.tv_display_desc);
        mTempHighDisplay = (TextView) findViewById(R.id.tv_display_temp_high);
        mTempLowDisplay = (TextView) findViewById(R.id.tv_display_temp_low);
        mHumidityDisplay = (TextView) findViewById(R.id.tv_display_humidity);
        mPressureDisplay = (TextView) findViewById(R.id.tv_display_pressure);
        mWindDisplay = (TextView) findViewById(R.id.tv_display_wind);

//      COMPLETE (14) Remove the code that checks for extra text
        Intent intentThatStartedThisActivity = getIntent();

//      COMPLETE (16) Use getData to get a reference to the URI passed with this Activity's Intent
        // Get data from intent that started the activity
        mUri = intentThatStartedThisActivity.getData();

//      COMPLETE (17) Throw a NullPointerException if that URI is null
        if (mUri == null) {
            throw new NullPointerException("URI for Detail Activity cannot be null");
        }

//      COMPLETE (35) Initialize the loader for DetailActivity
        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

//  COMPLETE (22) Override onCreateLoader
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
        switch (loaderId) {
//          COMPLETE (23) If the loader requested is our detail loader, return the appropriate CursorLoader
            case ID_DETAIL_LOADER:
                // (URI)
                // URI for all rows of weather data in the table
                Uri detailUri = mUri;

                // (projection)
                // Projection of the weather data declared above
                // Narrows down columns we want to show
                String[] projection = WEATHER_DETAIL_PROJECTION;

                // (selection)
                // Declares which rows we would like to return
                // In this case, we don't need to declare the row
                String selection = null;

                // (selection args)
                // Arguments for returning rows
                String[] selectionArgs = null;

                // (sort order)
                // We don't need to declare the sort
                String sortOrder = null;

                return new CursorLoader(this,
                        detailUri,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not implemented: " + loaderId);
        }
    }

//  COMPLETE (24) Override onLoadFinished
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
//      COMPLETE (25) Check before doing anything that the Cursor has valid data
        boolean cursorHasValidData = false;

        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (cursorHasValidData == false) {
            return;
        }

//      COMPLETE (26) Display a readable data string
        long normalizedUtcMidnight = data.getLong(INDEX_WEATHER_DATE);
        String dateText = SunshineDateUtils.getFriendlyDateString(this, normalizedUtcMidnight, false);
        mDateDisplay.setText("Date: " + dateText);

//      COMPLETE (27) Display the weather description (using SunshineWeatherUtils)
        int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
        String descText = SunshineWeatherUtils.getStringForWeatherCondition(this, weatherId);
        mDescriptionDisplay.setText("Condition: " + descText);

//      COMPLETE (28) Display the high temperature
        double highTemperature = data.getDouble(INDEX_WEATHER_MAX_TEMP);
        String highTempText = SunshineWeatherUtils.formatTemperature(this, highTemperature);
        mTempHighDisplay.setText("High Temp: " + highTempText);

//      COMPLETE (29) Display the low temperature
        double lowTemperature = data.getDouble(INDEX_WEATHER_MIN_TEMP);
        String lowTempText = SunshineWeatherUtils.formatTemperature(this,lowTemperature);
        mTempLowDisplay.setText("Low Temp: " + lowTempText);

//      COMPLETE (30) Display the humidity
        float humidity = data.getFloat(INDEX_WEATHER_HUMIDITY);
        String humidityText = getString(R.string.format_humidity, humidity);
        mHumidityDisplay.setText("Humidity: " + humidityText);

//      COMPLETE (31) Display the wind speed and direction
        float windSpeed = data.getFloat(INDEX_WEATHER_WIND_SPEED);
        float windDirection = data.getFloat(INDEX_WEATHER_DEGREES);
        String windSpeedText = SunshineWeatherUtils.getFormattedWind(this, windSpeed, windDirection);
        mWindDisplay.setText("Wind Speed: " + windSpeedText);

//      COMPLETE (32) Display the pressure
        float pressure = data.getFloat(INDEX_WEATHER_PRESSURE);
        String pressureText = getString(R.string.format_pressure, pressure);
        mPressureDisplay.setText("Pressure: " + pressureText);

//      COMPLETE (33) Store a forecast summary in mForecastSummary
        mForecastSummary = dateText + " - " + descText + " - " + highTempText + "/" + lowTempText;

    }

//  COMPLETE (34) Override onLoaderReset, but don't do anything in it yet
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}