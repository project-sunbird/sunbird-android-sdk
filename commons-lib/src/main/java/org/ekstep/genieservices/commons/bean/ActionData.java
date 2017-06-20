package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created by Indraja Machani on 6/20/2017.
 */

public class ActionData implements Serializable {
    private String contentid;
    private String searchquery;
    private String searchsortquery;
    private String searchlanguagequery;
    private String searchdomain;
    private String searchcontenttype;
    private String searchgradelevel;

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getSearchquery() {
        return searchquery;
    }

    public void setSearchquery(String searchquery) {
        this.searchquery = searchquery;
    }

    public String getSearchsortquery() {
        return searchsortquery;
    }

    public void setSearchsortquery(String searchsortquery) {
        this.searchsortquery = searchsortquery;
    }

    public String getSearchlanguagequery() {
        return searchlanguagequery;
    }

    public void setSearchlanguagequery(String searchlanguagequery) {
        this.searchlanguagequery = searchlanguagequery;
    }

    public String getSearchdomain() {
        return searchdomain;
    }

    public void setSearchdomain(String searchdomain) {
        this.searchdomain = searchdomain;
    }

    public String getSearchcontenttype() {
        return searchcontenttype;
    }

    public void setSearchcontenttype(String searchcontenttype) {
        this.searchcontenttype = searchcontenttype;
    }

    public String getSearchgradelevel() {
        return searchgradelevel;
    }

    public void setSearchgradelevel(String searchgradelevel) {
        this.searchgradelevel = searchgradelevel;
    }
}
