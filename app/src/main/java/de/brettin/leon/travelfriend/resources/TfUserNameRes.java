package de.brettin.leon.travelfriend.resources;

import android.content.Context;

import java.util.UUID;

/**
 * Username resource, who saves the username
 */
public class TfUserNameRes {

    // Singleton Pattern
    private static TfUserNameRes mInstance;

    public static TfUserNameRes getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TfUserNameRes(context);
        }
        return mInstance;
    }

    public static final String USERNAMEKEY = "usernamekey";
    public static final String RANDOMUSERNAME = "randomuser";

    TfSharedPreferences mPreferences;

    private TfUserNameRes (Context context) {
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
            mPreferences.write(USERNAMEKEY, username);
        }
        return username;

    }

    public void setUsername(String username) {
        mPreferences.write(USERNAMEKEY, username);
    }
}
