package com.miniproject.pickupalarm.Services;

import static com.miniproject.pickupalarm.App.CHANNEL_ID_1;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.GetLocationDetail;
import com.example.easywaylocation.Listener;
import com.example.easywaylocation.LocationData;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;
import com.miniproject.pickupalarm.Activities.SelectPhoneNumberActivity;
import com.miniproject.pickupalarm.Activities.SelectedPhoneActivity;
import com.miniproject.pickupalarm.Interfaces.BackendResponseListener;
import com.miniproject.pickupalarm.Interfaces.LocationChanged;
import com.miniproject.pickupalarm.Models.TokenModel;
import com.miniproject.pickupalarm.R;
import com.miniproject.pickupalarm.Singletones.Alert;
import com.miniproject.pickupalarm.Singletones.LiveLocation;
import com.miniproject.pickupalarm.Utils.PickupAlertBackend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LiveLocationService extends Service implements Listener {

    private LiveLocation liveLocation;
    private Notification notification;

    private LocationRequest locationRequest;
    private EasyWayLocation easyWayLocation;
    private NotificationManager notificationManager;

    private Alert alert;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private JSONObject msg;
    private JSONObject data;
    private JSONObject header;

//    private Uri alarmUri;
//    private static Ringtone ringtone;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCreate() {
        super.onCreate();
        liveLocation = LiveLocation.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        alert = Alert.getInstance();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        easyWayLocation = new EasyWayLocation(this, locationRequest, false,false,this);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        data = new JSONObject();
        msg = new JSONObject();
        header = new JSONObject();
//        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if(alarmUri ==null)
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        ringtone = RingtoneManager.getRingtone(alert.getActivity(), alarmUri);
//        ringtone.setLooping(true);
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, SelectedPhoneActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                .setContentTitle("Pickup Alarm")
                .setContentText("Your location is monitoring for pickup alarm.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(getColor(R.color.primary2))
                .setContentIntent(pendingIntent)
                .build();

        liveLocation.setLocationUpdater(new LocationChanged() {
            @Override
            public void newLocation(JSONArray location) {
                try {
                    if (haversine(
                            alert.getLatitude(),
                            alert.getLongitude(),
                            Double.parseDouble(location.get(0).toString()),
                            Double.parseDouble(location.get(1).toString())
                    ) < 2) {
                        easyWayLocation.endUpdates();
                        for (int i=0; i<alert.getSelectedContact().size(); i++) {
                            mDatabase.child(alert.getSelectedContact().get(i).getMobileNumber().replace("+91", "").replace(" ", "")).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        TokenModel tokenModel = task.getResult().getValue(TokenModel.class);
                                        try {
                                            data.put("title", "Reached Destination");
                                            data.put("body", String.format("%s was reached 2km away from the destination.", firebaseAuth.getCurrentUser().getEmail().replace("@gmail.com", "")));
                                            msg.put("to", tokenModel.getFcmToken());
                                            msg.put("data", data);
                                            header.put("Content-Type", "application/json");
                                            header.put("Authorization", alert.getActivity().getResources().getString(R.string.fcm_key));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        new PickupAlertBackend(alert.getActivity(), new BackendResponseListener() {
                                            @Override
                                            public void backendResponse(boolean isError, JSONObject response) {
                                                Log.i("ASDFGH", String.valueOf(response));
                                            }
                                        }).postRequest("https://fcm.googleapis.com/fcm/send", msg, header);
                                    }
                                }
                            });
                        }
                        stopForeground(Service.STOP_FOREGROUND_REMOVE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        startForeground(1, notification);
        easyWayLocation.startLocation();

        return START_NOT_STICKY;
    }

    private double haversine(double lat1, double lon1,
                            double lat2, double lon2)
    {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        easyWayLocation.endUpdates();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void locationOn() {

    }

    @Override
    public void currentLocation(Location location) {
        liveLocation.setLocation(location);
    }

    @Override
    public void locationCancelled() {

    }
}
