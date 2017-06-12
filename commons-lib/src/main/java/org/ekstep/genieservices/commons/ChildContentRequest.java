package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 12/6/17.
 * shriharsh
 */

public class ChildContentRequest {
    private String contentId;

    private ChildContentRequest(String contentId) {
        this.contentId = contentId;
    }

    public String getContentId() {
        return contentId;
    }

    public static class Builder {
        private String contentId;

        public ChildContentRequest.Builder contentId(String contentId) {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalArgumentException("contentId required.");
            }
            this.contentId = contentId;
            return this;
        }

        public ChildContentRequest build() {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalStateException("contentId required.");
            }
            return new ChildContentRequest(contentId);
        }
    }
}
