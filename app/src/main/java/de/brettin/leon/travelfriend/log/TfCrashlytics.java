package de.brettin.leon.travelfriend.log;

import com.crashlytics.android.Crashlytics;

/**
 * Logging class for the crashlytics tool
 */
public class TfCrashlytics {

    /**
     * Log a message to crashlytics
     * @param tag Tag for the message
     * @param msg Message to log
     */
    public static void log(String tag, String msg) {
        Crashlytics.log(1, tag, msg);
    }

    /**
     * Log Exception for Crashlytics
     * @param exception Exception to log
     */
    public static void logException (Throwable exception) {
        Crashlytics.logException(exception);
    }

    /**
     * Log Exception for Crashlytics
     * @param message Message of the exception
     */
    public static void logException (String message) { Crashlytics.logException(new Exception(message));
    }
}
