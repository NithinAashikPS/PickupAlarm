package com.miniproject.pickupalarm.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.miniproject.pickupalarm.R;
import com.miniproject.pickupalarm.Singletones.Alert;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Alert alert;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        alert = Alert.getInstance().setActivity(SplashActivity.this);

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mAuth.getCurrentUser() != null)
                                    if (alert.getSelectedContact().size() == 0)
                                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    else
                                        startActivity(new Intent(SplashActivity.this, SelectedPhoneActivity.class));
                                else
                                    startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));
                                finish();
                            }
                        }, 3000);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();
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
}