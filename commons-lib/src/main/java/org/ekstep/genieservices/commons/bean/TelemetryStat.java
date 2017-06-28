package org.ekstep.genieservices.commons.bean;


/**
 * This class holds the details about the telemetry stats.
 *
 */
public class TelemetryStat {

    private int unSyncedEventCount;
    private long lastSyncTime;

    public TelemetryStat(int unSyncedEventCount, long lastSyncTime) {
        this.unSyncedEventCount = unSyncedEventCount;
        this.lastSyncTime = lastSyncTime;
    }

    public int getUnSyncedEventCount() {
        return unSyncedEventCount;
    }

    public long getLastSyncTime() {
        return lastSyncTime;
    }


}
