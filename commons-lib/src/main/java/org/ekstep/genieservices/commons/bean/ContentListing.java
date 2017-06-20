package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * This class holds the data related to content listing.
 */
public class ContentListing {

    private String id;
    private String responseMessageId;
    private List<ContentListingSection> contentListingSections;

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

    public List<ContentListingSection> getContentListingSections() {
        return contentListingSections;
    }

    public void setContentListingSections(List<ContentListingSection> contentListingSections) {
        this.contentListingSections = contentListingSections;
    }
}
