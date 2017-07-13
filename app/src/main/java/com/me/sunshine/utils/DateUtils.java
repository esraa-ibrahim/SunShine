package com.me.sunshine.utils;

import android.content.Context;

import com.me.sunshine.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Esraa on 7/13/17.
 */

public class DateUtils {
    public static String getDayNameFromLongDate(Context context, long time) {
        Calendar weatherDate = Calendar.getInstance();
        weatherDate.setTimeInMillis(time * 1000);

        Calendar now = Calendar.getInstance();
        if (weatherDate.get(Calendar.DATE) == now.get(Calendar.DATE)) {
            return context.getString(R.string.today);
        }

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        if (weatherDate.get(Calendar.DATE) == tomorrow.get(Calendar.DATE)) {
            return context.getString(R.string.tomorrow);
        }

        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("EEEE", Locale.US);
        return simpleDateFormat.format(weatherDate.getTime());
    }

    public static String getDateStringFromLongDate(long time, SimpleDateFormat simpleDateFormat) {
        Calendar weatherDate = Calendar.getInstance();
        weatherDate.setTimeInMillis(time * 1000);

        return simpleDateFormat.format(weatherDate.getTime());
    }
}
