package com.miniproject.pickupalarm.Receiver;

import static com.miniproject.pickupalarm.Services.AlarmService.ringtone;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CancelAlarm extends BroadcastReceiver {

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if ("cancel".equals(intent.getAction())) {
            notificationManager.cancel(2);
            ringtone.stop();
        }
    }
}
