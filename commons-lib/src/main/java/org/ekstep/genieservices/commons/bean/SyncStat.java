package org.ekstep.genieservices.commons.bean;

/**
 * Created by swayangjit on 17/5/17.
 */

public class SyncStat {

    private int syncedEventCount;
    private String syncTime;
    private String syncedFileSize;

    public SyncStat(int syncedEventCount, String syncTime, String syncedFileSize) {
        this.syncedEventCount = syncedEventCount;
        this.syncTime = syncTime;
        this.syncedFileSize = syncedFileSize;
    }

    public int getSyncedEventCount() {
        return syncedEventCount;
    }

    public String getSyncTime() {
        return syncTime;
    }

    public String getSyncedFileSize() {
        return syncedFileSize;
    }
}
