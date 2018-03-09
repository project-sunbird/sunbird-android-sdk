package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/3/18.
 * shriharsh
 */
public class UserSearchCriteria {

    private static final int DEFAULT_LIMIT = 100;

    private String query;
    private int offset;
    private int limit;

    private UserSearchCriteria(String query, int offset, int limit) {
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

    public static class SearchBuilder {

        private String query;
        private int offset;
        private int limit;

        public SearchBuilder() {
            this.query = "";
            this.limit = DEFAULT_LIMIT;
        }

        public SearchBuilder searchQuery(String query) {
            if (StringUtil.isNullOrEmpty(query)) {
                throw new IllegalArgumentException("query cannot be empty");
            }

            this.query = query;
            return this;
        }


        public SearchBuilder setOffset(int offset) {
            if (offset > 0) {
                throw new IllegalArgumentException("offset should be greater than 0");
            }

            this.offset = offset;
            return this;
        }

        public SearchBuilder limit(int limit) {
            if (limit > 0) {
                throw new IllegalArgumentException("limit should be greater than 0");
            }

            this.limit = limit;
            return this;
        }

        public UserSearchCriteria build() {
            return new UserSearchCriteria(query, offset, limit);
        }
    }

}
