package com.me.sunshine.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Esraa on 8/5/2017.
 */

public class PrefsUtils {
    private static String mSharedPrefsName = "Sunshine_SharedPrefs";

    public static void saveBool(Context context, String key, boolean val) {
        SharedPreferences sharedPref = context.getSharedPreferences(mSharedPrefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static Boolean getBool(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(mSharedPrefsName, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }
}
