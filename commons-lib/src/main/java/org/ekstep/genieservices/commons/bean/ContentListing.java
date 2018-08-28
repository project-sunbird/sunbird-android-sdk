package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * This class holds list of {@link ContentListingSection}, responseMessageId, contentListingId.
 */
public class ContentListing {

    private String contentListingId;
    private String responseMessageId;
    private List<ContentListingSection> contentListingSections;

    public String getContentListingId() {
        return contentListingId;
    }

    public void setContentListingId(String contentListingId) {
        this.contentListingId = contentListingId;
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
