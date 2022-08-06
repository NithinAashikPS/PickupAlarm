package com.miniproject.pickupalarm.Singletones;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.miniproject.pickupalarm.Models.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class Alert {

    private static Alert alert = null;
    private static Activity activity;

    private static double latitude;
    private static double longitude;
    private static List<ContactModel> selectedContact;
    private static List<String> regTokens;

    public static Alert getInstance() {
        if (alert == null) {
            alert = new Alert();
        }
        return alert;
    }

    public Activity getActivity() {
        return activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public Alert setActivity(Activity activity) {
        Alert.activity = activity;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        Alert.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        Alert.longitude = longitude;
    }

    public List<ContactModel> getSelectedContact() {
        if (selectedContact == null)
            return new ArrayList<>();
        return selectedContact;
    }

    public void setSelectedContact(List<ContactModel> selectedContact) {
        Alert.selectedContact = selectedContact;
    }

    public static List<String> getRegTokens() {
        if (regTokens == null)
            return new ArrayList<>();
        return regTokens;
    }

    public static void setRegTokens(List<String> regTokens) {
        Alert.regTokens = regTokens;
    }
}
