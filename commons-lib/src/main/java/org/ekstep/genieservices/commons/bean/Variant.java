package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Ecar variants.
 * Created on 3/7/2017.
 *
 * @author anil
 */
public class Variant implements Serializable {

    private String name;
    private String ecarUrl;
    private String size;

    public Variant() {
    }

    public Variant(String name, String ecarUrl, String size) {
        this.name = name;
        this.ecarUrl = ecarUrl;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getEcarUrl() {
        return ecarUrl;
    }

    public String getSize() {
        return size;
    }

}
