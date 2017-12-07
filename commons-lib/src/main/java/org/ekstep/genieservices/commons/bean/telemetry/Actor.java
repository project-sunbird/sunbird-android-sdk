package org.ekstep.genieservices.commons.bean.telemetry;

/**
 * Created by swayangjit on 16/11/17.
 * <p>
 * Who did the event
 * "actor": { // Required. Actor of the event.
 * "id": , // Required. Id of the actor. For ex: uid incase of an user
 * "type":  // Required. User, System etc.
 * }
 */

public class Actor {

    private String id;
    private String type;
    public Actor() {
    }


    public Actor(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
