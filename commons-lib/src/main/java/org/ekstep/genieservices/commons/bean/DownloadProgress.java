package org.ekstep.genieservices.commons.bean;

/**
 * Created by swayangjit on 20/5/17.
 */

public class DownloadProgress {
    private String identifier;
    private long downloadId;
    private int downloadProgress;

    public DownloadProgress(String identifier, long downloadId, int downloadProgress) {
        this.identifier = identifier;
        this.downloadId = downloadId;
        this.downloadProgress = downloadProgress;
    }

    public String getIdentifier() {
        return identifier;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    @Override
    public String toString() {
        return "DownloadProgress{" +
                "identifier='" + identifier + '\'' +
                ", downloadId=" + downloadId +
                ", downloadProgress=" + downloadProgress +
                '}';
    }
}
