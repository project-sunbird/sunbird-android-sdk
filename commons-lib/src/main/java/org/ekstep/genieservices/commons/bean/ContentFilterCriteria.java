package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.SortOrder;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the uid, types of content required and attachFeedback, attachContentAccess flags if the feedback and content access are required.
 */
public class ContentFilterCriteria {

    private String uid;
    private String[] contentTypes;
    private String[] audience;
    private boolean attachFeedback;
    private boolean attachContentAccess;
    private List<ContentSortCriteria> sortCriteria;

    private ContentFilterCriteria(String uid, String[] contentTypes, String[] audience, boolean attachFeedback, boolean attachContentAccess, List<ContentSortCriteria> sortCriteria) {
        this.uid = uid;
        this.contentTypes = contentTypes;
        this.audience = audience;
        this.attachFeedback = attachFeedback;
        this.attachContentAccess = attachContentAccess;
        this.sortCriteria = sortCriteria;
    }

    public String getUid() {
        return uid;
    }

    public String[] getContentTypes() {
        return contentTypes;
    }

    public String[] getAudience() {
        return audience;
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
        private String uid;
        private String[] contentTypes;
        private String[] audience;
        private boolean attachFeedback;
        private boolean attachContentAccess;
        private List<ContentSortCriteria> sortCriteria;

        /**
         * User id to get the content in order to access by that user.
         * And also required when want feedback and content access.
         */
        public Builder forUser(String uid) {
            this.uid = uid;
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
         * Array of audience. i.e. "Learner", "Instructor".
         */
        public Builder audience(String[] audience) {
            this.audience = audience;
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

        public ContentFilterCriteria build() {
            if (contentTypes == null || contentTypes.length == 0) {
                contentTypes = new String[]{"Story", "Worksheet", "Game", "Collection", "TextBook"};
            }
            if (CollectionUtil.isNullOrEmpty(sortCriteria)) {
                sortCriteria = new ArrayList<>();
                sortCriteria.add(new ContentSortCriteria("lastUsedOn", SortOrder.DESC));
                sortCriteria.add(new ContentSortCriteria("localLastUpdatedOn", SortOrder.DESC));
            }
            return new ContentFilterCriteria(uid, contentTypes, audience, attachFeedback, attachContentAccess, sortCriteria);
        }
    }
}
