package de.brettin.leon.travelfriend.resources;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Resource for the position check
 */
public class TfPositionCheckRes {

    //Singleton Pattern
    static TfPositionCheckRes mInstance;

    public static TfPositionCheckRes getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TfPositionCheckRes(context);
        }
        return mInstance;
    }
    //-----------------------------------

    private TfSharedPreferences mPreferences;

    private static final String POSITIONCHECK = "POSITONCHECK";

    private TfPositionCheckRes (Context context) {
        mPreferences = TfSharedPreferences.getInstance(context);

        if (!mPreferences.hasValue(POSITIONCHECK)) {
            mPreferences.writeBoolean(POSITIONCHECK, true);
        }
    }

    public boolean shouldCheckPosition() {
        return mPreferences.readBoolean(POSITIONCHECK, true);
    }

    public void setCheckPosition(boolean value) {
        mPreferences.writeBoolean(POSITIONCHECK, value);
    }

}
