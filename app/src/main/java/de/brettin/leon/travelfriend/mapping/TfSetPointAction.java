package de.brettin.leon.travelfriend.mapping;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.brettin.leon.travelfriend.model.TfUserPosition;
import de.brettin.leon.travelfriend.resources.TfDatabase;
import de.brettin.leon.travelfriend.resources.TfFirebasePositionConverter;
import de.brettin.leon.travelfriend.resources.TfUserNameRes;

/**
 * Class how manages the point setting on the googlemap
 */
public class TfSetPointAction {

    Context mContext;
    GoogleMap mGoogleMap;
    DatabaseReference mRef;

    public TfSetPointAction(Context context, GoogleMap googleMap) {
        mContext = context;
        mGoogleMap = googleMap;
    }

    public void setPoints() {
        this.initiateListener();
    }

    public void setOtherPoints() {
        this.initiateListener();
    }

    /**
     * Initiates the listener for the firebase database.
     * When data on the realtime database changes onDataChange is called.
     */
    private void initiateListener() {
        final TfDatabase database = TfDatabase.getInstance(mContext);
        mRef = database.getPointReference();

        ValueEventListener positionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    return;
                }

                // Clear all markers on change
                mGoogleMap.clear();

                // Convert and filter list
                TfFirebasePositionConverter converter = new TfFirebasePositionConverter();
                List<TfUserPosition> convertedPositions = converter.convertAndFilter(dataSnapshot, mContext);

                TfSetPointAction.this.setOtherPoints(convertedPositions);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(this.getClass().getSimpleName(),databaseError.getMessage());
            }
        };
        mRef.addValueEventListener(positionListener);
    }

    private void setOtherPoints(List<TfUserPosition> positions) {
        for (TfUserPosition userPosition: positions) {
            setOnePoint(userPosition);
        }
    }

    private void setOnePoint (TfUserPosition userPosition) {
        MarkerOptions options = new MarkerOptions();
        LatLng position = new LatLng(userPosition.getLat(), userPosition.getLng());
        options.position(position);
        options.title(userPosition.getUsername());

        // Point is own point
        if (TfUserNameRes.getInstance(mContext).getUsername().equals(userPosition.getUsername())) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }

        mGoogleMap.addMarker(options);
    }


    /**
     * ---- UNUSED ----
     * Sets own position on the map
     * @param ownLocation Actual location of the client.
     */
    private void setOwnPosition(Location ownLocation) {
        MarkerOptions options = new MarkerOptions();
        LatLng position = new LatLng(ownLocation.getLatitude(), ownLocation.getLongitude());
        options.position(position);

        options.title(TfUserNameRes.getInstance(mContext).getUsername());
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        mGoogleMap.addMarker(options);
    }
}