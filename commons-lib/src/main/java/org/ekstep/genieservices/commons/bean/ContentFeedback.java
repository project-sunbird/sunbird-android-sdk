package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * This class holds the data related to feedback of a content.
 *
 */
public class ContentFeedback implements Serializable {

    private String contentId;
    private float rating;
    private String comments;
    private Long createdAt;
    private String stageId;
    private String contentVersion;

    public ContentFeedback() {
    }

    public ContentFeedback(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getContentVersion() {
        return contentVersion;
    }

    public void setContentVersion(String contentVersion) {
        this.contentVersion = contentVersion;
    }
}