package org.ekstep.genieservices.commons.bean;

/**
 * Created by souvikmondal on 21/3/18.
 */

public class PageAssemble {

    private String name;
    private String id;
    private String ttl;
    private String sections;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }
}
