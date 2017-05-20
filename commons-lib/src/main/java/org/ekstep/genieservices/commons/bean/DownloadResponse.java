package org.ekstep.genieservices.commons.bean;

/**
 * Created by swayangjit on 18/5/17.
 */

public class DownloadResponse {

    private long downloadId;
    private String identifier;
    private String filePath;

    public DownloadResponse(long downloadId, String identifier,String filePath) {
        this.downloadId = downloadId;
        this.identifier = identifier;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getDownloadId() {
        return downloadId;
    }

    @Override
    public String toString() {
        return "DownloadResponse{" +
                "downloadId=" + downloadId +
                ", identifier='" + identifier + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
