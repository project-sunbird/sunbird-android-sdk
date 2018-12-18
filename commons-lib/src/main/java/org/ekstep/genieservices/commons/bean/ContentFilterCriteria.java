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

    private static final int DEFAULT_LIMIT = 100;

    private String uid;
    private String[] contentTypes;
    private String[] audience;
    private String[] pragma;
    private String[] exclPragma;
    private boolean attachFeedback;
    private boolean attachContentAccess;
    private boolean attachContentMarker;
    private List<ContentSortCriteria> sortCriteria;
    private boolean recentlyViewed;
    private boolean downloadedOnly;
    private long limit;

    private ContentFilterCriteria(String uid, String[] contentTypes, String[] audience, String[] pragma, String[] exclPragma,
                                  boolean attachFeedback, boolean attachContentAccess, boolean attachContentMarker,
                                  List<ContentSortCriteria> sortCriteria,
                                  boolean recentlyViewed, boolean downloadedOnly, long limit) {
        this.uid = uid;
        this.contentTypes = contentTypes;
        this.audience = audience;
        this.pragma = pragma;
        this.exclPragma = exclPragma;
        this.attachFeedback = attachFeedback;
        this.attachContentAccess = attachContentAccess;
        this.attachContentMarker = attachContentMarker;
        this.sortCriteria = sortCriteria;
        this.recentlyViewed = recentlyViewed;
        this.downloadedOnly = downloadedOnly;
        this.limit = limit;
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

    public String[] getPragma() {
        return pragma;
    }

    public String[] getExclPragma() {
        return exclPragma;
    }

    public boolean attachFeedback() {
        return attachFeedback;
    }

    public boolean attachContentAccess() {
        return attachContentAccess;
    }

    public boolean attachContentMarker() {
        return attachContentMarker;
    }

    public List<ContentSortCriteria> getSortCriteria() {
        return sortCriteria;
    }

    public boolean isRecentlyViewed() {
        return recentlyViewed;
    }

    public boolean isDownloadedOnly() {
        return downloadedOnly;
    }

    public long getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {
        private String uid;
        private String[] contentTypes;
        private String[] audience;
        private String[] pragma;
        private String[] exclPragma;
        private boolean attachFeedback;
        private boolean attachContentAccess;
        private boolean attachContentMarker;
        private List<ContentSortCriteria> sortCriteria;
        private boolean recentlyViewed;
        private boolean downloadedOnly;
        private long limit;

        public Builder() {
            this.limit = DEFAULT_LIMIT;
        }

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
         * Array of pragma. i.e. "external", "ads".
         */
        public Builder pragma(String[] pragma) {
            this.pragma = pragma;
            return this;
        }

        /**
         * Array of pragma which needs to exclude from search result. i.e. "external", "ads".
         */
        public Builder excludePragma(String[] pragma) {
            this.exclPragma = pragma;
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
         * Call it if you want content marker by given uid.
         */
        public Builder withContentMarker() {
            this.attachContentMarker = true;
            return this;
        }

        /**
         * List of sort criteria {@link ContentSortCriteria}.
         */
        public Builder sort(List<ContentSortCriteria> sortCriteria) {
            this.sortCriteria = sortCriteria;
            return this;
        }

        /**
         * Call it if you want recently viewed content.
         */
        public Builder recentlyViewed() {
            this.recentlyViewed = true;
            return this;
        }

        /**
         * Call it if you want downloaded content.
         */
        public Builder downloadedOnly() {
            this.downloadedOnly = true;
            return this;
        }

        /**
         * Search results limit.
         */
        public Builder limit(long limit) {
            this.limit = limit;
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
            return new ContentFilterCriteria(uid, contentTypes, audience, pragma, exclPragma,
                    attachFeedback, attachContentAccess, attachContentMarker, sortCriteria,
                    recentlyViewed, downloadedOnly, limit);
        }
    }
}
