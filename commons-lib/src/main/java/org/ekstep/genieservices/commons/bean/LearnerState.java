package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created on 5/9/2017.
 *
 * @author anil
 */
public class LearnerState implements Serializable {

    private String tuIdentifier;

    public String getTuIdentifier() {
        return tuIdentifier;
    }

    public void setTuIdentifier(String tuIdentifier) {
        this.tuIdentifier = tuIdentifier;
    }
}