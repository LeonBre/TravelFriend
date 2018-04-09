package de.brettin.leon.travelfriend.resources;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import de.brettin.leon.travelfriend.model.TfUserPosition;


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
        TfUserNameRes usernameRes = TfUserNameRes.getInstance(mContext);
        Calendar cal = Calendar.getInstance();
        PositionWithTimeStamp valueToSave = new PositionWithTimeStamp(position.latitude, position.longitude, cal.getTimeInMillis());
        mRef.child(usernameRes.getUsername()).setValue(valueToSave);
    }

    @SuppressLint("MissingPermission")
    public void writeOwnPosition() {
        Awareness.getSnapshotClient(mContext).getLocation().addOnCompleteListener(new OnCompleteListener<LocationResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationResponse> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                Location ownLocation = task.getResult().getLocation();
                TfDatabase.this.writeOwnPosition(new LatLng(ownLocation.getLatitude(), ownLocation.getLongitude()));
            }

        });
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
