package de.brettin.leon.travelfriend.model;

import android.location.Location;

import java.util.Calendar;

/**
 * Representation of the position in the firebase database
 */
public class UserPosition {

    private String username;
    private Calendar timestamp;
    private double lat;
    private double lng;


    public UserPosition(String username, double lat, double lng, Calendar timestamp) {
        this.username = username;
        this.lng = lng;
        this.lat = lat;
        this.timestamp = timestamp;
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

    public Calendar getTimestamp (){return timestamp;}
}
