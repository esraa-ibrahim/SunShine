package com.me.sunshine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.sunshine.json.BaseWeatherForecastJson;
import com.me.sunshine.json.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class ForecastFragment extends Fragment {
    private ArrayAdapter<String> mForecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the fragment that it will handle menu items
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mForecastAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast,
                R.id.list_item_forecast_textview, new ArrayList<String>());

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, mForecastAdapter.getItem(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forcastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWeather() {
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
        // Cairo Id is 360630
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String cityId = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        String unit = prefs.getString(getString(R.string.pref_temp_key), getString(R.string.pref_temp_default));
        fetchWeatherTask.execute(cityId, unit);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    private class FetchWeatherTask extends AsyncTask <String, Void, String[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        private String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";

        private String APPID = "c1f6e31d5bdb009ddbc73ba2ffdb4877";

        private String APP_ID_PARAM = "appid";

        // City Id
        private String QUERY_PARAM = "id";

        // Data format JSON, XML, or HTML format
        private String FORMAT_PARAM = "mode";

        // Units for returned temperature (metric for Celsius , imperial for Fahrenheit,
        // if units param not set it is Kelvin by default)
        private String UNITS_PARAM = "units";

        // Number of days returned (from 1 to 16)
        private String DAYS_PARAM = "cnt";

        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            // Will contain the weather data
            String[] weatherForecastData = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                String format = "json", units = "metric";
                int days = 17;

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, params[1])
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(days))
                        .appendQueryParameter(APP_ID_PARAM, APPID).build();

                Log.v(LOG_TAG, builtUri.toString());

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG, forecastJsonStr);

                Gson gson = new Gson();
                BaseWeatherForecastJson weatherForecastJson = gson.fromJson(forecastJsonStr, BaseWeatherForecastJson.class);
                weatherForecastData = getWeatherForecastData(weatherForecastJson);

                Log.v(LOG_TAG, weatherForecastJson.getCity().getName());
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return weatherForecastData;
        }

        private String[] getWeatherForecastData(BaseWeatherForecastJson weatherForecastJson) {
            List<Day> days = weatherForecastJson.getDaysList();
            String[] formattedWeatherData = new String[days.size()];
            String dateString, weatherStatus;
            Day day;
            long dt;
            double min, max;

            for (int i = 0; i < days.size(); i++) {
                day = days.get(i);
                dt = day.getDt();
                dateString = getReadableDateString(dt);
                weatherStatus = day.getWeather().get(0).getMain();
                min = day.getTemp().getMin();
                max = day.getTemp().getMax();

                formattedWeatherData[i] = dateString + " - " + weatherStatus + " - " + ((int) min) + "/" + ((int) max);
            }

            return formattedWeatherData;
        }

        private String getReadableDateString(long time) {
            Date d = new Date(time * 1000);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMM d");
            return simpleDateFormat.format(d).toString();
        }

        @Override
        protected void onPostExecute(String[] weatherForecastData) {
            if (weatherForecastData != null) {
                mForecastAdapter.clear();
                mForecastAdapter.addAll(weatherForecastData);
            }
        }
    }
}
