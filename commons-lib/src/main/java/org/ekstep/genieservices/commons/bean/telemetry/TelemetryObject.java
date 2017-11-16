package org.ekstep.genieservices.commons.bean.telemetry;

/**
 * Created by swayangjit on 16/11/17.
 */

public class TelemetryObject {

    /**
     * Id of the object. For ex: content id incase of content
     */
    private String id;

    /**
     * Type of the object. For ex: "Content", "Community", "User" etc.
     */
    private String type;

    /**
     * Version of the object
     */
    private String version;

    public TelemetryObject(String id, String type, String version) {
        this.id = id;
        this.type = type;
        this.version = version;
    }
}
