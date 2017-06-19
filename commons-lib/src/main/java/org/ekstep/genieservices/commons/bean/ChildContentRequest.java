package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * This class accepts contentId while building, and is used when requesting child contents.
 */

public class ChildContentRequest {

    private String contentId;
    private boolean attachFeedback;
    private boolean attachContentAccess;

    private ChildContentRequest(String contentId, boolean attachFeedback, boolean attachContentAccess) {
        this.contentId = contentId;
        this.attachFeedback = attachFeedback;
        this.attachContentAccess = attachContentAccess;
    }

    public boolean attachFeedback() {
        return attachFeedback;
    }

    public boolean attachContentAccess() {
        return attachContentAccess;
    }

    public String getContentId() {
        return contentId;
    }

    public static class Builder {
        private String contentId;
        private boolean attachFeedback;
        private boolean attachContentAccess;

        public Builder forContent(String contentId) {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalArgumentException("contentId required.");
            }
            this.contentId = contentId;
            return this;
        }

        /**
         * If want feedback, provided by given uid.
         */
        public Builder withFeedback() {
            this.attachFeedback = true;
            return this;
        }

        /**
         * If want content access by given uid.
         */
        public Builder withContentAccess() {
            this.attachContentAccess = true;
            return this;
        }

        public ChildContentRequest build() {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalStateException("contentId required.");
            }
            return new ChildContentRequest(contentId, attachFeedback, attachContentAccess);
        }
    }
}
