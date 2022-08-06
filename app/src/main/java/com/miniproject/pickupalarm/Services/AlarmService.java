package com.miniproject.pickupalarm.Services;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

import static com.miniproject.pickupalarm.App.CHANNEL_ID_2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.text.HtmlCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.miniproject.pickupalarm.R;
import com.miniproject.pickupalarm.Receiver.CancelAlarm;
import com.miniproject.pickupalarm.Singletones.Alert;

import java.util.Map;

public class AlarmService extends FirebaseMessagingService {

    private Notification notification;
    private Intent requestIntent;
    private PendingIntent requestPendingIntent;
    private NotificationManager notificationManager;

    private static Uri alarmUri;
    public static Ringtone ringtone;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null)
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.setLooping(true);
        Map<String, String> data = message.getData();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        requestIntent = new Intent(this, CancelAlarm.class);
        requestIntent.setAction("cancel");
        requestPendingIntent = PendingIntent.getBroadcast(this, 0, requestIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setContentTitle(data.get("title"))
                .setContentText(HtmlCompat.fromHtml(data.get("body"), FROM_HTML_MODE_LEGACY))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(getColor(R.color.primary2))
                .addAction(R.drawable.ic_app_icon, "OK", requestPendingIntent)
                .build();

        notificationManager.notify(2, notification);
        ringtone.play();

    }
}
