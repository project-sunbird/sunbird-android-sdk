package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created by indraja on 29/3/18.
 */

public class Attachment implements Serializable {
    private String name;
    private String mimetype;
    private String size;
    private String link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
