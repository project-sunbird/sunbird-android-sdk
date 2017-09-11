package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class accepts downloadId,identifier,downloadUrl,name,mimeType,destinationFolder,createdAt,isChildContent,processorClass and {@link List<CorrelationData>} whem requesting for a download to begin.
 */
public class DownloadRequest implements Serializable {

    private long downloadId = -1;
    private String identifier;
    private String downloadUrl;
    private String mimeType;
    private String destinationFolder;
    private boolean isChildContent;
    private List<CorrelationData> correlationData;
    private String processorClass;
    private String filename;
    private String downloadedFilePath;
    private Map<String, Object> attributes;

    public DownloadRequest(String identifier, String downloadUrl, String mimeType, String destinationFolder, boolean isChildContent) {
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

    public String getMimeType() {
        return mimeType;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public boolean isChildContent() {
        return isChildContent;
    }

    public List<CorrelationData> getCorrelationData() {
        return correlationData;
    }

    public void setCorrelationData(List<CorrelationData> correlationData) {
        this.correlationData = correlationData;
    }

    public String getProcessorClass() {
        return processorClass;
    }

    public void setProcessorClass(String processorClass) {
        this.processorClass = processorClass;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDownloadedFilePath() {
        return downloadedFilePath;
    }

    public void setDownloadedFilePath(String downloadedFilePath) {
        this.downloadedFilePath = downloadedFilePath;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadRequest that = (DownloadRequest) o;
        return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }
}
