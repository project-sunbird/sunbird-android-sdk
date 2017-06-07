package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;

/**
 * This class implements the criteria that would affect/change a search query. It has
 * options like <b>sort</b>, <b>filter</b>, <b>limit</b>, <b>offset</b> and <b>options</b>
 * which can be used to fine tune a search.
 *
 * @query - The keywords used for search.
 * @sort - A map of options and the order to sort "asc"/"desc" to arrange the results in desired order.
 * @filter - A map of options which helps to refine the search results as per the need.
 * @limit - Used for pagination, the number of records in the response of search result.
 * @offset - Used for pagination, the page number of current response.
 * @facets - List of filter options, the result will have all the possible values for each of the options.
 * @options - Other set of conditions in the search.
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

    public long getLimit() {
        return limit;
    }

    public String getMode() {
        return mode;
    }

    public boolean isProfileFilter() {
        return profileFilter;
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
