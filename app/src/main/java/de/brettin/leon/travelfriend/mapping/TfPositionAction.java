package de.brettin.leon.travelfriend.mapping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.brettin.leon.travelfriend.R;
import de.brettin.leon.travelfriend.log.TfCrashlytics;
import de.brettin.leon.travelfriend.resources.TfAction;
import de.brettin.leon.travelfriend.resources.TfPositionCheckRes;
import de.brettin.leon.travelfriend.view.TfNotificationBuilder;
import de.brettin.leon.travelfriend.view.TfNotificationType;

public class TfPositionAction {

    private static final int GPS_ERROR_NOTIFICATION_ID = 3847398;

    /**
     * Update the position of the user
     * @param writeAction Action to write the position in
     * @param context The {@link Context}
     */
    @SuppressLint("MissingPermission")
    public void updatePosition(final TfAction writeAction, final Context context) {
        // Check if the position should be checked
        if (!TfPositionCheckRes.getInstance(context).shouldCheckPosition()) {
            return;
        }

        Awareness.getSnapshotClient(context).getLocation().addOnCompleteListener(new OnCompleteListener<LocationResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationResponse> task) {
                if (!task.isSuccessful()) {

                    // GPS off
                    if (task.getException().getMessage().contains("7503")) {
                        TfCrashlytics.log(TfPositionAction.class.getSimpleName(), "Error 7503: Error occurs when gps is off");

                        // Show the user a message with indicates he should check his connection
                        TfNotificationBuilder builder = new TfNotificationBuilder();
                        builder.buildStandardNotification(context,
                                GPS_ERROR_NOTIFICATION_ID,
                                context.getString(R.string.notification_no_gps_title),
                                context.getString(R.string.notification_no_gps_message),
                                TfNotificationType.ERROR
                                );
                    }

                    TfCrashlytics.logException(task.getException());
                    return;
                }
                Location ownLocation = task.getResult().getLocation();
                LatLng position = new LatLng(ownLocation.getLatitude(), ownLocation.getLongitude());
                writeAction.doAction(position);
            }
        });
    }
}
