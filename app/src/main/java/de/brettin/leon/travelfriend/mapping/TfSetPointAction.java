package de.brettin.leon.travelfriend.mapping;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.brettin.leon.travelfriend.model.TfUserPosition;
import de.brettin.leon.travelfriend.resources.TfDatabase;
import de.brettin.leon.travelfriend.resources.TfFirebasePositionConverter;

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

    /**
     * Initiates the listener for the firebase database.
     * When data on the realtime database changes onDataChange is called.
     */
    private void initiateListener() {
        final TfDatabase database = new TfDatabase(mContext);
        mRef = database.getPointReference();

        ValueEventListener positionListener = new ValueEventListener() {

            /**
             * Here the interesting part begins.
             * At this point all current data is loaded from the firebase database and it is possible to add them on the map.
             * @param dataSnapshot Snapshot with all the firebase data.
             */

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    return;
                }

                // Clear all markers on change
                // If you dont clear them you just add and add new markers.
                mGoogleMap.clear();

                // Convert and filter list.
                // There are still some points on the database that you dont want to see.
                TfFirebasePositionConverter converter = new TfFirebasePositionConverter();
                List<TfUserPosition> convertedPositions = converter.convertAndFilter(dataSnapshot, mContext);

                // Sets the convertet points on the map
                TfSetPointAction.this.setPoints(convertedPositions);

                // Updates the cameraposition to see all points on the map
                TfSetPointAction.this.setCameraPosition(convertedPositions);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(this.getClass().getSimpleName(),databaseError.getMessage());
            }
        };
        mRef.addValueEventListener(positionListener);
    }

    /**
     * Sets all markers on the map.
     * @param positions Positions to set.
     */
    private void setPoints(List<TfUserPosition> positions) {
        for (TfUserPosition userPosition: positions) {
            setOnePoint(userPosition);
        }
    }
    private void setOnePoint (TfUserPosition userPosition) {
       MarkerOptions options = TfMarkerFactory.getInstance().createMarker(userPosition, mContext);
        mGoogleMap.addMarker(options);

    }

    /**
     * Set the cameraposition to a good angle.
     * @param convertedPositions Position of all markers to get a good position.
     */
    private void setCameraPosition(List<TfUserPosition> convertedPositions) {
        TfMapCalculation zoomAction = new TfMapCalculation();
        zoomAction.setCameraPosition(convertedPositions, mGoogleMap);
    }
}