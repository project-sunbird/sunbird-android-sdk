package org.ekstep.genieservices.commons.bean;


/**
 *
 *
 */
public class GameData {

    private String id;
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
