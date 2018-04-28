package de.brettin.leon.travelfriend.resources;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import de.brettin.leon.travelfriend.mapping.TfGetLocationAction;

/**
 * Database which interacts with firebase database
 */
public class TfDatabase {

    private static final String POSITIONDATABASENAME = "position";

    private DatabaseReference mRef;
    private Context mContext;

    public TfDatabase(Context context) {
        mContext = context;

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference(POSITIONDATABASENAME);

    }

    /**
     * Writes the own position in the database
     * @param position Position to write
     */
    public void writeOwnPosition(LatLng position) {
        TfUserNameRes usernameRes = TfUserNameRes.getInstance(mContext);
        Calendar cal = Calendar.getInstance();
        PositionWithTimeStamp valueToSave = new PositionWithTimeStamp(position.latitude, position.longitude, cal.getTimeInMillis());
        mRef.child(usernameRes.getUsername()).setValue(valueToSave);
    }


    /**
     * Updates the own position by calling {@link com.google.android.gms.awareness.Awareness}
     * @param context The {@link Context}
     */
    public void updateOwnPosition(Context context) {
        TfAction writeAction = new TfAction() {
            @Override
            public void doAction(Object o) {
                LatLng position = (LatLng) o;
                TfDatabase.this.writeOwnPosition(position);
            }
        };

        TfGetLocationAction updatePosition = new TfGetLocationAction();
        updatePosition.updatePosition(writeAction, context);

    }

    public DatabaseReference getPointReference() {
        return mRef;
    }

}

class PositionWithTimeStamp {
    public double lat;
    public double lng;
    public long timestamp;

    public PositionWithTimeStamp(double lat, double lng, long timestamp) {
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
    }

    //None argument constructor for firebase
    public PositionWithTimeStamp(){}
}
