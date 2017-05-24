package org.ekstep.genieservices.commons.bean;

/**
 * Created on 18/5/17.
 *
 * @author swayangjit
 */
public class DownloadResponse {

    private long downloadId;
    private String identifier;
    private String filePath;

    public DownloadResponse(long downloadId, String identifier, String filePath) {
        this.downloadId = downloadId;
        this.identifier = identifier;
        this.filePath = filePath;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getFilePath() {
        return filePath;
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
