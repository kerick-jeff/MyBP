package com.mybp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String ACTION_NOTIFICATION = "com.mybp.ACTION_NOTIFICATION";
    private SharedPreferences preferences;
    private static int numNots = 0;

    private boolean receiveNotifications;
    private boolean setSoundNotification;
    private boolean setLightNotification;
    private boolean setVibrationsNotification;

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = context.getSharedPreferences("MyBPreferences", Context.MODE_PRIVATE);

        receiveNotifications = preferences.getBoolean("receiveNotifications", false);
        setSoundNotification = preferences.getBoolean("setSoundNotification", false);
        setLightNotification = preferences.getBoolean("setLightNotification", false);
        setVibrationsNotification = preferences.getBoolean("setVibrationsNotification", false);

        if (intent != null && ACTION_NOTIFICATION.equals(intent.getAction()) && receiveNotifications) {
            raiseNotification(context);
        }
    }

    @SuppressWarnings("deprecation")
    private void raiseNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker("MyBP reminder");
        builder.setSmallIcon(R.drawable.ic_dashboard_grey_700_24dp);
        builder.setColor(context.getResources().getColor(R.color.teal));
        builder.setContentTitle("MyBP");
        builder.setContentText("MyBP periodic reminder");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        if(numNots == 0)
            ++numNots;
        else
            builder.setNumber(++numNots);

        if(setSoundNotification) {
           builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }

        if(setLightNotification) {
            builder.setLights(context.getResources().getColor(R.color.teal), 2000, 2000);
        }

        if(setVibrationsNotification) {
            builder.setVibrate(new long[]{250, 500, 250, 500, 250, 500});
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
