package com.beckoningtech.fastnsafe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.util.ArrayList;

import static android.app.Notification.PRIORITY_MAX;
import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationAdder {
    public static final ArrayList IDStorage = new ArrayList();
    // addNotification()
    // desc: adds a notification
    // args: notificationID is the id of the notification, used to ref same notis later
    //      context should be calling activity context
    public static void addNotification(Class destination, int notificationID, Context given, String title, String text) {

        IDStorage.add(new Integer(notificationID));

        // Intent launches the main activity
        Intent intention = new Intent(given, destination);
        intention.setAction("callChangeOngoing");
        PendingIntent pIntent = PendingIntent.getActivity(given, 0, intention, 0);

        // Building notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(given);
        mBuilder.setSmallIcon(R.drawable.checkin);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setOngoing(false);
        mBuilder.setPriority(PRIORITY_MAX);
        mBuilder.setContentIntent(pIntent);
//        mBuilder.addAction(R.drawable.sample_notification, "testing1", pIntent);
//        mBuilder.addAction(R.drawable.sample_notification, "testing2", pIntent);
//        mBuilder.addAction(R.drawable.sample_notification, "testing3", pIntent);

        // Notification launching
        NotificationManager mNotificationManager = (NotificationManager) given.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, mBuilder.build());
    }


    // removeNotification()
    // desc: removes a notification
    // args: notificationID is the id of the noti we want to remove
    //      context should be calling activity context
    public static void removeNotification(int notificationID, Context given) {
        IDStorage.remove(new Integer(notificationID));
        // Notification removal
        NotificationManager mNotificationManager = (NotificationManager) given.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notificationID);
    }

    public static boolean checkNotification(int notificationID){
        return IDStorage.contains(new Integer(notificationID));
    }
}
