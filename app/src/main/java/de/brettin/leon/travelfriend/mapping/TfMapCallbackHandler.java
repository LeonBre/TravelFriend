package de.brettin.leon.travelfriend.mapping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.brettin.leon.travelfriend.resources.TfDatabase;
import de.brettin.leon.travelfriend.resources.TfPositionCheckRes;

/**
 * Handler to work with the map, when the map has finished loading
 */
public class TfMapCallbackHandler implements OnMapReadyCallback {

    Context mContext;

    public TfMapCallbackHandler(Context context) {
        mContext = context;

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Move the camera to nelson
        LatLng nelson = new LatLng(-41, 171);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(nelson));

        if (TfPositionCheckRes.getInstance(mContext).shouldCheckPosition()) {
            Awareness.getSnapshotClient(mContext).getLocation().addOnCompleteListener(new OnCompleteListener<LocationResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationResponse> task) {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    Location ownLocation = task.getResult().getLocation();
                    TfDatabase.getInstance(mContext).writeOwnPosition(new LatLng(ownLocation.getLatitude(), ownLocation.getLongitude()));
                    TfSetPointAction pointAction = new TfSetPointAction(mContext, googleMap);
                    pointAction.setPoints(ownLocation);
                }

            });
        } else {

            // Just set the other points and not the own one
            TfSetPointAction pointAction = new TfSetPointAction(mContext, googleMap);
            pointAction.setOtherPoints();
        }
    }
}
