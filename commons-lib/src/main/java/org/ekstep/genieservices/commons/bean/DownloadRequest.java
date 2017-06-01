package org.ekstep.genieservices.commons.bean;

import java.io.File;

/**
 * Created on 17/5/17.
 *
 * @author swayangjit
 */
public class DownloadRequest {

    private long downloadId = -1;
    private String identifier;
    private String downloadUrl;
    private String name;
    private String mimeType;
    private File destinationFolder;
    private boolean isChildContent;

    public DownloadRequest(String identifier, String downloadUrl, String mimeType, File destinationFolder, boolean isChildContent) {
        this.identifier = identifier;
        this.downloadUrl = downloadUrl;
        this.mimeType = mimeType;
        this.destinationFolder = destinationFolder;
        this.isChildContent = isChildContent;
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

    public boolean isChildContent() {
        return isChildContent;
    }

    public File getDestinationFolder() {
        return destinationFolder;
    }
}
