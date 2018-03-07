package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/3/18.
 * shriharsh
 */

public class SearchUserRequest {
    private String query;
    private int offset;
    private int limit;

    private SearchUserRequest(String query, int offset, int limit) {
        this.query = query;
        this.offset = offset;
        this.limit = limit;
    }

    public String getQuery() {
        return query;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public static class Builder {

        private String query;
        private int offset;
        private int limit;

        public Builder searchQuery(String query) {
            if (StringUtil.isNullOrEmpty(query)) {
                throw new IllegalArgumentException("query cannot be empty");
            }

            this.query = query;
            return this;
        }


        public Builder setOffset(int offset) {
            if (offset > 0) {
                throw new IllegalStateException("offset should be greater than 0");
            }

            this.offset = offset;
            return this;
        }

        public Builder setLimit(int limit) {
            if (limit > 0) {
                throw new IllegalStateException("offset should be greater than 0");
            }

            this.limit = limit;
            return this;
        }

        public SearchUserRequest build() {

            return new SearchUserRequest(query, offset, limit);
        }

    }

}
