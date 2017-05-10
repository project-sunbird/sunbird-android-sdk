package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created on 5/4/2017.
 *
 * @author anil
 */
public class ContentFeedback implements Serializable {

    private String contentId;
    private String uid;
    private String rating;
    private String comments;
    private Long createdAt;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
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

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}