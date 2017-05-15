package org.ekstep.genieservices.commons.bean;

import java.util.List;
import java.util.Map;

/**
 * Created on 5/12/2017.
 *
 * @author anil
 */
public class ContentSearchResult {

    private String id;
    private Map<String, Object> params;
    private List<Map<String, Object>> facets;
    private Map<String, Object> request;
    private List<Content> contents;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<Map<String, Object>> getFacets() {
        return facets;
    }

    public void setFacets(List<Map<String, Object>> facets) {
        this.facets = facets;
    }

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }
}
