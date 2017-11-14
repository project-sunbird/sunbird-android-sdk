package org.ekstep.genieservices.contentservice;

import android.util.Log;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.telemetry.model.EventModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Sneha on 5/29/2017.
 */

public class AssertImportTelemetryEvent {
    private static final String TAG = AssertImportTelemetryEvent.class.getSimpleName();

    public static void verifyGeTransferIsLoggedForContentImport(String telemetryEvent) {

        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(telemetryEvent);
        Map eventMap = eventModelList.get(0).getEventMap();

        Log.v(TAG, "eventMap getEID() :::: " + eventMap.get("eid"));
        Assert.assertEquals(telemetryEvent, eventMap.get("eid"));
    }

    public static void verifyGEInteractIsLoggedForContentImportInitiated(String telemetryEvent) {
        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(telemetryEvent);
        Map eventMap = eventModelList.get(0).getEventMap();

        Log.v(TAG, "eventMap getEid() :::: " + eventMap.get("eid"));
        Assert.assertEquals(telemetryEvent, eventMap.get("eid"));

    }

    public static void verifyGEInteractIsLoggedForContentImportSuccess(String telemetryEvent) {
        List<EventModel> eventModelList = GenieServiceDBHelper.findEventById(telemetryEvent);
        Map eventMap = eventModelList.get(0).getEventMap();

        Log.v(TAG, "eventMap getEid() :::: " + eventMap.get("eid"));
        Assert.assertEquals(telemetryEvent, eventMap.get("eid"));
    }
}
