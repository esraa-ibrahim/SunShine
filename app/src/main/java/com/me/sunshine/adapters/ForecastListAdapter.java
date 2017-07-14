package com.me.sunshine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.sunshine.R;
import com.me.sunshine.json.Day;
import com.me.sunshine.utils.DateUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by Esraa on 7/11/17.
 */

public class ForecastListAdapter extends BaseAdapter {
    private List<Day> mWeatherDays;
    private Context mContext;

    public ForecastListAdapter(Context context, List<Day> weatherDays) {
        mContext = context;
        mWeatherDays = weatherDays;
    }

    @Override
    public int getCount() {
        return mWeatherDays.size();
    }

    @Override
    public Day getItem(int position) {
        return mWeatherDays.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.forecast_list_item, parent, false);
            holder = new ViewHolder();

            holder.ivIcon = view.findViewById(R.id.weather_icon);
            holder.tvDay = view.findViewById(R.id.weather_day);
            holder.tvMaxTemp = view.findViewById(R.id.weather_max_temp);
            holder.tvStatus = view.findViewById(R.id.weather_status);
            holder.tvMinTemp = view.findViewById(R.id.weather_min_temp);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Day day = getItem(position);
        holder.tvDay.setText(DateUtils.getDayNameFromLongDate(mContext, day.getDt()));
        holder.tvMaxTemp.setText(String.format(Locale.US, "%s°",Integer.toString((int)day.getTemp().getMax())));
        holder.tvStatus.setText(day.getWeather().get(0).getMain());
        holder.tvMinTemp.setText(String.format(Locale.US, "%s°",Integer.toString((int)day.getTemp().getMin())));

        switch (day.getWeather().get(0).getId()) {
            // Storm
            case 200: case 201: case 202: case 210: case 211:
            case 212: case 221: case 230: case 231: case 232:
                holder.ivIcon.setImageResource(R.drawable.ic_storm);
                break;

            // Clear
            case 800:
                holder.ivIcon.setImageResource(R.drawable.ic_clear);
                break;

            // Light Cloud
            case 801: case 802:
                holder.ivIcon.setImageResource(R.drawable.ic_light_clouds);
                break;

            // Cloud
            case 803: case 804:
                holder.ivIcon.setImageResource(R.drawable.ic_cloudy);
                break;

            // Fog
            case 701: case 711: case 721: case 731: case 741:
            case 751: case 761: case 762: case 771: case 781:
                holder.ivIcon.setImageResource(R.drawable.ic_fog);
                break;

            // Light Rain
            case 500: case 501: case 502: case 503: case 504:
                holder.ivIcon.setImageResource(R.drawable.ic_light_rain);
                break;

            // Rain
            case 511: case 520: case 521: case 522: case 531:
                holder.ivIcon.setImageResource(R.drawable.ic_rain);
                break;

            // Snow
            case 600: case 601: case 602: case 611: case 612:
            case 615: case 616: case 620: case 621: case 622:
                holder.ivIcon.setImageResource(R.drawable.ic_snow);
                break;
        }

        return view;
    }



    private static class ViewHolder {
        ImageView ivIcon;
        TextView tvDay;
        TextView tvMaxTemp;
        TextView tvStatus;
        TextView tvMinTemp;
    }
}
