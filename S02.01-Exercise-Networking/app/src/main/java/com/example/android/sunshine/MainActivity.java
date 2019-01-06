/*
 * Copyright (C) 2016 The Android Open Source Project
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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        // COMPLETE (4) Delete the dummy weather data. You will be getting REAL data from the Internet in this lesson.

        // COMPLETE (3) Delete the for loop that populates the TextView with dummy data

        // COMPLETE (9) Call loadWeatherData to perform the network request to get the weather
        loadWeatherData();
    }

    // COMPLETE (8) Create a method that will get the user's preferred location and execute your new AsyncTask and call it loadWeatherData
    public void loadWeatherData() {
        String preferredLocation = SunshinePreferences.getPreferredWeatherLocation(this);
        new ForecastQueryTask().execute(preferredLocation);
    }

    // COMPLETE (5) Create a class that extends AsyncTask to perform network requests
    public class ForecastQueryTask extends AsyncTask<String, Void, String[]> {

        // COMPLETE (6) Override the doInBackground method to perform your network requests
        @Override
        protected String[] doInBackground(String[] params) {

            // If there are no parameter entries, return null
            if(params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String weatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

                String[] jsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, weatherResponse);

                return jsonWeatherData;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // COMPLETED (7) Override the onPostExecute method to display the results of the network request
        @Override
        protected void onPostExecute(String[] weatherData) {
            if(weatherData != null) {
                for (String weatherDatum : weatherData) {
                    mWeatherTextView.append(weatherDatum + "\n\n\n");
                }
            }
        }
    }


}