package org.ekstep.genieservices.commons.bean;

/**
 * Created by swayangjit on 17/5/17.
 */

public class TelemetryStat {

    private int unSyncedEventCount;
    private String lastSyncTime;

    public TelemetryStat(int unSyncedEventCount, String lastSyncTime) {
        this.unSyncedEventCount = unSyncedEventCount;
        this.lastSyncTime = lastSyncTime;
    }

    public int getUnSyncedEventCount() {
        return unSyncedEventCount;
    }

    public String getLastSyncTime() {
        return lastSyncTime;
    }


}
