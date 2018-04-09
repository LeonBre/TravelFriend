package de.brettin.leon.travelfriend.resources;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.brettin.leon.travelfriend.model.TfUserPosition;

/**
 * Created by Leon on 09.04.18.
 */

public class TfFirebasePositionConverter {

    public List<TfUserPosition> convertAndFilter(DataSnapshot dataSnapshot, Context context) {
        return filterPositions(convertDataSnapshot(dataSnapshot), context);
    }

    public List<TfUserPosition> convertDataSnapshot(DataSnapshot dataSnapshot) {
        List<TfUserPosition> result = new ArrayList<>();

        Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
        while (iter.hasNext()) {
            DataSnapshot next = iter.next();

            String username = next.getKey();
            PositionWithTimeStamp pos = next.getValue(PositionWithTimeStamp.class);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(pos.timestamp);
            result.add(new TfUserPosition(username, pos.lat, pos.lng, cal));

        }
        return result;
    }

    public List<TfUserPosition> filterPositions (List<TfUserPosition> userPositions, Context context) {
        List<TfUserPosition> result = new LinkedList<>();

        TfUserNameRes userNameRes = new TfUserNameRes(context);
        Calendar twoDaysAgo= Calendar.getInstance();
        twoDaysAgo.add(Calendar.DATE, -2);

        for (TfUserPosition userPosition : userPositions) {
            // Check timestamp
            boolean timestampTooOld = userPosition.getTimestamp().before(twoDaysAgo);
            // Check for own data
            boolean ownUserName = userPosition.getUsername().equals(userNameRes.getUsername());

            if (!(timestampTooOld || ownUserName)) {
                result.add(userPosition);
            }
        }

        return result;
    }
}
