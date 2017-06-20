package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;

/**
 *This class holds all the data related to a content.
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
    private List<Content> children;
    private List<HierarchyInfo> hierarchyInfo;

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

}
