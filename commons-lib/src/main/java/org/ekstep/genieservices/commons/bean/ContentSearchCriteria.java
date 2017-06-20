package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;

/**
 * This class accepts query string, list of {@link ContentSearchFilter}, list of {@link PartnerFilter}, sort by string, limit and profileFilter for searching a content with all
 * set criteria
 *
 */
public class ContentSearchCriteria implements Serializable {

    private static final int DEFAULT_LIMIT = 100;

    private String query;
    private List<ContentSearchFilter> filters;
    private List<PartnerFilter> partnerFilters;
    private String sortBy;
    private long limit;
    private String mode;
    private boolean profileFilter;

    private ContentSearchCriteria(String query, List<ContentSearchFilter> filters, List<PartnerFilter> partnerFilters, String sortBy, long limit, String mode, boolean profileFilter) {
        this.query = query;
        this.filters = filters;
        this.partnerFilters = partnerFilters;
        this.sortBy = sortBy;
        this.limit = limit;
        this.mode = mode;
        this.profileFilter = profileFilter;
    }

    public String getQuery() {
        return query;
    }

    public List<ContentSearchFilter> getFilters() {
        return filters;
    }

    public List<PartnerFilter> getPartnerFilters() {
        return partnerFilters;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public long getLimit() {
        return limit;
    }

    public String getMode() {
        return mode;
    }

    public boolean isProfileFilter() {
        return profileFilter;
    }

    public void setFilter(List<ContentSearchFilter> filters) {
        this.filters = filters;
    }

    public static class Builder {

        private String query;
        private List<ContentSearchFilter> filters;
        private List<PartnerFilter> partnerFilters;
        private String sortBy;
        private long limit;
        private String mode;
        private boolean profileFilter;

        public Builder() {
            this.query = "";
            this.limit = DEFAULT_LIMIT;
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder applyFilters(List<ContentSearchFilter> filters) {
            this.filters = filters;
            return this;
        }

        public Builder applyPartnerFilters(List<PartnerFilter> partnerFilters) {
            this.partnerFilters = partnerFilters;
            return this;
        }

        public Builder sortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public Builder limit(long limit) {
            this.limit = limit;
            return this;
        }

        public Builder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public Builder applyCurrentProfile(boolean profileFilter) {
            this.profileFilter = profileFilter;
            return this;
        }

        public ContentSearchCriteria build() {
            return new ContentSearchCriteria(query, filters, partnerFilters, sortBy, limit, mode, profileFilter);
        }
    }
}
