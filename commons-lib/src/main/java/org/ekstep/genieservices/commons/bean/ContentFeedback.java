package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created on 5/4/2017.
 *
 * @author anil
 */
public class ContentFeedback implements Serializable {

    private String contentId;
    private float rating;
    private String comments;
    private Long createdAt;

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

}