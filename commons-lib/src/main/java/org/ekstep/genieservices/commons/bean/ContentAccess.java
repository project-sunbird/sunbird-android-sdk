package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.ContentAccessStatus;

import java.io.Serializable;

/**
 * This class holds the data related to content access, for a particular content, like status and learner state.
 */
public class ContentAccess implements Serializable {

    private int status;
    private String contentId;
    private String contentType;
    private ContentLearnerState contentLearnerState;

    public ContentAccessStatus getStatus() {
        ContentAccessStatus contentAccessStatus;

        switch (status) {
            case 1:
                contentAccessStatus = ContentAccessStatus.PLAYED;
                break;

            default:
                contentAccessStatus = ContentAccessStatus.NOT_PLAYED;
                break;
        }

        return contentAccessStatus;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ContentLearnerState getContentLearnerState() {
        return contentLearnerState;
    }

    public void setContentLearnerState(ContentLearnerState contentLearnerState) {
        this.contentLearnerState = contentLearnerState;
    }

}