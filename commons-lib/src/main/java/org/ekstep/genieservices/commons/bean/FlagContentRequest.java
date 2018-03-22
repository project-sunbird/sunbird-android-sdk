package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 22/3/18.
 *
 * @author IndrajaMachani
 */
public class FlagContentRequest {

    private String contentId;
    private String[] flagReasons;
    private String flaggedBy;
    private String versionKey;
    private String[] flags;

    private FlagContentRequest(String contentId, String[] flagReasons, String flaggedBy, String versionKey, String[] flags) {
        this.contentId = contentId;
        this.flagReasons = flagReasons;
        this.flaggedBy = flaggedBy;
        this.versionKey = versionKey;
        this.flags = flags;
    }

    public String getContentId() {
        return contentId;
    }

    public String[] getFlagReasons() {
        return flagReasons;
    }

    public String getFlaggedBy() {
        return flaggedBy;
    }

    public String getVersionKey() {
        return versionKey;
    }

    public String[] getFlags() {
        return flags;
    }

    public static class Builder {

        private String contentId;
        private String[] flagReasons;
        private String flaggedBy;
        private String versionKey;
        private String[] flags;

        public Builder forContent(String contentId) {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalArgumentException("contentId should not be null or empty.");
            }
            this.contentId = contentId;
            return this;
        }

        public Builder flagReasons(String[] flagReasons) {
            if (CollectionUtil.isEmpty(flagReasons)) {
                throw new IllegalArgumentException("flagReasons should not be null or empty.");
            }
            this.flagReasons = flagReasons;
            return this;
        }

        public Builder flaggedBy(String flaggedBy) {
            if (StringUtil.isNullOrEmpty(flaggedBy)) {
                throw new IllegalArgumentException("flaggedBy should not be null or empty.");
            }
            this.flaggedBy = flaggedBy;
            return this;
        }

        public Builder versionKey(String versionKey) {
            if (StringUtil.isNullOrEmpty(versionKey)) {
                throw new IllegalArgumentException("versionKey should not be null or empty.");
            }
            this.versionKey = versionKey;
            return this;
        }

        public Builder flags(String[] flags) {
            if (CollectionUtil.isEmpty(flags)) {
                throw new IllegalArgumentException("flags should not be null or empty.");
            }
            this.flags = flags;
            return this;
        }

        public FlagContentRequest build() {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalStateException("contentId required.");
            }

            if (CollectionUtil.isEmpty(flagReasons)) {
                throw new IllegalStateException("flagReasons required.");
            }

            if (StringUtil.isNullOrEmpty(flaggedBy)) {
                throw new IllegalStateException("flaggedBy required.");
            }

            if (StringUtil.isNullOrEmpty(versionKey)) {
                throw new IllegalStateException("versionKey required.");
            }

            if (CollectionUtil.isEmpty(flags)) {
                throw new IllegalStateException("flags required.");
            }

            return new FlagContentRequest(contentId, flagReasons, flaggedBy, versionKey, flags);
        }
    }
}
