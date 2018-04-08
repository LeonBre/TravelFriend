package de.brettin.leon.travelfriend.resources;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

/**
 * Own implementation of shared preferences
 */
public class TfSharedPreferences {

    private static TfSharedPreferences mInstance;

    /**
     * Singleton Pattern for Shared Preferences
     * (Not 100% sure if this is necessary)
     * @return Instance of shared preferences
     */
    public static TfSharedPreferences getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TfSharedPreferences(context);
        }
        return mInstance;
    }

    // ------------------------------------------------------------


    private SharedPreferences mSharedPreferences;

    public TfSharedPreferences(Context context) {
        mSharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void persist(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public String read(String key, String def) {
        return mSharedPreferences.getString(key, def);
    }

    public String read(String key) throws NothingFoundException{
        String value = mSharedPreferences.getString(key, null);
        if (value == null) {
            throw new NothingFoundException();
        }
        return value;
    }
}
