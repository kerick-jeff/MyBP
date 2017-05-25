package com.mybp;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class NotificationService extends IntentService {
    private SharedPreferences preferences;
    private static int numNots = 0;

    private boolean receiveNotifications;
    private boolean setSoundNotification;
    private boolean setLightNotification;
    private boolean setVibrationsNotification;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        preferences = getSharedPreferences("MyBPreferences", Context.MODE_PRIVATE);

        receiveNotifications = preferences.getBoolean("receiveNotifications", false);
        setSoundNotification = preferences.getBoolean("setSoundNotification", false);
        setLightNotification = preferences.getBoolean("setLightNotification", false);
        setVibrationsNotification = preferences.getBoolean("setVibrationsNotification", false);

        if (intent != null && receiveNotifications) {
            raiseNotification();
        }
    }

    @SuppressWarnings("deprecation")
    private void raiseNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("MyBP reminder");
        builder.setSmallIcon(R.drawable.ic_dashboard_grey_700_24dp);
        builder.setColor(this.getResources().getColor(R.color.teal));
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
            builder.setLights(this.getResources().getColor(R.color.teal), 2000, 2000);
        }

        if(setVibrationsNotification) {
            builder.setVibrate(new long[]{250, 500, 250, 500, 250, 500});
        }

        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
