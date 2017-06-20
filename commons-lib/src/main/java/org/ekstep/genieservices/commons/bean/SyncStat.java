package org.ekstep.genieservices.commons.bean;

/**
 * This class holds data related to the sync state
 *
 */
public class SyncStat {

    private int syncedEventCount;
    private long syncTime;
    private String syncedFileSize;

    public SyncStat(int syncedEventCount, long syncTime, String syncedFileSize) {
        this.syncedEventCount = syncedEventCount;
        this.syncTime = syncTime;
        this.syncedFileSize = syncedFileSize;
    }

    public int getSyncedEventCount() {
        return syncedEventCount;
    }

    public long getSyncTime() {
        return syncTime;
    }

    public String getSyncedFileSize() {
        return syncedFileSize;
    }
}
