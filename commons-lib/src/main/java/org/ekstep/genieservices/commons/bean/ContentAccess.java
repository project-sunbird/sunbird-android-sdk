package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.ContentAccessStatusType;

import java.io.Serializable;
import java.util.Map;

/**
 * Created on 5/9/2017.
 *
 * @author anil
 */
public class ContentAccess implements Serializable {

    private int status;
    private String contentId;
    private ContentLearnerState contentLearnerState;

    public ContentAccessStatusType getStatus() {
        ContentAccessStatusType contentAccessStatusType;

        switch (status) {
            case 1:
                contentAccessStatusType = ContentAccessStatusType.PLAYED;
                break;

            default:
                contentAccessStatusType = ContentAccessStatusType.NOT_PLAYED;
                break;
        }

        return contentAccessStatusType;
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

    public ContentLearnerState getContentLearnerState() {
        return contentLearnerState;
    }

    public void setContentLearnerState(ContentLearnerState contentLearnerState) {
        this.contentLearnerState = contentLearnerState;
    }

}