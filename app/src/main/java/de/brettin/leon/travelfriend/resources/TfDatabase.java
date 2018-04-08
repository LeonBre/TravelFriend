package de.brettin.leon.travelfriend.resources;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import de.brettin.leon.travelfriend.mapping.TfSetPointAction;


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
        mRef.child(usernameRes.getUsername()).setValue(position.latitude + ":" + position.longitude);
    }

    public DatabaseReference getPointReference() {
        return mRef;
    }
}
