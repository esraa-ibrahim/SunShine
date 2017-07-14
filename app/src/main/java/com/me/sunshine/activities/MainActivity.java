package com.me.sunshine.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.me.sunshine.R;
import com.me.sunshine.fragments.ForecastDetailFragment;
import com.me.sunshine.fragments.ForecastFragment;
import com.me.sunshine.json.Day;


public class MainActivity extends AppCompatActivity implements ForecastFragment.OnForecastItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set actionbar logo icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_map) {
            showPreferredLocationOnMap();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPreferredLocationOnMap() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String cityId = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q", cityId).build());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "No App to open the location on map", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemClicked(Day dayData) {
        ForecastFragment forecastFragment = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.forecast_fragment);

        if (forecastFragment != null) {
            // If forecast frag is available, we're in two-pane layout
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags
            Bundle bundle = new Bundle();
            bundle.putSerializable("DAY_DATA", dayData);
            ForecastDetailFragment forecastDetailFragment = ForecastDetailFragment.newInstance();
            forecastDetailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, forecastDetailFragment)
                    .addToBackStack(null).commit();
        }
    }
}
