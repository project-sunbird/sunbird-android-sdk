package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created by swayangjit on 15/11/17.
 */

public class Visit {
    private String objid;
    private String objtype;
    private String objver;
    private String section;
    private int index;

    public Visit(String objid, String objtype) {
        if (StringUtil.isNullOrEmpty(objid)) {
            throw new IllegalArgumentException("objid can not be null.");
        }

        if (StringUtil.isNullOrEmpty(objtype)) {
            throw new IllegalArgumentException("objecttype can not be null.");
        }

        this.objid = objid;
        this.objtype = objtype;
    }

    public String getObjid() {
        return objid;
    }

    public String getObjtype() {
        return objtype;
    }

    public String getObjver() {
        return objver;
    }

    public void setObjver(String objver) {
        this.objver = objver;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
