package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created by souvikmondal on 21/3/18.
 */

public class PageAssembleCriteria {

    private String source;
    private String name;
    private String mode;
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

    public String getMode() {
        return mode;
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

        public Builder mode(String mode) {
            pageAssembleCriteria.mode = mode;
            return this;
        }

        public Builder purpose(String[] purpose) {
            pageAssembleCriteria.filters.setPurpose(purpose);
            return this;
        }

        public Builder channel(String[] channel) {
            pageAssembleCriteria.filters.setChannel(channel);
            return this;
        }

        public Builder subject(String[] subject) {
            pageAssembleCriteria.filters.setSubject(subject);
            return this;
        }

        public Builder grade(String[] grade) {
            pageAssembleCriteria.filters.setGradeLevel(grade);
            return this;
        }

        public Builder medium(String[] medium) {
            pageAssembleCriteria.filters.setMedium(medium);
            return this;
        }

        public Builder ageGroup(String[] ageGroup) {
            pageAssembleCriteria.filters.setAgeGroup(ageGroup);
            return this;
        }

        public Builder language(String[] language) {
            pageAssembleCriteria.filters.setLanguage(language);
            return this;
        }

        public Builder ownership(String[] ownership) {
            pageAssembleCriteria.filters.setOwnership(ownership);
            return this;
        }

        public Builder concepts(String[] concepts) {
            pageAssembleCriteria.filters.setConcepts(concepts);
            return this;
        }

        public Builder dialcodes(String dialcodes) {
            pageAssembleCriteria.filters.setDialcodes(dialcodes);
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
