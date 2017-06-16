package com.mybp;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

/**
 * Created by kerick on 1/18/17.
 */

public class RateApp extends DialogFragment {
    private final String packageName = "com.mybp";
    private final static int LAUNCHES_UNTIL_PROMPT = 5;
    private final static int DAYS_UNTIL_PROMPT = 5;

    public void appLaunched(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MyBPreferences", Context.MODE_PRIVATE);
        if(preferences.getBoolean("disabled", false)) {
            return;
        }

        SharedPreferences.Editor editor = preferences.edit();

        long launchCount = preferences.getLong("launchCount", 0) + 1;
        editor.putLong("launchCount", launchCount);

        long firstLaunchDate = preferences.getLong("firstLaunchDate", 0);
        if(firstLaunchDate == 0) {
            firstLaunchDate = System.currentTimeMillis();
            editor.putLong("firstLaunchDate", firstLaunchDate);
        }

        if(launchCount >= LAUNCHES_UNTIL_PROMPT) {
            if(System.currentTimeMillis() >= firstLaunchDate + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(context, editor);
            }
        }

        editor.commit();
    }

    public void rate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MyBPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        showRateDialog(context, editor);
    }

    private void showRateDialog(final Context context, final SharedPreferences.Editor editor){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Rate this app")
            .setMessage("Please take a moment to rate us 5 stars on Play Store")
            .setPositiveButton("Rate now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    editor.putBoolean("disabled", true);
                    editor.commit();
                }
            })
            .setNeutralButton("No, thanks", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    editor.putBoolean("disabled", true);
                    editor.commit();
                }
            })
            .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                   // dismiss();
                }
            });
        builder.create().show();
    }
}
