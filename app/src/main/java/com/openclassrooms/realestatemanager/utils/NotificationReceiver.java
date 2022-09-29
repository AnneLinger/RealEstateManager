package com.openclassrooms.realestatemanager.utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.di.MainApplication;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Util class to manage notifications
 */

@RequiresApi(api = Build.VERSION_CODES.M)
@AndroidEntryPoint
public class NotificationReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 7;
    private static final String NOTIFICATION_TAG = "Real Estate Manager";
    private Context context;

    @Inject
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        sendVisualNotification();
    }

    private void sendVisualNotification() {
        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(context, MainApplication.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Create a Channel (Android 8)
        String channelId = context.getString(R.string.default_notification_channel_id);

        // Build notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_house_24)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(context.getString(R.string.notification_text))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Firebase Messages";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}
