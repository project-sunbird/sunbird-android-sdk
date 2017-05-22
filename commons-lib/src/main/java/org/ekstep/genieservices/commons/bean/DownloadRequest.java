package org.ekstep.genieservices.commons.bean;

/**
 * Created on 17/5/17.
 *
 * @author swayangjit
 */
public class DownloadRequest {

    private long downloadId;
    private String identifier;
    private String downloadUrl;
    private String name;
    private String mimeType;
    private String filePath;


    public DownloadRequest(String identifier, String downloadUrl, String mimeType) {
        this.identifier = identifier;
        this.downloadUrl = downloadUrl;
        this.mimeType = mimeType;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
