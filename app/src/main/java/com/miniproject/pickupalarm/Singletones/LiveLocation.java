package com.miniproject.pickupalarm.Singletones;

import android.location.Location;

import com.miniproject.pickupalarm.Interfaces.LocationChanged;

import org.json.JSONArray;
import org.json.JSONException;

public class LiveLocation {

    private static LiveLocation liveLocation = null;

    private static JSONArray locationData = null;

    private Location location;
    private String pinCode;
    private LocationChanged locationChanged = null;

    public static LiveLocation getInstance() {
        if (liveLocation == null) {
            liveLocation = new LiveLocation();
            locationData = new JSONArray();
        }
        return liveLocation;
    }

    public LiveLocation setLocationUpdater(LocationChanged locationChanged) {
        this.locationChanged = locationChanged;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {

        locationData = new JSONArray();
        try {
            locationData.put(location.getLatitude());
            locationData.put(location.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        locationChanged.newLocation(locationData);
        this.location = location;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
