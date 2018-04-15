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

/**
 * Factory for the markeroptions.
 * They will get designed here.
 */
public class TfMarkerFactory {
    // Singleton pattern
    private static TfMarkerFactory mInstance;

    public static TfMarkerFactory getInstance() {
        if (mInstance == null) {
            mInstance = new TfMarkerFactory();
        }
        return mInstance;
    }
    //------------------------------------------

    /**
     * Creates a markeroption for the googlemap.
     * The design of the marker is made here.
     * @param userPosition Informations for the marker.
     * @param context The {@link Context}
     * @return Returns the ready to set markeroption for the googlemap
     */
    public MarkerOptions createMarker(TfUserPosition userPosition, Context context) {
        MarkerOptions options = new MarkerOptions();

        // Set position
        LatLng position = new LatLng(userPosition.getLat(), userPosition.getLng());
        options.position(position);

        // Set username
        options.title(userPosition.getUsername());

        // Set icon
        if (TfUserNameRes.getInstance(context).getUsername().equals(userPosition.getUsername())) {
            // Own point of the user
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        } else {
            // Other users
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
