package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.SortOrder;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the uid, types of content required and attachFeedback, attachContentAccess flags if the feedback and content access are required.
 */
public class SummarizerContentFilterCriteria {

    private List<String> uids;
    private String[] contentTypes;
    private boolean attachFeedback;
    private boolean attachContentAccess;
    private List<ContentSortCriteria> sortCriteria;

    private SummarizerContentFilterCriteria(List<String> uids, String[] contentTypes,
                                            boolean attachFeedback, boolean attachContentAccess, List<ContentSortCriteria> sortCriteria) {
        this.uids = uids;
        this.contentTypes = contentTypes;
        this.attachFeedback = attachFeedback;
        this.attachContentAccess = attachContentAccess;
        this.sortCriteria = sortCriteria;
    }

    public List<String> getUid() {
        return uids;
    }

    public String[] getContentTypes() {
        return contentTypes;
    }

    public boolean attachFeedback() {
        return attachFeedback;
    }

    public boolean attachContentAccess() {
        return attachContentAccess;
    }

    public List<ContentSortCriteria> getSortCriteria() {
        return sortCriteria;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {
        private List<String> uids;
        private String[] contentTypes;
        private boolean attachFeedback;
        private boolean attachContentAccess;
        private List<ContentSortCriteria> sortCriteria;

        /**
         * User id to get the content in order to access by that user.
         * And also required when want feedback and content access.
         */
        public Builder forUsers(List<String> uids) {
            this.uids = uids;
            return this;
        }

        public Builder contentTypes(String[] contentTypes) {
            this.contentTypes = contentTypes;
            return this;
        }

        /**
         * Call it if you want feedback, provided by given uid.
         */
        public Builder withFeedback() {
            this.attachFeedback = true;
            return this;
        }

        /**
         * Call it if you want content access by given uid.
         */
        public Builder withContentAccess() {
            this.attachContentAccess = true;
            return this;
        }

        /**
         * List of sort criteria {@link ContentSortCriteria}.
         */
        public Builder sort(List<ContentSortCriteria> sortCriteria) {
            this.sortCriteria = sortCriteria;
            return this;
        }

        public SummarizerContentFilterCriteria build() {
            if (contentTypes == null || contentTypes.length == 0) {
                contentTypes = new String[]{"Story", "Worksheet", "Game", "Collection", "TextBook"};
            }
            if (CollectionUtil.isNullOrEmpty(sortCriteria)) {
                sortCriteria = new ArrayList<>();
                sortCriteria.add(new ContentSortCriteria("lastUsedOn", SortOrder.ASC));
                sortCriteria.add(new ContentSortCriteria("localLastUpdatedOn", SortOrder.ASC));
            }
            return new SummarizerContentFilterCriteria(uids, contentTypes,
                    attachFeedback, attachContentAccess, sortCriteria);
        }
    }
}
