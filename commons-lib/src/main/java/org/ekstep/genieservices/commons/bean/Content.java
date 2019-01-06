package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.telemetry.Rollup;

import java.io.Serializable;
import java.util.List;

/**
 * This class holds all the data related to a content.
 */
public class Content implements Serializable {

    private String identifier;
    private ContentData contentData;
    private String mimeType;
    private String basePath;
    private String contentType;
    private int referenceCount;
    private Long lastUpdatedTime;
    private boolean isAvailableLocally;
    private boolean isUpdateAvailable;
    private List<ContentFeedback> contentFeedback;
    private List<ContentAccess> contentAccess;
    private List<ContentMarker> contentMarker;
    private List<Content> children;
    private List<HierarchyInfo> hierarchyInfo;
    private Long sizeOnDevice;
    private Long lastUsedTime;
    private Rollup rollup;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public ContentData getContentData() {
        return contentData;
    }

    public void setContentData(ContentData contentData) {
        this.contentData = contentData;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(int referenceCount) {
        this.referenceCount = referenceCount;
    }

    public Long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public boolean isAvailableLocally() {
        return isAvailableLocally;
    }

    public void setAvailableLocally(boolean availableLocally) {
        isAvailableLocally = availableLocally;
    }

    public boolean isUpdateAvailable() {
        return isUpdateAvailable;
    }

    public void setUpdateAvailable(boolean updateAvailable) {
        isUpdateAvailable = updateAvailable;
    }

    public List<ContentFeedback> getContentFeedback() {
        return contentFeedback;
    }

    public void setContentFeedback(List<ContentFeedback> contentFeedback) {
        this.contentFeedback = contentFeedback;
    }

    public List<ContentAccess> getContentAccess() {
        return contentAccess;
    }

    public void setContentAccess(List<ContentAccess> contentAccess) {
        this.contentAccess = contentAccess;
    }

    public List<ContentMarker> getContentMarker() {
        return contentMarker;
    }

    public void setContentMarker(List<ContentMarker> contentMarker) {
        this.contentMarker = contentMarker;
    }

    public List<Content> getChildren() {
        return children;
    }

    public void setChildren(List<Content> children) {
        this.children = children;
    }

    public List<HierarchyInfo> getHierarchyInfo() {
        return hierarchyInfo;
    }

    public void setHierarchyInfo(List<HierarchyInfo> hierarchyInfo) {
        this.hierarchyInfo = hierarchyInfo;
    }

    public Long getSizeOnDevice() {
        return sizeOnDevice;
    }

    public void setSizeOnDevice(Long sizeOnDevice) {
        this.sizeOnDevice = sizeOnDevice;
    }

    public Long getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(Long lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }

    public Rollup getRollup() {
        return rollup;
    }

    public void setRollup(Rollup rollup) {
        this.rollup = rollup;
    }
}
