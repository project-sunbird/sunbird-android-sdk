package org.ekstep.genieservices.commons.bean;

/**
 * Created on 20/5/17.
 *
 * @author swayangjit
 */
public class DownloadProgress {

    private long downloadId;
    private String identifier;
    private int downloadProgress;

    public DownloadProgress(String identifier, long downloadId, int downloadProgress) {
        this.identifier = identifier;
        this.downloadId = downloadId;
        this.downloadProgress = downloadProgress;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public String getIdentifier() {
        return identifier;
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
