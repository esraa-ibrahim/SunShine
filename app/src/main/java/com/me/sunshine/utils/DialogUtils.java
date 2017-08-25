package com.me.sunshine.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.me.sunshine.R;

/**
 * Created by Esraa on 8/25/2017.
 */

public class DialogUtils {
    public static AlertDialog showProgressDialog(Activity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
}
