package com.mybp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    // preferences
    private boolean setProfile = false;
    private boolean useGoogleProfile = false;
    private String defaultCurrency = null;

    // Views to be updated
    private Switch switchProfile;
    private Switch switchGoogle;
    private TextView textViewNotifications;
    private Switch switchCurrency;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupActionBar();

        // reference views to be updated
        referenceViews();

        preferences = getSharedPreferences("MyBPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        updateViews();
    }

    private void setupActionBar() {
        // Set action bar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void referenceViews() {
        switchProfile = (Switch) findViewById(R.id.switchProfile);
        switchGoogle = (Switch) findViewById(R.id.switchGoogle);
        textViewNotifications = (TextView) findViewById(R.id.textViewNotifications);
        switchCurrency = (Switch) findViewById(R.id.switchCurrency);

        String profile = getResources().getString(R.string.profile);
        String profileSub = getResources().getString(R.string.profile_sub);
        Span.span(profile, profileSub, switchProfile);

        String profileGoogle = getResources().getString(R.string.profile_google);
        String profileGoogleSub = getResources().getString(R.string.profile_google_sub);
        Span.span(profileGoogle, profileGoogleSub, switchGoogle);

        String notification = getResources().getString(R.string.notification);
        String notificationSub = getResources().getString(R.string.notification_sub);
        Span.span(notification, notificationSub, textViewNotifications);

        String currency = getResources().getString(R.string.currency);
        String currencySub = getResources().getString(R.string.currency_sub);
        Span.span(currency, currencySub, switchCurrency);
    }

    private void updateViews() {
        // update switchProfile view
        setProfile = preferences.getBoolean("setProfile", false);
        if(setProfile) {
            switchProfile.setChecked(true);
            switchGoogle.setChecked(true);
        } else {
            switchProfile.setChecked(false);
        }

        // update switchCurrency view
        defaultCurrency = preferences.getString("defaultCurrency", null);
        if(defaultCurrency == null) {
            switchCurrency.setChecked(false);
        } else {
            switchCurrency.setChecked(true);
        }
    }

    public void onClickProfile(View view) {
        // first check if switchProfile had already been checked
        setProfile = preferences.getBoolean("setProfile", false);
        useGoogleProfile = preferences.getBoolean("useGoogleProfile", false);
        if(setProfile && useGoogleProfile) {
            setProfile = false;
            useGoogleProfile = false;
            editor.putBoolean("setProfile", setProfile);
            editor.putBoolean("useGoogleProfile", useGoogleProfile);
            editor.commit();
            switchGoogle.setChecked(false);
            return;
        }

        setProfile = true;
        useGoogleProfile = true;
        editor.putBoolean("setProfile", setProfile);
        editor.putBoolean("useGoogleProfile", useGoogleProfile);
        editor.commit();
        switchGoogle.setChecked(true);
    }

    public void onClickShowNotificationsActivity(View view) {
        startActivity(new Intent(this, NotificationActivity.class));
    }

    public void onClickCurrency(View view) {
        defaultCurrency = preferences.getString("defaultCurrency", null);
        if(defaultCurrency != null) {
            defaultCurrency = null;
            editor.putString("defaultCurrency", defaultCurrency);
            editor.commit();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Currency")
                .setMessage("Currency list")
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        defaultCurrency = "USD";
                        editor.putString("defaultCurrency", defaultCurrency);
                        editor.commit();
                        Toast.makeText(SettingsActivity.this, defaultCurrency, Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        switchCurrency.setChecked(false);
                        dialogInterface.cancel();
                    }
                });
        builder.create().show();
    }
}
