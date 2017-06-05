package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 6/5/2017.
 *
 * @author anil
 */
public class RecommendedContentCriteria {

    private static final int DEFAULT_LIMIT = 10;

    private String language;
    private long limit;

    private RecommendedContentCriteria(String language, long limit) {
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

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder limit(long limit) {
            this.limit = limit;
            return this;
        }

        public RecommendedContentCriteria build() {
            return new RecommendedContentCriteria(language, limit);
        }
    }
}
