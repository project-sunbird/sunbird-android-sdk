package org.ekstep.genieservices.commons;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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

    private static Location mLocation;

    private Context context;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                mLocation = location;

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
        public void onConnectionFailed(ConnectionResult connectionResult) {

            if (connectionResult.hasResolution()) {
                try { // Start an Activity that tries to resolve the error
                    connectionResult.startResolutionForResult((Activity) context, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                } catch (IntentSender.SendIntentException e) { // Log the error
                    e.printStackTrace();
                }
            } else {
                connectLocationService();
            }
        }
    };

    public LocationInfo(Context context) {
        this.context = context;

        // Build google api client
        buildGoogleApiClient(context);

        // create a location request
        create();

        connectLocationService();
    }

    @Override
    public String getLocation() {
        String locationString = "";
        if (mLocation != null) {
            if (DateUtil.getTimeDifferenceInHours(mLocation.getTime(), System.currentTimeMillis()) < 2) {    // If hour difference is less than 2 hours than set the lat long.
                double latitude = mLocation.getLatitude();
                double longitude = mLocation.getLongitude();

                if (latitude != 0.0 && longitude != 0.0) {
                    locationString = latitude + "," + longitude;
                }
            }
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
    public void connectLocationService() {
        mGoogleApiClient.connect();
    }

    /**
     * call this method on onStop() in activity
     */
    public void disconnectLocationService() {
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

        // Use high accuracy
        mLocationRequest.setPriority(LocationPriority.PRIORITY_HIGH_ACCURACY);

        // Use low power
        // mLocationRequest.setPriority(PRIORITY_LOW_POWER);
        // Set the interval Fastest interval
        mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
    }

    /**
     * Set the desired interval for active location updates, in milliseconds.
     *
     * @param millis
     */
    public void setInterval(long millis) {
        mLocationRequest.setInterval(millis);
    }

    /**
     * Set the priority of the request.
     *
     * @param priority
     */
    public void setPriority(int priority) {
        mLocationRequest.setPriority(priority);
    }

    /**
     * Explicitly set the fastest interval for location updates, in milliseconds.
     *
     * @param millis
     */
    public void setFastestInterval(long millis) {
        mLocationRequest.setFastestInterval(millis);
    }

    /**
     * Set the duration of this request, in milliseconds. The duration begins immediately (and not
     * when the request is passed to the location client), so call this method again if the request
     * is re-used at a later time. The location client will automatically stop updates after the
     * request expires. The duration includes suspend time. Values less than 0 are allowed, but
     * indicate that the request has already expired.
     *
     * @param millis
     */
    public void setExpirationDuration(long millis) {
        mLocationRequest.setExpirationDuration(millis);
    }

    /**
     * Set the request expiration time, in millisecond since boot. This expiration time uses the
     * same time base as elapsedRealtime(). The location client will automatically stop updates
     * after the request expires. The duration includes suspend time. Values before
     * elapsedRealtime() are allowed, but indicate that the request has already expired.
     *
     * @param millis
     */
    public void setExpirationTime(long millis) {
        mLocationRequest.setExpirationTime(millis);
    }

    /**
     * Set the number of location updates. By default locations are continuously updated until the
     * request is explicitly removed, however you can optionally request a set number of updates.
     * For example, if your application only needs a single fresh location, then call this method
     * with a value of 1 before passing the request to the location client. When using this option
     * care must be taken to either explicitly remove the request when no longer needed or to set an
     * expiration with (setExpirationDuration(long) or setExpirationTime(long). Otherwise in some
     * cases if a location can't be computed, this request could stay active indefinitely consuming
     * power.
     *
     * @param numUpdates
     * @throws IllegalArgumentException if numUpdates is 0 or less
     */
    public void setNumUpdates(int numUpdates) {
        if (numUpdates > 0) {
            mLocationRequest.setNumUpdates(numUpdates);
        }
    }

    /**
     * Set the minimum displacement between location updates in meters By default this is 0.
     *
     * @param smallestDisplacementMeters
     */
    public void setSmallestDisplacement(float smallestDisplacementMeters) {
        mLocationRequest.setSmallestDisplacement(smallestDisplacementMeters);
    }

    /**
     * In response to a request to start updates, send a request to Location Services start periodic
     * location updates
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Logger.i(TAG, "Google PlayerUtil services is available");

            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context, CONNECTION_FAILURE_RESOLUTION_REQUEST).show();

            Logger.i(TAG, "Google PlayerUtil services is not available on your device.");

            return false;
        }
    }

    public interface LocationPriority {

        /**
         * Used with setPriority(int) to request "block" level accuracy. Block level accuracy is
         * considered to be about 100 meter accuracy. Using a coarse accuracy such as this often
         * consumes less power. Constant Value: 102 (0x00000066)
         */
        int PRIORITY_BALANCED_POWER_ACCURACY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

        /**
         * Used with setPriority(int) to request the most accurate locations available. This will return
         * the finest location available. Constant Value: 100 (0x00000064)
         */
        int PRIORITY_HIGH_ACCURACY = LocationRequest.PRIORITY_HIGH_ACCURACY;

        /**
         * Used with setPriority(int) to request "city" level accuracy. City level accuracy is
         * considered to be about 10km accuracy. Using a coarse accuracy such as this often consumes
         * less power. Constant Value: 104 (0x00000068)
         */
        int PRIORITY_LOW_POWER = LocationRequest.PRIORITY_LOW_POWER;

        /**
         * Used with setPriority(int) to request the best accuracy possible with zero additional power
         * consumption. No locations will be returned unless a different client has requested location
         * updates in which case this request will act as a passive listener to those locations.
         * Constant Value: 105 (0x00000069)
         */
        int PRIORITY_NO_POWER = LocationRequest.PRIORITY_NO_POWER;

    }

}
