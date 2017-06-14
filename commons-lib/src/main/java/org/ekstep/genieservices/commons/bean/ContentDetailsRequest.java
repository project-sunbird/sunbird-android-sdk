package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/6/2017.
 *
 * @author anil
 */
public class ContentDetailsRequest {

    private String contentId;
    private boolean attachFeedback;
    private boolean attachContentAccess;

    private ContentDetailsRequest(String contentId, boolean attachFeedback, boolean attachContentAccess) {
        this.contentId = contentId;
        this.attachFeedback = attachFeedback;
        this.attachContentAccess = attachContentAccess;
    }

    public String getContentId() {
        return contentId;
    }

    public boolean isAttachFeedback() {
        return attachFeedback;
    }

    public boolean isAttachContentAccess() {
        return attachContentAccess;
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

        public ContentDetailsRequest build() {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalStateException("contentId required.");
            }
            return new ContentDetailsRequest(contentId, attachFeedback, attachContentAccess);
        }
    }
}
