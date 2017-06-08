package org.ekstep.genieservices.commons.bean;

/**
 * Created on 6/6/2017.
 *
 * @author anil
 */
public class ContentDetailsRequest {

    private String contentId;
//    private boolean updated or cached;


    private ContentDetailsRequest(String contentId) {
        this.contentId = contentId;
    }

    public String getContentId() {
        return contentId;
    }

    public static class Builder {
        private String contentId;

        public Builder contentId(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public ContentDetailsRequest build() {
            return new ContentDetailsRequest(contentId);
        }
    }
}
