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

import java.util.LinkedList;
import java.util.Map;

import de.brettin.leon.travelfriend.model.UserPosition;
import de.brettin.leon.travelfriend.resources.TfDatabase;
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

    public void setPoints(Location ownLocation) {
        this.setOwnPosition(ownLocation);
        this.initiateListener();
    }

    private void setOwnPosition(Location ownLocation) {
        MarkerOptions options = new MarkerOptions();
        LatLng position = new LatLng(ownLocation.getLatitude(), ownLocation.getLongitude());
        options.position(position);

        TfUserNameRes userNameRes = new TfUserNameRes(mContext);
        options.title(userNameRes.getUsername());
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mGoogleMap.addMarker(options);
    }

    private void initiateListener() {
        mRef = TfDatabase.getInstance(mContext).getPointReference();

        ValueEventListener positionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    return;
                }

                Log.d(this.getClass().getSimpleName(), dataSnapshot.getValue().toString());
                Map<String,String> data = (Map<String,String>) dataSnapshot.getValue();

                LinkedList<UserPosition> positionList = new LinkedList<>();
                for (String key : data.keySet()) {
                    String[] positionArray = data.get(key).split(":");

                    // CARE this can make huge trouble!!!
                    positionList.add(new UserPosition(key, Double.parseDouble(positionArray[0]), Double.parseDouble(positionArray[1])));
                }

                TfSetPointAction.this.setOtherPoints(positionList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(this.getClass().getSimpleName(),databaseError.getMessage());
            }
        };
        mRef.addValueEventListener(positionListener);
    }

    private void setOtherPoints(LinkedList<UserPosition> positions) {
        for (UserPosition userPosition: positions) {
            setOnePoint(userPosition);
        }
    }

    private void setOnePoint (UserPosition userPosition) {
        MarkerOptions options = new MarkerOptions();
        LatLng position = new LatLng(userPosition.getLat(), userPosition.getLng());
        options.position(position);
        options.title(userPosition.getUsername());

        mGoogleMap.addMarker(options);
    }

}