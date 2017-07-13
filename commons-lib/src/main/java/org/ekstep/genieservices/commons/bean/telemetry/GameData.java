package org.ekstep.genieservices.commons.bean.telemetry;

/**
 * Data about the game that generated the event.
 */
public class GameData {

    /**
     * unique id assigned to that game
     */
    private String id;

    /**
     * version number of the game
     */
    private String ver;

    public GameData(String id, String ver) {
        this.id = id;
        this.ver = ver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}
