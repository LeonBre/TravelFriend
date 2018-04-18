package de.brettin.leon.travelfriend.mapping;

import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import de.brettin.leon.travelfriend.model.TfUserPosition;
import de.brettin.leon.travelfriend.resources.TfUserNameRes;

/**
 * Calculation class for the map fragment.
 * The class sets the zoom and the positioning of the camera
 */
public class TfMapCalculation {

    // The zoom preferences are just integer on the google maps api.
    private static final float ZOOMPREFERENCE = 6F;
    private static final float MAXIMUMDISTANCETOUSER_INMETERS = 300 * 1000;

    List<TfUserPosition> mConvertedPositions;
    GoogleMap mGoogleMap;
    Context mContext;

    public TfMapCalculation(List<TfUserPosition> convertedPositions, GoogleMap googleMap, Context context) {
        mConvertedPositions = convertedPositions;
        mGoogleMap = googleMap;
        mContext = context;

    }

    /**
     * Try to set the google maps camera on an optimal position too see the most important points.
     */
    public void setCameraPosition() {
        if (mConvertedPositions.isEmpty()) {
            return;
        }

        LatLngBounds.Builder builder = LatLngBounds.builder();

        // Include points depending on what we have to calculate the camera
        TfUserPosition ownUserPosition = this.ownPosition();
        if (ownUserPosition == null) {
            this.setBuilderWithoutUserPosition(builder);
        } else {
            this.setBuilderWithUserPosition(builder, ownUserPosition);
        }

        // Set Camera
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), ZOOMPREFERENCE);
        mGoogleMap.animateCamera(cu);
    }

    /**
     * Check if the own position of the user is available to calculate with
     * @return The position of the own user or Null
     */
    @Nullable
    private TfUserPosition ownPosition() {
        String username = TfUserNameRes.getInstance(mContext).getUsername();

        for (TfUserPosition userPosition : mConvertedPositions) {
            if (userPosition.getUsername().equals(username)) {
                return userPosition;
            }
        }
        return null;
    }

    /**
     * Just add all points and get the middlepoint
     * @param builder Builder for the camera
     * @return {@link LatLngBounds.Builder} with points included
     */
    private LatLngBounds.Builder setBuilderWithoutUserPosition(LatLngBounds.Builder builder) {
        for (TfUserPosition position: mConvertedPositions) {
            builder.include(new LatLng(position.getLat(), position.getLng()));
        }
        return builder;
    }

    /**
     * Just add all points which are {@value MAXIMUMDISTANCETOUSER_INMETERS} far away
     * @param builder Builder for the camera
     * @param userPosition Position of the user
     * @return {@link LatLngBounds.Builder} with points included
     */
    private LatLngBounds.Builder setBuilderWithUserPosition(LatLngBounds.Builder builder, TfUserPosition userPosition) {
        for (TfUserPosition otherposition : mConvertedPositions) {
            // Calculate distance between positions
            float [] result = new float[1];
            Location.distanceBetween(userPosition.getLat(), userPosition.getLng(),otherposition.getLat(),otherposition.getLng(),result);
            float distanceInMeters = result[0];

            // If the position is too high dont use them in the camera
            if (distanceInMeters < MAXIMUMDISTANCETOUSER_INMETERS) {
                builder.include(new LatLng(otherposition.getLat(), otherposition.getLng()));
            }
        }

        return builder;
    }
}
