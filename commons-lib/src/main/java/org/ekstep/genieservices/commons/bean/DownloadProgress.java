package org.ekstep.genieservices.commons.bean;

/**
 * This class holds the data like downloadId, content identifier, download progress and status to represent the state of the download progress.
 * <p>
 * Status -
 * -1 for unknown
 * 0 - not started
 * 1 - started
 * 2- success
 * 3- failed
 */
public class DownloadProgress {

    private long downloadId;
    private String identifier;
    private int downloadProgress = -1;
    //Status -1 unknown, 0 - not started, 1 - started, 2- success, 3- failed
    private int status = -1;

    public DownloadProgress(long downloadId) {
        this.downloadId = downloadId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
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
