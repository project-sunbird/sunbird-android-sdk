package org.ekstep.genieservices.commons;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.Logger;

/**
 * Created on 27/4/17.
 * shriharsh
 */

public class LocationInfo implements ILocationInfo {

    private static final String TAG = LocationInfo.class.getSimpleName();

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // The update interval
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // A fast interval ceiling
    private static final int FAST_CEILING_IN_SECONDS = 1;
    // Update interval in milliseconds
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // A fast ceiling of update intervals, used when the app is visible
    private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 900;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String LAST_KNOWN_LOCATION = "LAST_KNOWN_LOCATION";
    private static final String LAST_KNOWN_LOCATION_TIME = "LAST_KNOWN_LOCATION_TIME";

    private AppContext<Context> mAppContext;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                if (latitude != 0.0 && longitude != 0.0) {
                    String locationString = latitude + "," + longitude;
                    mAppContext.getKeyValueStore().putString(LAST_KNOWN_LOCATION, locationString);
                    mAppContext.getKeyValueStore().putLong(LAST_KNOWN_LOCATION_TIME, location.getTime());
                }

                Logger.i(TAG, "Time: " + location.getTime());
                Logger.i(TAG, "Latitude: " + location.getLatitude());
                Logger.i(TAG, "Longitude: " + location.getLongitude());
                Logger.i(TAG, "Accuracy: " + location.getAccuracy());
            }

            disconnectLocationService();
        }
    };

    /**
     * Location service calls when location is connected or disconnected
     */
    private ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks() {

        @Override
        public void onConnectionSuspended(int arg0) {
            Logger.i(TAG, "Location services disconnected");
        }

        @Override
        public void onConnected(Bundle bundle) {
            if (servicesConnected()) {
                startLocationUpdates();
            }

            Logger.i(TAG, "Location services connected");
        }
    };
    /**
     * Location service calls if an error occurs while connecting to location client
     */
    private OnConnectionFailedListener mConnectionFailedListener = new OnConnectionFailedListener() {

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            if (connectionResult.hasResolution()) {
                try { // Start an Activity that tries to resolve the error
                    connectionResult.startResolutionForResult((Activity) mAppContext.getContext(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                } catch (IntentSender.SendIntentException e) { // Log the error
                    e.printStackTrace();
                }
            } else {
                connectLocationService();
            }
        }
    };

    public LocationInfo(AppContext<Context> appContext) {
        this.mAppContext = appContext;

        // Build google api client
        buildGoogleApiClient(mAppContext.getContext());

        // create a location request
        create();

        connectLocationService();
    }

    @Override
    public String getLocation() {
        String locationString = "";
        if (DateUtil.getTimeDifferenceInHours(mAppContext.getKeyValueStore().getLong(LAST_KNOWN_LOCATION_TIME, 0), System.currentTimeMillis()) < 12) {    // If hour difference is less than 12 hours than set the lat long.
            locationString = mAppContext.getKeyValueStore().getString(LAST_KNOWN_LOCATION, "");
        }
        return locationString;
    }

    /**
     * Build a Google api client
     */
    private synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mConnectionFailedListener)
                .addApi(LocationServices.API).build();
    }

    /**
     * call this method on onStart() in activity
     */
    private void connectLocationService() {
        mGoogleApiClient.connect();
    }

    /**
     * call this method on onStop() in activity
     */
    private void disconnectLocationService() {
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }

        mGoogleApiClient.disconnect();
    }

    /**
     * create a location request..
     */
    private void create() {
        mLocationRequest = LocationRequest.create();

        // Set the update interval
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Set the priority
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        // Set the interval Fastest interval
        mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
    }

    /**
     * In response to a request to start updates, send a request to Location Services start periodic
     * location updates
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mAppContext.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mAppContext.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);

        Logger.i(TAG, "Periodic updates requested");
    }

    /**
     * In response to a request to stop updates, send a request to Location Services stop periodic
     * location updates
     */
    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);

        Logger.i(TAG, "Periodic updates stopped");
    }

    /**
     * Verify that Google PlayerUtil services is available before making a request.
     *
     * @return true if Google PlayerUtil services is available, otherwise false
     */
    private boolean servicesConnected() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(mAppContext.getContext());

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(resultCode)) {
                googleAPI.getErrorDialog((Activity) mAppContext.getContext(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Logger.i(TAG, "This device is not supported.");
            }

            return false;
        }

        return true;
    }

}
