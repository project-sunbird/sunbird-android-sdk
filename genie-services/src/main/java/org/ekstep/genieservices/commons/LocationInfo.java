package org.ekstep.genieservices.commons;

import android.content.Context;

/**
 * Created on 27/4/17.
 * shriharsh
 */

public class LocationInfo implements ILocationInfo {

    private final Context context;

    public LocationInfo(Context context) {
        this.context = context;
    }

    @Override
    public String getLocation() {
        // TODO: 27/4/17 We need to add the logic for sending the location
        return "";
    }
}
