package com.mybp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            editor.putLong("notificationPeriod", calendar.getTimeInMillis());
            editor.commit();

            Intent intent = new Intent(NotificationActivity.this, NotificationReceiver.class);
            intent.setAction(NotificationReceiver.ACTION_NOTIFICATION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationActivity.this, 0, intent, 0);
            AlarmManager manager = (AlarmManager) NotificationActivity.this.getSystemService(Context.ALARM_SERVICE);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, pendingIntent);

            Toast.makeText(NotificationActivity.this, DateUtils.formatDateTime(NotificationActivity.this, calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME), Toast.LENGTH_LONG).show();
        }
    };

    // preferences
    private boolean receiveNotifications;
    private boolean setSoundNotification;
    private boolean setLightNotification;
    private boolean setVibrationsNotification;

    // Views to be updated
    private Switch switchNotifications;
    private Switch switchNotificationSound;
    private Switch switchNotificationLight;
    private Switch switchNotificationVibrations;
    private TextView notificationPeriods;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setupActionBar();

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
        switchNotifications = (Switch) findViewById(R.id.switchNotifications);
        switchNotificationSound = (Switch) findViewById(R.id.switchNotificationSound);
        switchNotificationLight = (Switch) findViewById(R.id.switchNotificationLight);
        switchNotificationVibrations = (Switch) findViewById(R.id.switchNotificationVibrations);
        notificationPeriods = (TextView) findViewById(R.id.textViewNotificationPeriods);
    }

    private void updateViews() {
        // update switchNotifications view
        receiveNotifications = preferences.getBoolean("receiveNotifications", false);
        if(receiveNotifications) {
            switchNotifications.setChecked(true);

            // enable other notification features based on whether notification button is checked
            switchNotificationSound.setEnabled(true);
            switchNotificationLight.setEnabled(true);
            switchNotificationVibrations.setEnabled(true);
            notificationPeriods.setEnabled(true);
        } else {
            switchNotifications.setChecked(false);

            // disable other notification features based on whether notification button is unchecked
            switchNotificationSound.setEnabled(false);
            switchNotificationLight.setEnabled(false);
            switchNotificationVibrations.setEnabled(false);
            notificationPeriods.setEnabled(false);
        }

        // update switchNotificationSound view
        setSoundNotification = preferences.getBoolean("setSoundNotification", false);
        if(setSoundNotification) {
            switchNotificationSound.setChecked(true);
        } else {
            switchNotificationSound.setChecked(false);
        }

        // update switchNotificationLight view
        setLightNotification = preferences.getBoolean("setLightNotification", false);
        if(setLightNotification) {
            switchNotificationLight.setChecked(true);
        } else {
            switchNotificationLight.setChecked(false);
        }

        // update switchNotificationVibrations view
        setVibrationsNotification = preferences.getBoolean("setVibrationsNotification", false);
        if(!setVibrationsNotification) {
            switchNotificationVibrations.setChecked(false);
        } else {
            switchNotificationVibrations.setChecked(true);
        }
    }

    public void onClickReceiveNotifications(View view) {
        receiveNotifications = preferences.getBoolean("receiveNotifications", false);
        if(receiveNotifications) {
            // disable other notification features based on whether notification button is unchecked
            switchNotificationSound.setEnabled(false);
            switchNotificationLight.setEnabled(false);
            switchNotificationVibrations.setEnabled(false);
            notificationPeriods.setEnabled(false);

            receiveNotifications = false;
            editor.putBoolean("receiveNotifications", receiveNotifications);
            editor.commit();
            return;
        }

        // enable other notification features based on whether notification button is checked
        switchNotificationSound.setEnabled(true);
        switchNotificationLight.setEnabled(true);
        switchNotificationVibrations.setEnabled(true);
        notificationPeriods.setEnabled(true);

        receiveNotifications = true;
        editor.putBoolean("receiveNotifications", receiveNotifications);
        editor.commit();
    }

    public void onClickSoundNotification(View view) {
        setSoundNotification = preferences.getBoolean("setSoundNotification", false);
        if(setSoundNotification) {
            setSoundNotification = false;
            editor.putBoolean("setSoundNotification", setSoundNotification);
            editor.commit();
            return;
        }

        setSoundNotification = true;
        editor.putBoolean("setSoundNotification", setSoundNotification);
        editor.commit();
    }

    public void onClickLightNotification(View view) {
        setLightNotification = preferences.getBoolean("setLightNotification", false);
        if(setLightNotification) {
            setLightNotification = false;
            editor.putBoolean("setLightNotification", setLightNotification);
            editor.commit();
            return;
        }

        setLightNotification = true;
        editor.putBoolean("setLightNotification", setLightNotification);
        editor.commit();
    }

    public void onClickVibrationsNotification(View view) {
        setVibrationsNotification = preferences.getBoolean("setVibrationsNotification", false);
        if(setVibrationsNotification) {
            setVibrationsNotification = false;
            editor.putBoolean("setVibrationsNotification", setVibrationsNotification);
            editor.commit();
            return;
        }

        setVibrationsNotification = true;
        editor.putBoolean("setVibrationsNotification", setVibrationsNotification);
        editor.commit();
    }

    public void onClickNotificationPeriod(View view) {
        new TimePickerDialog(
                NotificationActivity.this,
                onTimeSetListener,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true).show();
    }

}
