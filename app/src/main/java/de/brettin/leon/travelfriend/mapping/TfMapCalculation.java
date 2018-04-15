package de.brettin.leon.travelfriend.mapping;

import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import de.brettin.leon.travelfriend.model.TfUserPosition;

/**
 * Calculation class for the map fragment.
 * The class sets the zoom and the positioning of the camera
 */
public class TfMapCalculation {

    // The zoom preferences are just integer on the google maps api.
    private static final float ZOOMPREFERENCE = 6F
            ;

    public void setCameraPosition(List<TfUserPosition> convertedPositions, GoogleMap googleMap) {
        if (convertedPositions.isEmpty()) {
            return;
        }

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (TfUserPosition position: convertedPositions) {
            builder.include(new LatLng(position.getLat(), position.getLng()));
        }
        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), ZOOMPREFERENCE);


        googleMap.animateCamera(cu);

    }
}
