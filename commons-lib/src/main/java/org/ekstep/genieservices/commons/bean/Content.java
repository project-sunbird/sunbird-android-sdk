package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 5/4/2017.
 *
 * @author anil
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
    private ContentFeedback contentFeedback;
    private ContentAccess contentAccess;
    private List<Content> children;
    private List<HierarchyInfo> childrenHierarchyInfo;

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

    public ContentFeedback getContentFeedback() {
        return contentFeedback;
    }

    public void setContentFeedback(ContentFeedback contentFeedback) {
        this.contentFeedback = contentFeedback;
    }

    public ContentAccess getContentAccess() {
        return contentAccess;
    }

    public void setContentAccess(ContentAccess contentAccess) {
        this.contentAccess = contentAccess;
    }

    public List<Content> getChildren() {
        return children;
    }

    public void setChildren(List<Content> children) {
        this.children = children;
    }

    public List<HierarchyInfo> getChildrenHierarchyInfo() {
        return childrenHierarchyInfo;
    }

    public void setChildrenHierarchyInfo(List<HierarchyInfo> childrenHierarchyInfo) {
        this.childrenHierarchyInfo = childrenHierarchyInfo;
    }

}
