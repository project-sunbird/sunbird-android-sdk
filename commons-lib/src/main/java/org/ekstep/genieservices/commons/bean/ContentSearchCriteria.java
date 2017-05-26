package org.ekstep.genieservices.commons.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ContentSearchCriteria {

    private static final int DEFAULT_LIMIT = 10;
    private String query;
    private Map<String, String> sort;
    private List<String> facets;
    private Map<String, String[]> filter;
    private long limit;
    private boolean importedContentInfo;

    public ContentSearchCriteria() {
        limit = DEFAULT_LIMIT;
        query = "";
        sort = new HashMap<>();
        filter = new HashMap<>();
        facets = new ArrayList<>();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, String> getSort() {
        return sort;
    }

    public void setSort(Map<String, String> sort) {
        this.sort = sort;
    }

    public List<String> getFacets() {
        return facets;
    }

    public void setFacets(List<String> facets) {
        this.facets = facets;
    }

    public Map<String, String[]> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String[]> filter) {
        this.filter = filter;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public boolean isImportedContentInfo() {
        return importedContentInfo;
    }

    public void setImportedContentInfo(boolean importedContentInfo) {
        this.importedContentInfo = importedContentInfo;
    }
}
