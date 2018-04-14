package de.brettin.leon.travelfriend.schedule;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.brettin.leon.travelfriend.resources.TfDatabase;
import de.brettin.leon.travelfriend.resources.TfPositionCheckRes;

public class TfUpdatePositionIntentService extends JobService{

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return this.updatePosition();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    private boolean updatePosition() {
        Context context = this.getApplicationContext();

        // If the user doesnt want to get checked we return here
        TfPositionCheckRes checkRes = TfPositionCheckRes.getInstance(context);
        if (!checkRes.shouldCheckPosition()) {
            return true;
        }

        final TfDatabase database = TfDatabase.getInstance(context);

        // Permission check
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        // Update position
        Awareness.getSnapshotClient(context).getLocation().addOnCompleteListener(new OnCompleteListener<LocationResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationResponse> task) {
                Location ownLocation = task.getResult().getLocation();
                database.writeOwnPosition(new LatLng(ownLocation.getLatitude(), ownLocation.getLongitude()));

            }
        });
        return true;
    }
}
