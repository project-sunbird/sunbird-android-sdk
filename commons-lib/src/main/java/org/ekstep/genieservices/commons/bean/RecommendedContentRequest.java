package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * This class accepts language and limit when requesting for a recommended content with respect to a particular language.
 */
public class RecommendedContentRequest {

    private static final int DEFAULT_LIMIT = 10;

    private String language;
    private long limit;

    private RecommendedContentRequest(String language, long limit) {
        this.language = language;
        this.limit = limit;
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

        private String language;
        private long limit;

        public Builder() {
            this.limit = DEFAULT_LIMIT;
        }

        public Builder byLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder limit(long limit) {
            this.limit = limit;
            return this;
        }

        public RecommendedContentRequest build() {
            return new RecommendedContentRequest(language, limit);
        }
    }
}
