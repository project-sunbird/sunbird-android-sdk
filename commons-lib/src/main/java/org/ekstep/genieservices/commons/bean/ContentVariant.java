package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * This class holds name of the content, ecarUrl and size of the content.
 *
 */
public class ContentVariant implements Serializable {

    private String name;
    private String ecarUrl;
    private String size;

    public ContentVariant(String name, String ecarUrl, String size) {
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
