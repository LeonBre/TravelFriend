package de.brettin.leon.travelfriend.mapping;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.brettin.leon.travelfriend.R;
import de.brettin.leon.travelfriend.model.TfUserPosition;
import de.brettin.leon.travelfriend.resources.TfUserNameRes;

public class TfMarkerFactory {

    public static TfMarkerFactory mInstance;

    public static TfMarkerFactory getInstance() {
        if (mInstance == null) {
            mInstance = new TfMarkerFactory();
        }
        return mInstance;
    }

    public MarkerOptions createMarker(TfUserPosition userPosition, Context context) {
        MarkerOptions options = new MarkerOptions();
        LatLng position = new LatLng(userPosition.getLat(), userPosition.getLng());
        options.position(position);
        options.title(userPosition.getUsername());

        // Point is own point
        if (TfUserNameRes.getInstance(context).getUsername().equals(userPosition.getUsername())) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        } else {
            int height = 103;
            int width = 103;
            BitmapDrawable bitmapdraw=(BitmapDrawable)context.getDrawable(R.drawable.mapicon);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            options.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        }

        return options;
    }
}
