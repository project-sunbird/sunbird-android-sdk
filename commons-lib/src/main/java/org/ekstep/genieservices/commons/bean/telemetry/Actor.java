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
// TODO: Need to revisit
public class Actor {

    public static final String TYPE_SYSTEM = "System";
    public static final String TYPE_USER = "User";

    private String id;
    private String type;

    public Actor() {
        this.type = TYPE_USER;
    }

    public Actor(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
