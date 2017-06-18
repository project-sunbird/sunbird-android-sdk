package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * This class accepts contentId while building, and is used when requesting child contents.
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

        public Builder forContent(String contentId) {
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
