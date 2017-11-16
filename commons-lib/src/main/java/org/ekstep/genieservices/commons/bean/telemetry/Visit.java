package org.ekstep.genieservices.commons.bean.telemetry;

/**
 * Created by swayangjit on 15/11/17.
 */

public class Visit {
    private String objid = "";
    private String objtype = "";
    private String objver = "";
    private String section = "";
    private int index;

    public Visit(String objid, String objtype, String objver, String section, int index) {
        this.objid = objid;
        this.objtype = objtype;
        this.objver = objver;
        this.section = section;
        this.index = index;

    }
}
