package org.ekstep.genieservices.commons.bean.telemetry;

/**
 * Created by swayangjit on 16/11/17.
 */

public class Actor {
    private String id;
    private String type;

    public Actor() {
    }

    public Actor(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
