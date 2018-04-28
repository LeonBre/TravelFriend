package de.brettin.leon.travelfriend.mapping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import de.brettin.leon.travelfriend.R;
import de.brettin.leon.travelfriend.log.TfCrashlytics;
import de.brettin.leon.travelfriend.resources.TfAction;
import de.brettin.leon.travelfriend.resources.TfPositionCheckRes;
import de.brettin.leon.travelfriend.view.TfNotificationBuilder;
import de.brettin.leon.travelfriend.view.TfNotificationType;

/**
 * Action to get the location of the android device
 */
public class TfGetLocationAction implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Code for the GPS Notification id
    // Its random but Im intending not to spam the user so the notification will replace a old one through this id
    private static final int GPS_ERROR_NOTIFICATION_ID = 3847398;

    private Context mContext;

    // Action Class with one method to write the location to the database
    // I did this action object because of better encapsulation
    private TfAction mWriteAction;

    // The following objects are for the FusedLocationApi
    // Google Client to connect to the Play Service
    private GoogleApiClient mGoogleApiClient;
    // Boolean value to check if the api is already conneced
    // --------------------------------------------------------------------------
    // ------The client MUST be connected to get a location other than null------
    // --------------------------------------------------------------------------
    private boolean mGoogleApiIsConnected;
    // Static value to check if the connection should be checked
    private static boolean mAskForConnection = false;

    public TfGetLocationAction(Context context, TfAction writeAction) {
        mContext = context;
        mWriteAction = writeAction;

        mGoogleApiIsConnected = false;
        // Build the google api client for the fusedlocation api
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
         if (mGoogleApiClient != null) {
             mGoogleApiClient.connect();
         }

    }

    /**
     * Updates the position of the user
     */
    public void updatePosition() {
        // Check if the position should be checked
        if (!TfPositionCheckRes.getInstance(mContext).shouldCheckPosition()) {
            return;
        }

        // Check if the google api is already connected
        if (!mGoogleApiIsConnected) {
            mAskForConnection = true;

            // Maybe I get a Exception for double connection here im not sure about this
            mGoogleApiClient.connect();
        } else {
            startLocationSearch();
        }


    }

    /**
     * If everything is set up nicely we can start to search for our location
     */
    @SuppressLint("MissingPermission")
    private void startLocationSearch() {
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    TfCrashlytics.log(TfGetLocationAction.class.getSimpleName(), "Location is null. Maybe the gps is turned off");

                    TfNotificationBuilder notificationBuilder = new TfNotificationBuilder();

                    notificationBuilder.buildStandardNotification(
                            mContext,
                            GPS_ERROR_NOTIFICATION_ID,
                            mContext.getString(R.string.notification_no_gps_title),
                            mContext.getString(R.string.notification_no_gps_message),
                            TfNotificationType.ERROR);

                    Toast.makeText(mContext, mContext.getString(R.string.notification_no_gps_message), Toast.LENGTH_LONG).show();
                    return;
                }

                TfGetLocationAction.this.setLocation(location);
            }
        });
    }

    /**
     * Set location on the database
     * @param location Location to set
     */
    private void setLocation(Location location) {
        // If everything went on fine just write the position
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        mWriteAction.doAction(position);

        // Stop the locationsearch
        mAskForConnection = false;
    }

    /**
     * Google Api is connected
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mGoogleApiIsConnected = true;

        if (mAskForConnection) {
            startLocationSearch();
        }
    }

    // I have no use for this
    @Override
    public void onConnectionSuspended(int i) {
        TfCrashlytics.log(this.getClass().getSimpleName(), "Location supsended :-/");
    }

    // Error on the connection i just log this
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiIsConnected = false;

        TfCrashlytics.logException(connectionResult.getErrorMessage());
    }
}
