package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

// COMPLETE (4) Create SettingsFragment and extend PreferenceFragmentCompat
// COMPLETE (10) Implement OnSharedPreferenceChangeListener from SettingsFragment
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    
    // COMPLETE (5) Override onCreatePreferences and add the preference xml file using addPreferencesFromResource
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Add sunshine preferences, defined in the XML file in res->xml->pref_sunshine
        addPreferencesFromResource(R.xml.pref_sunshine);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();

        // Go through all the preferences and set up their preference summary
        for (int i = 0; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            // COMPLETE (9) Set the preference summary on each preference that isn't a CheckBoxPreference
            // You don't need to set up preference summary for checkboxes
            // as they are already set up in xml using summaryOn and summaryOff
            if(!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(),"");
                setPreferenceSummary(preference, value);
            }
        }
    }

    // COMPLETE (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Figure out which preference was changed
        Preference  preference = findPreference(key);
        if(preference != null) {
            // Update the summary for the preference
            if(!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(),"");
                setPreferenceSummary(preference, value);
            }
        }
    }

    // COMPLETE (8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference
    public void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            // For ListPreferences, figure out the label of the selected value
            ListPreference listPreference = (ListPreference) preference;
            // Get the index of the preference value
            int preferenceIndex = listPreference.findIndexOfValue(value);
            // Check that the preference is valid
            if (preferenceIndex >= 0) {
                // Set summary to the label
                listPreference.setSummary(listPreference.getEntries()[preferenceIndex]);
            }
        } else if (preference instanceof EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's string representation
            preference.setSummary(value);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return false;
    }

    // COMPLETE (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart
    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    // COMPLETE (13) Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop
    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
