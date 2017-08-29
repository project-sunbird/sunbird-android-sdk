package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * This class accepts contentId and isChildContent while building it, and is used when deleting a content and set if it is a child content.
 */
public class ContentDeleteRequest {

    private String contentId;
    private boolean isChildContent;

    private ContentDeleteRequest(String contentId, boolean isChildContent) {
        this.contentId = contentId;
        this.isChildContent = isChildContent;
    }

    public String getContentId() {
        return contentId;
    }

    public boolean isChildContent() {
        return isChildContent;
    }

    public static class Builder {
        private String contentId;
        private boolean isChildContent;

        /**
         * Content id which you want to delete
         */
        public Builder contentId(String contentId) {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalArgumentException("Illegal contentId");
            }
            this.contentId = contentId;
            return this;
        }

        /**
         * Call it only if the deleting content is a child content
         */
        public Builder isChildContent() {
            this.isChildContent = true;
            return this;
        }

        public ContentDeleteRequest build() {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalStateException("contentId required.");
            }

            return new ContentDeleteRequest(contentId, isChildContent);
        }
    }
}
