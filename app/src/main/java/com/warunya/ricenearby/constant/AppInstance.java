package com.warunya.ricenearby.constant;

import android.location.Location;

public class AppInstance {

    public static final int DELIVERY_PRICE = 20;
    private static final AppInstance ourInstance = new AppInstance();
    private Location myLocation;
    private Location currentLocation;

    public static AppInstance getInstance() {
        return ourInstance;
    }

    private AppInstance() {

    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
