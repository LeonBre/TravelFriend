package de.brettin.leon.travelfriend.resources;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.brettin.leon.travelfriend.mapping.TfSetPointAction;
import de.brettin.leon.travelfriend.model.UserPosition;


public class TfDatabase {

    static TfDatabase database;

    public static TfDatabase getInstance(Context context){
        if (database == null) {
            database = new TfDatabase(context);
        }
        return database;
    }

    public static final String POSITIONDATABASENAME = "position";

    DatabaseReference mRef;
    Context mContext;

    private TfDatabase(Context context) {
        mContext = context;

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference(POSITIONDATABASENAME);

    }

    public void writeOwnPosition(LatLng position) {
        TfUserNameRes usernameRes = new TfUserNameRes(mContext);
        Calendar cal = Calendar.getInstance();
        PositionWithTimeStamp valueToSave = new PositionWithTimeStamp(position.latitude, position.longitude, cal.getTimeInMillis());
        mRef.child(usernameRes.getUsername()).setValue(valueToSave);
    }

    public DatabaseReference getPointReference() {
        return mRef;
    }

    public List<UserPosition> convertDataSnapshot(DataSnapshot dataSnapshot) {
        List<UserPosition> result = new ArrayList<>();

        Map<String,PositionWithTimeStamp> data = (Map<String,PositionWithTimeStamp>) dataSnapshot.getValue();

        for (String key : data.keySet()) {
            PositionWithTimeStamp pos = data.get(key);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(pos.timestamp);
            result.add(new UserPosition(key, pos.lat, pos.lng, cal));

        }
        return result;
    }

}

class PositionWithTimeStamp {
    double lat;
    double lng;
    long timestamp;

    public PositionWithTimeStamp(double lat, double lng, long timestamp) {
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
    }
}
