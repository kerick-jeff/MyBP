package com.mybp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

public class OnBootReceiver extends BroadcastReceiver {
    final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    private SharedPreferences preferences;

    private boolean receiveNotifications;
    private long notificationPeriod;

    public OnBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = context.getSharedPreferences("MyBPreferences", Context.MODE_PRIVATE);
        receiveNotifications = preferences.getBoolean("receiveNotifications", false);
        notificationPeriod = preferences.getLong("notificationPeriod", 0);

        if(intent != null && intent.getAction().equals(BOOT_COMPLETED) && receiveNotifications && notificationPeriod > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(notificationPeriod);
            Intent alarmIntent = new Intent(context, NotificationService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, pendingIntent);
        }
    }
}
