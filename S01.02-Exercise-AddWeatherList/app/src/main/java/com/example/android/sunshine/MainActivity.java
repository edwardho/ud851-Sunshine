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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // COMPLETED (1) Create a field to store the weather display TextView
    TextView weatherDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // COMPLETED (2) Use findViewById to get a reference to the weather display TextView
        weatherDisplay = (TextView) findViewById(R.id.tv_weather_data);

        // COMPLETED (3) Create an array of Strings that contain fake weather data
        String[] weatherData = {
                "Today, Dec 31 - Clear - 32°F / 28°F",
                "Tomorrow - Cloudy - 43°F / 34°F",
                "Wednesday, Jan 2 - Clear - 43°F / 34°F",
                "Thursday, Jan 3 - Clear - 32°F / 28°F",
                "Friday, Jan 4 - Cloudy - 43°F / 34°F",
                "Saturday, Jan 5 - Clear - 32°F / 28°F",
                "Sunday, Jan 6 - Cloudy - 43°F / 34°F",
                "Monday, Jan 7 - Clear - 32°F / 28°F",
                "Tuesday, Jan 8 - Clear - 32°F / 28°F",
                "Wednesday, Jan 9 - Cloudy - 43°F / 34°F",
                "Thursday, Jan 10 - Clear - 32°F / 28°F",
                "Friday, Jan 11 - Cloudy - 43°F / 34°F",
                "Saturday, Jan 12 - Clear - 32°F / 28°F",
                "Sunday, Jan 13 - Cloudy - 43°F / 34°F",
        };

        // COMPLETED (4) Append each String from the fake weather data array to the TextView
        for (String weatherDatum:weatherData) {
            weatherDisplay.append(weatherDatum + "\n\n\n");
        }
    }
}