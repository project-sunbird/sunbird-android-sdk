package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * This class holds the data in id, responseMessageId and {@link List<ContentData>} after requesting for related content.
 *
 */
public class RelatedContentResult {

    private String id;
    private String responseMessageId;
    private List<Content> relatedContents;

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

    public List<Content> getRelatedContents() {
        return relatedContents;
    }

    public void setRelatedContents(List<Content> relatedContents) {
        this.relatedContents = relatedContents;
    }
}
