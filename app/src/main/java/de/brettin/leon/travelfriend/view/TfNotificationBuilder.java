package de.brettin.leon.travelfriend.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import de.brettin.leon.travelfriend.MapsActivity;
import de.brettin.leon.travelfriend.R;

public class TfNotificationBuilder {

    private static final String CHANNEL_ID = "347883473";
    private static final String CHANNEL_NAME = "Travelfriend Notifications";

    public void buildStandardNotification(Context context, int notificationId, String notificationTitle,
                                          String notificationMessage, TfNotificationType notificationType) {

        // Set string for notificationsummarytext
        // There are currently only 2 available so i hold it short here
        String notificationSummaryText = context.getString(
                notificationType == TfNotificationType.MESSAGE ?
                        R.string.notification_message : R.string.notification_error);


        // Set up notification builder
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID);
        Intent ii = new Intent(context.getApplicationContext(), MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);

        // Set up style for the notification
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(notificationMessage);
        bigText.setBigContentTitle(notificationTitle);
        bigText.setSummaryText(notificationSummaryText);

        // Build Notification
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_notification_icon);
        mBuilder.setContentTitle(notificationTitle);
        mBuilder.setContentText(notificationMessage);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        // Since oreo you need a notificationchannel... so if this is used with oreo we build it here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        // Notify the user
        mNotificationManager.notify(0, mBuilder.build());

    }
}

