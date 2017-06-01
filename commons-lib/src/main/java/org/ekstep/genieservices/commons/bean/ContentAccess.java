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
    private Map<String, Object> learnerState;

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

    public Map<String, Object> getLearnerState() {
        return learnerState;
    }

    public void setLearnerState(Map<String, Object> learnerState) {
        this.learnerState = learnerState;
    }
}