package com.me.sunshine.utils;

/**
 * Created by Esraa on 7/18/17.
 */

public class Constants {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?",
            APPID = "c1f6e31d5bdb009ddbc73ba2ffdb4877",
            APP_ID_PARAM = "appid",

            // City Id
            QUERY_PARAM = "id",

            // Data format JSON, XML, or HTML format
            FORMAT_PARAM = "mode",

            // Units for returned temperature (metric for Celsius , imperial for Fahrenheit,
            // if units param not set it is Kelvin by default)
            UNITS_PARAM = "units",

            // Number of days returned (from 1 to 16)
            DAYS_PARAM = "cnt",

            // App hash tag when sharing
            FORECAST_SHARE_HASH_TAG = " #SunshineApp",

            // ForecastDetailFragment argument constant
            ARG_DAY_DATA = "DAY_DATA",

            // Constant for saving current selected forecast item position
            PREFS_CURRENT_POSITION = "CURRENT_POSITION";

    public static boolean isDualPane = false;
}
