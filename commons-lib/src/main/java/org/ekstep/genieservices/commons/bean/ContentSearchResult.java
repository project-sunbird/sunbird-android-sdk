package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created on 5/12/2017.
 *
 * @author anil
 */
public class ContentSearchResult implements Serializable {

    private String id;
    private String responseMessageId;
    private List<ContentSearchFilter> filter;
    private Map<String, Object> request;
    private List<Content> contents;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResponseMessageId() {
        return responseMessageId;
    }

    public void setResponseMessageId(String responseMessageId) {
        this.responseMessageId = responseMessageId;
    }

    public List<ContentSearchFilter> getFilter() {
        return filter;
    }

    public void setFilter(List<ContentSearchFilter> filter) {
        this.filter = filter;
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
