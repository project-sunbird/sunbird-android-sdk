package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.List;
import java.util.Set;

/**
 * Created on 6/3/18.
 * shriharsh
 */
public class UserSearchCriteria {

    private static final int DEFAULT_LIMIT = 100;

    private String query;
    private int offset;
    private int limit;
    private Set<String> identifiers;
    private List<String> fields;

    private UserSearchCriteria(String query, int offset, int limit, Set<String> identifiers, List<String> fields) {
        this.query = query;
        this.offset = offset;
        this.limit = limit;
        this.identifiers = identifiers;
        this.fields = fields;
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

    public Set<String> getIdentifiers() {
        return identifiers;
    }

    public List<String> getFields() {
        return fields;
    }

    public static class SearchBuilder {

        private String query;
        private int offset;
        private int limit;
        private Set<String> identifiers;
        private List<String> fields;

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
            if (offset < 0) {
                throw new IllegalArgumentException("offset should be greater than 0");
            }

            this.offset = offset;
            return this;
        }

        public SearchBuilder limit(int limit) {
            if (limit < 0) {
                throw new IllegalArgumentException("limit should be greater than 0");
            }

            this.limit = limit;
            return this;
        }

        public SearchBuilder identifiers(Set<String> identifiers) {
            this.identifiers = identifiers;
            return this;
        }

        public SearchBuilder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }

        public UserSearchCriteria build() {
            return new UserSearchCriteria(query, offset, limit, identifiers, fields);
        }
    }

}
