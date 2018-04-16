package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created by souvikmondal on 21/3/18.
 */

public class PageAssembleCriteria {

    private String source;
    private String name;
    private PageAssembleFilter filters;

    private PageAssembleCriteria() {
        this.source = "app";
        this.filters = new PageAssembleFilter();
    }

    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public PageAssembleFilter getFilters() {
        return filters;
    }

    private boolean isValid() {
        return !StringUtil.isNullOrEmpty(name);
    }

    public static class Builder {

        private PageAssembleCriteria pageAssembleCriteria;

        public Builder() {
            pageAssembleCriteria = new PageAssembleCriteria();
        }

        public Builder name(String name) {
            pageAssembleCriteria.name = name;
            return this;
        }

        public Builder source(String source) {
            pageAssembleCriteria.source = source;
            return this;
        }

        public Builder subject(String subject) {
            pageAssembleCriteria.filters.setSubject(subject);
            return this;
        }

        public Builder grade(String grade) {
            pageAssembleCriteria.filters.setGrade(grade);
            return this;
        }

        public Builder medium(String medium) {
            pageAssembleCriteria.filters.setMedium(medium);
            return this;
        }

        public PageAssembleCriteria build() throws IllegalStateException {
            if (!pageAssembleCriteria.isValid()) {
                throw new IllegalStateException();
            }

            return pageAssembleCriteria;
        }

    }
}
