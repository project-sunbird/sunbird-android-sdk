package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 6/5/2017.
 *
 * @author anil
 */
public class RelatedContentCriteria {

    private static final int DEFAULT_LIMIT = 10;

    private String contentId;
    private long limit;

    private RelatedContentCriteria(String contentId, long limit) {
        this.contentId = contentId;
        this.limit = limit;
    }

    public String getContentId() {
        return contentId;
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
        private long limit;

        public Builder() {
            this.limit = DEFAULT_LIMIT;
        }

        public Builder contentId(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder limit(long limit) {
            this.limit = limit;
            return this;
        }

        public RelatedContentCriteria build() {
            return new RelatedContentCriteria(contentId, limit);
        }
    }
}
