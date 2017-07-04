package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.SearchType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class accepts query string, {@link List<ContentSearchFilter>}, age, grade, medium, board, audience array, channel array, sort criteria, limit and mode for searching a content with all
 * set criteria
 */
public class ContentSearchCriteria implements Serializable {

    private static final int DEFAULT_LIMIT = 100;

    private String query;
    private List<ContentSearchFilter> facetFilters;
    private List<ContentSearchFilter> impliedFilters;
    private List<ContentSortCriteria> sortCriteria;
    private long limit;
    private String mode;
    private int age;
    private int grade;
    private String medium;
    private String board;
    private String[] audience;
    private String[] channel;
    private List<String> status;
    private List<String> facets;
    // 1 - indicates search, 2 - filter
    private SearchType searchType;

    private ContentSearchCriteria(String query, long limit, String mode, int age, int grade, String medium, String board,
                                  String[] audience, String[] channel, List<String> status, List<String> facets, List<ContentSortCriteria> sortCriteria,
                                  SearchType searchType) {
        this.query = query;
        this.limit = limit;
        this.mode = mode;
        this.age = age;
        this.grade = grade;
        this.medium = medium;
        this.board = board;
        this.audience = audience;
        this.channel = channel;
        this.status = status;
        this.facets = facets;
        this.sortCriteria = sortCriteria;
        this.searchType = searchType;
    }

    private ContentSearchCriteria(String query, long limit, String mode, List<ContentSearchFilter> facetFilters, List<ContentSearchFilter> impliedFilters, List<ContentSortCriteria> sortCriteria, SearchType searchType) {
        this.query = query;
        this.limit = limit;
        this.mode = mode;
        this.facetFilters = facetFilters;
        this.impliedFilters = impliedFilters;
        this.sortCriteria = sortCriteria;
        this.searchType = searchType;
    }

    public String getQuery() {
        return query;
    }

    public int getAge() {
        return age;
    }

    public int getGrade() {
        return grade;
    }

    public String getMedium() {
        return medium;
    }

    public String getBoard() {
        return board;
    }

    public String[] getAudience() {
        return audience;
    }

    public String[] getChannel() {
        return channel;
    }

    public List<String> getStatus() {
        return status;
    }

    public List<String> getFacets() {
        return facets;
    }

    public List<ContentSearchFilter> getImpliedFilters() {
        return impliedFilters;
    }

    public List<ContentSortCriteria> getSortCriteria() {
        return sortCriteria;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public List<ContentSearchFilter> getFacetFilters() {
        return facetFilters;
    }

    public long getLimit() {
        return limit;
    }

    public String getMode() {
        return mode;
    }

    public static class SearchBuilder {

        private String query;
        private long limit;
        private String mode;
        private int age;
        private int grade;
        private String medium;
        private String board;
        private String[] audience;
        private String[] channel;
        private List<ContentSortCriteria> sortCriteria;
        private List<String> status;
        private List<String> facets;

        public SearchBuilder() {
            this.query = "";
            this.limit = DEFAULT_LIMIT;
        }

        public SearchBuilder query(String query) {
            this.query = query;
            return this;
        }

        public SearchBuilder limit(long limit) {
            this.limit = limit;
            return this;
        }

        public SearchBuilder sort(List<ContentSortCriteria> sortCriteria) {
            this.sortCriteria = sortCriteria;
            return this;
        }

        public SearchBuilder softFilters() {
            this.mode = "soft";
            return this;
        }

        public SearchBuilder age(int age) {
            this.age = age;
            return this;
        }

        public SearchBuilder grade(int standard) {
            this.grade = standard;
            return this;
        }

        public SearchBuilder medium(String medium) {
            this.medium = medium;
            return this;
        }

        public SearchBuilder board(String board) {
            this.board = board;
            return this;
        }

        public SearchBuilder audience(String[] audience) {
            this.audience = audience;
            return this;
        }

        public SearchBuilder channel(String[] audience) {
            this.channel = channel;
            return this;
        }

        public SearchBuilder status(List<String> status) {
            this.status = status;
            return this;
        }

        public ContentSearchCriteria build() {
            if (status == null || status.isEmpty()) {
                this.status = Collections.singletonList("Live");
            }

            if (facets == null || facets.isEmpty()) {
                this.facets = Arrays.asList("contentType", "domain", "ageGroup", "language", "gradeLevel");
            }

            return new ContentSearchCriteria(query, limit, mode, age, grade, medium, board,
                    audience, channel, status, facets, sortCriteria, SearchType.SEARCH);
        }
    }


    public static class FilterBuilder {

        private String query;
        private long limit;
        private String mode;
        private List<ContentSearchFilter> facetFilters;
        private List<ContentSearchFilter> impliedFilters;
        private List<ContentSortCriteria> sortCriteria;

        public FilterBuilder() {
            this.query = "";
            this.limit = DEFAULT_LIMIT;
        }

        public FilterBuilder query(String query) {
            this.query = query;
            return this;
        }

        public FilterBuilder limit(long limit) {
            this.limit = limit;
            return this;
        }

        public FilterBuilder impliedFilters(List<ContentSearchFilter> impliedFilters) {
            this.impliedFilters = impliedFilters;
            return this;
        }

        public FilterBuilder facetFilters(List<ContentSearchFilter> facetFilters) {
            this.facetFilters = facetFilters;
            return this;
        }

        public FilterBuilder sort(List<ContentSortCriteria> sortCriteria) {
            this.sortCriteria = sortCriteria;
            return this;
        }

        public FilterBuilder softFilters() {
            this.mode = "soft";
            return this;
        }

        public ContentSearchCriteria build() {
            return new ContentSearchCriteria(query, limit, mode, facetFilters, impliedFilters, sortCriteria, SearchType.FILTER);
        }
    }
}
