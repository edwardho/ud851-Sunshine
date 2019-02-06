package com.example.android.sunshine;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    // COMPLETE (1) Add new Activity called SettingsActivity using Android Studio wizard

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = this.getSupportActionBar();

        // Do step 2 in SettingsActivity
        // COMPLETE (2) Set setDisplayHomeAsUpEnabled to true on the support ActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
