package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * This class accepts language and limit when requesting for a recommended content with respect to a particular content.
 */
public class RelatedContentRequest {

    private static final int DEFAULT_LIMIT = 10;

    private String contentId;
    private String uid;
    private String language;
    private long limit;

    private RelatedContentRequest(String contentId, String uid, String language, long limit) {
        this.contentId = contentId;
        this.uid = uid;
        this.language = language;
        this.limit = limit;
    }

    public String getContentId() {
        return contentId;
    }

    public String getUid() {
        return uid;
    }

    public String getLanguage() {
        return language;
    }

    public long getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {

        private String contentId;
        private String uid;
        private String language;
        private long limit;

        public Builder() {
            this.limit = DEFAULT_LIMIT;
        }

        public Builder forContent(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder byUser(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder byLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder limit(long limit) {
            this.limit = limit;
            return this;
        }

        public RelatedContentRequest build() {
            return new RelatedContentRequest(contentId, uid, language, limit);
        }
    }
}
