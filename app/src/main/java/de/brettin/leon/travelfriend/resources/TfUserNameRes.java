package de.brettin.leon.travelfriend.resources;

import android.content.Context;

import java.util.UUID;

/**
 * Created by Leon on 06.04.18.
 */

public class TfUserNameRes {

    public static final String USERNAMEKEY = "usernamekey";
    public static final String RANDOMUSERNAME = "randomuser";

    TfSharedPreferences mPreferences;

    public TfUserNameRes (Context context) {
        mPreferences = TfSharedPreferences.getInstance(context);
    }

    /**
     * Gets the username out of the shared preferences.
     * If no username is found a random one will be generated
     * @return Username of the user
     */
    public String getUsername() {
        String username;
        try {
            username = mPreferences.read(USERNAMEKEY);
        } catch (NothingFoundException e) {
            username = RANDOMUSERNAME + UUID.randomUUID().toString().substring(0,4);
            mPreferences.persist(USERNAMEKEY, username);
        }
        return username;

    }

    public void setUsername(String username) {
        mPreferences.persist(USERNAMEKEY, username);
    }
}
