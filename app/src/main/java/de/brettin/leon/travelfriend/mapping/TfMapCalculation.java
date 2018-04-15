package de.brettin.leon.travelfriend.mapping;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import de.brettin.leon.travelfriend.model.TfUserPosition;

public class TfMapCalculation {
    public void setCameraPosition(List<TfUserPosition> convertedPositions, GoogleMap googleMap) {
        if (convertedPositions.isEmpty()) {
            return;
        }

        googleMap.setMaxZoomPreference(10);


        LatLngBounds.Builder builder = LatLngBounds.builder();

        for (TfUserPosition position: convertedPositions) {
            builder.include(new LatLng(position.getLat(), position.getLng()));
        }

        LatLngBounds bounds = builder.build();

        int padding = 0;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        googleMap.animateCamera(cu);

    }
}
