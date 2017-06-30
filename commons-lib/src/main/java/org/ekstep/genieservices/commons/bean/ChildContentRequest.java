package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * This class accepts contentId while building it, and is used when requesting child contents.
 */

public class ChildContentRequest {

    private String contentId;
    private int level;
    private boolean attachFeedback;
    private boolean attachContentAccess;

    private ChildContentRequest(String contentId, boolean attachFeedback, boolean attachContentAccess, int level) {
        this.contentId = contentId;
        this.level = level;
        this.attachFeedback = attachFeedback;
        this.attachContentAccess = attachContentAccess;
    }

    public String getContentId() {
        return contentId;
    }

    public int getLevel() {
        return level;
    }

    public boolean attachFeedback() {
        return attachFeedback;
    }

    public boolean attachContentAccess() {
        return attachContentAccess;
    }

    public static class Builder {
        private String contentId;
        private int level;
        private boolean attachFeedback;
        private boolean attachContentAccess;

        public Builder() {
            this.level = -1;
        }

        public Builder forContent(String contentId) {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalArgumentException("contentId required.");
            }
            this.contentId = contentId;
            return this;
        }

        public Builder nextLevelOnly() {
            this.level = 1;
            return this;
        }

        public Builder tillLeafLevel() {
            this.level = -1;
            return this;
        }

        public Builder levelUpto(int level) {
            this.level = level;
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
            return new ChildContentRequest(contentId, attachFeedback, attachContentAccess, level);
        }
    }
}
