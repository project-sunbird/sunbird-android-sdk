package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * Created on 5/4/2017.
 *
 * @author anil
 */
public class Content {

    private String identifier;
    private ContentData contentData;
    private String mimeType;
    private String path;
    private String contentType;
    private Long lastUpdatedTime;
    private List<ContentFeedback> contentFeedbackList;
    private List<ContentAccess> contentAccessList;
    private boolean isExternalContent;
    private boolean isArtifactAvailable;
    private boolean isAccessedElseWhere;
    private boolean isUpdateAvailable;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public List<ContentFeedback> getContentFeedbackList() {
        return contentFeedbackList;
    }

    public void setContentFeedbackList(List<ContentFeedback> contentFeedbackList) {
        this.contentFeedbackList = contentFeedbackList;
    }

    public List<ContentAccess> getContentAccessList() {
        return contentAccessList;
    }

    public void setContentAccessList(List<ContentAccess> contentAccessList) {
        this.contentAccessList = contentAccessList;
    }

    public boolean isExternalContent() {
        return isExternalContent;
    }

    public void setExternalContent(boolean externalContent) {
        isExternalContent = externalContent;
    }

    public boolean isArtifactAvailable() {
        return isArtifactAvailable;
    }

    public void setArtifactAvailable(boolean artifactAvailable) {
        isArtifactAvailable = artifactAvailable;
    }

    public boolean isAccessedElseWhere() {
        return isAccessedElseWhere;
    }

    public void setAccessedElseWhere(boolean accessedElseWhere) {
        isAccessedElseWhere = accessedElseWhere;
    }

    public boolean isUpdateAvailable() {
        return isUpdateAvailable;
    }

    public void setUpdateAvailable(boolean updateAvailable) {
        isUpdateAvailable = updateAvailable;
    }
}
