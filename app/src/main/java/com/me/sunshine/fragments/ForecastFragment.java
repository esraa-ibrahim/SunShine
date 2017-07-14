package com.me.sunshine.fragments;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.me.sunshine.R;
import com.me.sunshine.adapters.ForecastListAdapter;
import com.me.sunshine.json.BaseWeatherForecastJson;
import com.me.sunshine.json.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class ForecastFragment extends Fragment {
    private ForecastListAdapter mForecastAdapter;
    private ListView listView;
    private View headerView;
    private OnForecastItemSelectedListener mForecastItemSelectedListener;

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
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_header, null);
        listView = rootView.findViewById(R.id.listview_forecast);

        mForecastAdapter = new ForecastListAdapter(getContext(), new ArrayList<Day>());

        listView.addHeaderView(headerView);
        listView.setAdapter(mForecastAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Open ForecastDetailFragment and pass current day weather to it
                mForecastItemSelectedListener.onListItemClicked((Day) parent.getItemAtPosition(position));
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

    private class FetchWeatherTask extends AsyncTask<String, Void, BaseWeatherForecastJson> {

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
        protected BaseWeatherForecastJson doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            // Will contain the weather data
            BaseWeatherForecastJson weatherForecastJson = null;

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
                weatherForecastJson = gson.fromJson(forecastJsonStr, BaseWeatherForecastJson.class);

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
            return weatherForecastJson;
        }

        @Override
        protected void onPostExecute(BaseWeatherForecastJson weatherForecastJson) {
            if (weatherForecastJson != null) {
                List<Day> weatherDays = weatherForecastJson.getDaysList();
                // Set current city and country
                ((TextView) headerView.findViewById(R.id.header)).setText(weatherForecastJson.getCity().getName() + ", "
                        + weatherForecastJson.getCity().getCountry());

                listView.setAdapter(new ForecastListAdapter(getContext(), weatherDays));
                mForecastAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mForecastItemSelectedListener = (OnForecastItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnForecastItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mForecastItemSelectedListener = null;
    }

    public interface OnForecastItemSelectedListener {
        void onListItemClicked(Day dayData);
    }
}
