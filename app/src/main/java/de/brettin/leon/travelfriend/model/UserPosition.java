package de.brettin.leon.travelfriend.model;

import android.location.Location;

/**
 * Created by Leon on 06.04.18.
 */

public class UserPosition {

    private String username;
    private double lat;
    private double lng;


    public UserPosition(String username, double lat, double lng) {
        this.username = username;
        this.lng = lng;
        this.lat = lat;
    }

    public String getUsername() {
        return username;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }
}
