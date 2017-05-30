package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 5/29/2017.
 *
 * @author anil
 */
public class Section implements Serializable {

    private String responseMessageId;
    private String apiId;
    private Display display;
    private List<Content> contents;
    private ContentSearchCriteria contentSearchCriteria;

    public String getResponseMessageId() {
        return responseMessageId;
    }

    public void setResponseMessageId(String responseMessageId) {
        this.responseMessageId = responseMessageId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public ContentSearchCriteria getContentSearchCriteria() {
        return contentSearchCriteria;
    }

    public void setContentSearchCriteria(ContentSearchCriteria contentSearchCriteria) {
        this.contentSearchCriteria = contentSearchCriteria;
    }
}
