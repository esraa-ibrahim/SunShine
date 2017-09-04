package com.me.sunshine.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.me.sunshine.R;
import com.me.sunshine.custom.CityDialogPreference;
import com.me.sunshine.database.entities.City;
import com.me.sunshine.utils.Constants;
import com.me.sunshine.utils.DialogUtils;
import com.me.sunshine.utils.PrefsUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener,
        CityDialogPreference.OnCityChangeListener {

    private NumberProgressBar progressBar;
    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_general);

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        Preference locationPref = findPreference(getString(R.string.pref_location_key));
        bindPreferenceSummaryToValue(locationPref);
        ((CityDialogPreference) locationPref).setOnCityChangeListener(this);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_temp_key)));

        Boolean isCitiesCached = PrefsUtils.getBool(SettingsActivity.this, Constants.PREFS_IS_CACHED_CITIES);
        if (!isCitiesCached) {
            dialog = DialogUtils.showProgressDialog(SettingsActivity.this);
            progressBar = (NumberProgressBar) dialog.findViewById(R.id.number_progress_bar);

            new FetchCitiesTask().execute();
        }
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }

    @Override
    public void onCitySelected(City city) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.pref_location_id_key), String.valueOf(city.getUid()));
        editor.apply();
    }

    /**
     * Copy cities database from assets folder to internal storage
     */
    private class FetchCitiesTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String dbName = "database_sunshine";
                String path = getDatabasePath(dbName).getParentFile().toString();

                // If directory database doesn't exist create one
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                OutputStream myOutput = new FileOutputStream(path +"/"+ dbName);
                byte[] buffer = new byte[1024];
                int length;
                int totalSize = 0;
                InputStream myInput = getAssets().open(dbName);
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                    totalSize +=length;
                    publishProgress(totalSize);
                }
                myInput.close();
                myOutput.flush();
                myOutput.close();

                PrefsUtils.saveBool(SettingsActivity.this, Constants.PREFS_IS_CACHED_CITIES, true);
            } catch (IOException ex) {
                Log.e(getClass().getSimpleName(), ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }
}
