package com.me.sunshine.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.sunshine.R;
import com.me.sunshine.json.Day;
import com.me.sunshine.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class ForecastDetailFragment extends Fragment {
    // the fragment initialization parameters
    private static final String ARG_DAY_DATA = "DAY_DATA";
    private Day mParamDayData;
    private final String FORECAST_SHARE_HASH_TAG = " #SunshineApp";
    private String mForecastData;

    public ForecastDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForecastDetailFragment.
     */
    public static ForecastDetailFragment newInstance() {
        ForecastDetailFragment fragment = new ForecastDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamDayData = (Day) getArguments().getSerializable(ARG_DAY_DATA);
        }
        // Tell the fragment that it will handle menu items
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View forecastDetailView = inflater.inflate(R.layout.fragment_forecast_detail, container, false);

        TextView tvDay = forecastDetailView.findViewById(R.id.day);
        TextView tvDate = forecastDetailView.findViewById(R.id.date);
        TextView tvMaxTemp = forecastDetailView.findViewById(R.id.max_temp);
        TextView tvMinTemp = forecastDetailView.findViewById(R.id.min_temp);
        ImageView ivDayImage = forecastDetailView.findViewById(R.id.weather_image);
        TextView tvStatus = forecastDetailView.findViewById(R.id.status);
        TextView tvHumidity = forecastDetailView.findViewById(R.id.humidity);
        TextView tvPressure = forecastDetailView.findViewById(R.id.pressure);
        TextView tvWind = forecastDetailView.findViewById(R.id.wind);

        tvDay.setText(DateUtils.getDayNameFromLongDate(getContext(), mParamDayData.getDt()));
        tvDate.setText(DateUtils.getDateStringFromLongDate(mParamDayData.getDt(), new SimpleDateFormat("MMMM dd", Locale.US)));
        tvMaxTemp.setText(String.format(Locale.US, "%s°",Integer.toString((int)mParamDayData.getTemp().getMax())));
        tvMinTemp.setText(String.format(Locale.US, "%s°",Integer.toString((int)mParamDayData.getTemp().getMin())));
        switch (mParamDayData.getWeather().get(0).getId()) {
            // Storm
            case 200: case 201: case 202: case 210: case 211:
            case 212: case 221: case 230: case 231: case 232:
                ivDayImage.setImageResource(R.drawable.art_storm);
                break;

            // Clear
            case 800:
                ivDayImage.setImageResource(R.drawable.art_clear);
                break;

            // Light Cloud
            case 801: case 802:
                ivDayImage.setImageResource(R.drawable.art_light_clouds);
                break;

            // Cloud
            case 803: case 804:
                ivDayImage.setImageResource(R.drawable.art_clouds);
                break;

            // Fog
            case 701: case 711: case 721: case 731: case 741:
            case 751: case 761: case 762: case 771: case 781:
                ivDayImage.setImageResource(R.drawable.art_fog);
                break;

            // Light Rain
            case 500: case 501: case 502: case 503: case 504:
                ivDayImage.setImageResource(R.drawable.art_light_rain);
                break;

            // Rain
            case 511: case 520: case 521: case 522: case 531:
                ivDayImage.setImageResource(R.drawable.art_rain);
                break;

            // Snow
            case 600: case 601: case 602: case 611: case 612:
            case 615: case 616: case 620: case 621: case 622:
                ivDayImage.setImageResource(R.drawable.art_snow);
                break;
        }

        tvStatus.setText(mParamDayData.getWeather().get(0).getMain());

        tvHumidity.setText(String.format(Locale.US, getString(R.string.humidity_str_format), mParamDayData.getHumidity()));
        tvPressure.setText(String.format(Locale.US, getString(R.string.pressure_str_format), (int)mParamDayData.getPressure()));
        tvWind.setText(String.format(Locale.US, getString(R.string.wind_str_format), mParamDayData.getSpeed()));

        mForecastData = String.format(Locale.US, "%s %s\nMax Temp: %s\nMin Temp: %s\nWeather Status: %s",
                tvDay.getText().toString(), tvDate.getText().toString(), tvMaxTemp.getText().toString(),
                tvMinTemp.getText().toString(), tvStatus.getText().toString());
        return forecastDetailView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.detailfragment, menu);

        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    /***
     * Create intent for sharing current weather data
     * @return the sharing intent
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastData + FORECAST_SHARE_HASH_TAG);
        shareIntent.setType("text/plain");
        return shareIntent;
    }
}
