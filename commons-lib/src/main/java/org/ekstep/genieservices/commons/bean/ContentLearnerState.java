package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * This class holds the data related to learner state in a map object.
 */
public class ContentLearnerState implements Serializable {

    private Map<String, Object> learnerState;

    public Map<String, Object> getLearnerState() {
        return learnerState;
    }

    public void setLearnerState(Map<String, Object> learnerState) {
        this.learnerState = learnerState;
    }
}
