package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

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
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalArgumentException("Illegal contentId");
            }
            this.contentId = contentId;
            return this;
        }

        public ContentDetailsRequest build() {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalStateException("contentId required.");
            }
            return new ContentDetailsRequest(contentId);
        }
    }
}
