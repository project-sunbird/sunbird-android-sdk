package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * This class accepts contentId,attachFeedback and attachContentAccess while building the request, and is used when requesting content details.
 */
public class ContentDetailsRequest {

    private String contentId;
    private boolean attachFeedback;
    private boolean attachContentAccess;
    private boolean attachContentMarker;
    private boolean refreshContentDetails;

    private ContentDetailsRequest(String contentId, boolean attachFeedback, boolean attachContentAccess,
                                  boolean attachContentMarker, boolean refreshContentDetails) {
        this.contentId = contentId;
        this.attachFeedback = attachFeedback;
        this.attachContentAccess = attachContentAccess;
        this.attachContentMarker = attachContentMarker;
        this.refreshContentDetails = refreshContentDetails;
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

    public boolean isAttachContentMarker() {
        return attachContentMarker;
    }

    public boolean isRefreshContentDetails() {
        return refreshContentDetails;
    }

    public static class Builder {
        private String contentId;
        private boolean attachFeedback;
        private boolean attachContentAccess;
        private boolean attachContentMarker;
        private boolean refreshContentDetails;

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
         * If want content access history by given uid.
         */
        public Builder withContentAccess() {
            this.attachContentAccess = true;
            return this;
        }

        /**
         * If want content access history by given uid.
         */
        public Builder withContentMarker() {
            this.attachContentMarker = true;
            return this;
        }

        /**
         * The content details are refreshed from the server only if this flag is set.
         */
        public Builder refreshContentDetailsFromServer() {
            this.refreshContentDetails = true;
            return this;
        }

        public ContentDetailsRequest build() {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalStateException("contentId required.");
            }
            return new ContentDetailsRequest(contentId, attachFeedback, attachContentAccess,
                    attachContentMarker, refreshContentDetails);
        }
    }
}
