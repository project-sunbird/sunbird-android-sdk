package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * Created on 6/5/2017.
 *
 * @author anil
 */
public class RecommendedContentResult {

    private String id;
    private String responseMessageId;
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

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }
}
