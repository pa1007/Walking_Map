package fr.pa1007.walkingmap.notifiaction.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import fr.pa1007.walkingmap.notifiaction.StartWalkingNotification;

public class NotificationCloseReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        StartWalkingNotification.cancel(context, intent.getIntExtra("id", 1));
    }
}
