package de.brettin.leon.travelfriend.schedule;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.brettin.leon.travelfriend.resources.TfDatabase;

/**
 * IntentService to update the current location of the user in the firebase database.
 *
 * Explanation:
 * You need a Service to work with the Firebase Tools.
 * Just doing it in the BroadcastReceiver wont work, because the Firebase Database would crash when you try to do it directly from there.
 * The BroadcastReceiver is not designed for such complex tasks. You need to start a service for this much computation "power"...
 * So I start the Service over an BroadcastReceiver.
 */
public class TfUpdatePositionIntentService extends IntentService{

    /**
     * Updates the Position of the user.
     * @param intent <-- not needed
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context = this.getApplicationContext();
        final TfDatabase database = TfDatabase.getInstance(context);

        // Permission check
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Update position
        Awareness.getSnapshotClient(context).getLocation().addOnCompleteListener(new OnCompleteListener<LocationResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationResponse> task) {
                Location ownLocation = task.getResult().getLocation();
                database.writeOwnPosition(new LatLng(ownLocation.getLatitude(), ownLocation.getLongitude()));

            }
        });
    }

    // Some Boilerplate Code:
    public TfUpdatePositionIntentService() {super("TfUpdatePositionIntentService");}
    public TfUpdatePositionIntentService(String name) {super(name);}
}
