package de.brettin.leon.travelfriend.log;

import com.crashlytics.android.Crashlytics;

public class TfCrashlytics {

    public static void log(String tag, String msg) {
        Crashlytics.log(1, tag, msg);
    }

    public static void logException (Throwable exception) {
        Crashlytics.logException(exception);
    }

    public static void logException (String message) { Crashlytics.logException(new Exception(message));
    }
}
