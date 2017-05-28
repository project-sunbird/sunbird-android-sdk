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

    private static final int DEFAULT_LIMIT = 10;

    private String query;
    private List<ContentSearchFilter> filter;
    private String sortBy;
    private long limit;
    private String mode;
    private boolean profileFilter;

    public ContentSearchCriteria() {
        limit = DEFAULT_LIMIT;
        query = "";
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<ContentSearchFilter> getFilter() {
        return filter;
    }

    public void setFilter(List<ContentSearchFilter> filter) {
        this.filter = filter;
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

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isProfileFilter() {
        return profileFilter;
    }

    public void setProfileFilter(boolean profileFilter) {
        this.profileFilter = profileFilter;
    }
}
