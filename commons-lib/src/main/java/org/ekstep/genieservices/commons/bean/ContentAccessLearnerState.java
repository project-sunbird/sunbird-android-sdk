package org.ekstep.genieservices.commons.bean;

import java.util.Map;

/**
 * Created on 6/6/2017.
 *
 * @author anil
 */
public class ContentAccessLearnerState {

    private String contentId;
    private Map<String, Object> learnerState;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Map<String, Object> getLearnerState() {
        return learnerState;
    }

    public void setLearnerState(Map<String, Object> learnerState) {
        this.learnerState = learnerState;
    }
}
