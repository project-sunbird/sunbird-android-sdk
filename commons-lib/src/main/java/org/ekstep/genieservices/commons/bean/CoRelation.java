package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created on 6/5/17.
 *
 * @author swayangjit
 */
public class CoRelation implements Serializable {

    private String type;    // Used to indicate action that is being correlated
    private String id;      // The correlation ID value

    public CoRelation(String id, String type) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
