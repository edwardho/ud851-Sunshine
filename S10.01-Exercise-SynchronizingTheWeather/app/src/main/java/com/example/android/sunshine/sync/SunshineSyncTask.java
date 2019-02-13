package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class SunshineSyncTask {
//  COMPLETE (1) Create a class called SunshineSyncTask
//  COMPLETE (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
    synchronized  public static void syncWeather(@NonNull final Context context) {
//      COMPLETE (3) Within syncWeather, fetch new weather data
        try{
            // Get weather url to obtain JSON response for weather
            URL weatherRequestUrl = NetworkUtils.getUrl(context);

            // Use the url to retrieve the JSON
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

            // Parse the JSON values
            ContentValues[] weatherValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            if (weatherValues != null && weatherValues.length != 0) {
                // Get ContentResolver to insert and delete data
                ContentResolver sunshineContentResolver = context.getContentResolver();

//              COMPLETE (4) If we have valid results, delete the old data and insert the new
                // Delete old data
                sunshineContentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);
                // Insert new data
                sunshineContentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
