package fr.pa1007.walkingmap.notifiaction;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import fr.pa1007.walkingmap.MainActivity;
import fr.pa1007.walkingmap.R;
import fr.pa1007.walkingmap.notifiaction.receiver.NotificationCloseReceiver;

/**
 * Helper class for showing and canceling start walking app
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class StartWalkingNotification {

    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "StartWakingMap";

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of start walking app notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context, int)
     */
    @SuppressLint("NewApi")

    public static void notify(
            final Context context,
            final String exampleString,
            final int number,
            int id,
            String... strings
    ) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final Resources     res         = context.getResources();
        CharSequence        name        = context.getString(R.string.channel_name);
        String              description = context.getString(R.string.channel_description);
        int                 importance  = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel     = new NotificationChannel("WalkingMap", name, importance);
        channel.setDescription(description);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        NotificationCompat.InboxStyle notifications = new NotificationCompat.InboxStyle()
                .setBigContentTitle(exampleString)
                .setSummaryText("See start information");

        for (String string : strings) {
            notifications.addLine(string);
        }
        // Notification notification = new NotificationCompat.Builder(context, "WalkMap").setPublicVersion().build();

        Intent intentStop = new Intent(context, NotificationCloseReceiver.class);
        intentStop.putExtra("id", id);
        PendingIntent pIntentStop = PendingIntent.getBroadcast(
                context,
                1, intentStop
                , PendingIntent.FLAG_UPDATE_CURRENT
        );

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "WalkingMap")
                .setSmallIcon(R.drawable.ic_stat_start_walking_app_main)
                .setOngoing(true)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(importance)
                .setAutoCancel(true)
                .setNumber(number)
                .setCategory(Notification.CATEGORY_STATUS)
                .setStyle(notifications)
                .addAction(
                        R.drawable.ic_action_stat_share,
                        res.getString(R.string.action_share),
                        PendingIntent.getActivity(
                                context,
                                0,
                                Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                                             .setType("text/plain")
                                                             .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"),
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )
                )
                .addAction(
                        R.drawable.ic_stop_black_24dp,
                        res.getString(R.string.stop_notification_button),
                        pIntentStop
                )

                .setAutoCancel(false);

        notify(context, builder.build(), id);
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String, int, int, String...)}.
     */
    public static void cancel(final Context context, int id) {
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(NOTIFICATION_TAG, id);
    }

    private static void notify(final Context context, final Notification notification, int id) {
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_TAG, id, notification);
    }
}
