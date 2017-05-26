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
    private String mimeType;
    private boolean status;

    public DownloadResponse(boolean status,long downloadId, String identifier, String filePath,String mimeType) {
        this.status=status;
        this.downloadId = downloadId;
        this.identifier = identifier;
        this.filePath = filePath;
        this.mimeType = mimeType;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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

    public String getMimeType() {
        return mimeType;
    }

    public boolean getStatus() {
        return status;
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
