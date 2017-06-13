package org.ekstep.genieservices.commons.bean;

/**
 * Created on 20/5/17.
 *
 * @author swayangjit
 */
public class DownloadProgress {

    private long downloadId;
    private String identifier;
    private int downloadProgress = -1;
    //Status -1 unknown, 0 - not started, 1 - started, 2- success, 3- failed
    private int status = -1;

    private String downloadPath;

    public DownloadProgress(long downloadId) {
        this.downloadId = downloadId;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
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

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
