package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * This class accepts contentListingId, subject, age, grade, medium, board, audience array, channel array and device id while building the criteria for content listing request.
 */
public class ContentListingCriteria {

    private String contentListingId;
    private String uid;
    private String language;
    private String subject;
    private int age;
    private int grade;
    private String medium;
    private String board;
    private String did;
    private String[] audience;
    private String[] channel;
    private String[] facets;

    private ContentListingCriteria(String contentListingId, String uid, String language, String subject,
                                   int age, int grade, String medium, String board, String did,
                                   String[] audience, String[] channel, String[] facets) {
        this.contentListingId = contentListingId;
        this.uid = uid;
        this.language = language;
        this.subject = subject;
        this.age = age;
        this.grade = grade;
        this.medium = medium;
        this.board = board;
        this.did = did;
        this.audience = audience;
        this.channel = channel;
        this.facets = facets;
    }

    public String getContentListingId() {
        return contentListingId;
    }

    public String getUid() {
        return uid;
    }

    public String getLanguage() {
        return language;
    }

    public String getSubject() {
        return subject;
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

    public String getDid() {
        return did;
    }

    public String[] getAudience() {
        return audience;
    }

    public String[] getChannel() {
        return channel;
    }

    public String[] getFacets() {
        return facets;
    }

    public static class Builder {
        private String contentListingId;
        private String uid;
        private String language;
        private String subject;
        private int age;
        private int grade;
        private String medium;
        private String board;
        private String did;
        private String[] audience;
        private String[] channel;
        private String[] facets;

        public Builder listingId(String contentListingId) {
            if (StringUtil.isNullOrEmpty(contentListingId)) {
                throw new IllegalArgumentException("listingId required.");
            }
            this.contentListingId = contentListingId;
            return this;
        }

        public Builder forUser(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder forLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder grade(int standard) {
            this.grade = standard;
            return this;
        }

        public Builder medium(String medium) {
            this.medium = medium;
            return this;
        }

        public Builder board(String board) {
            this.board = board;
            return this;
        }

        public Builder deviceId(String did) {
            this.did = did;
            return this;
        }

        public Builder audience(String[] audience) {
            this.audience = audience;
            return this;
        }

        public Builder channel(String[] channel) {
            this.channel = channel;
            return this;
        }

        /**
         * Array of facets. i.e. "contentType", "domain", "ageGroup", "language", "gradeLevel"
         */
        public Builder facets(String[] facets) {
            this.facets = facets;
            return this;
        }

        public ContentListingCriteria build() {
            if (StringUtil.isNullOrEmpty(contentListingId)) {
                throw new IllegalStateException("listingId required.");
            }

            if (facets == null || facets.length == 0) {
                this.facets = new String[]{"contentType", "domain", "ageGroup", "language", "gradeLevel"};
            }

            return new ContentListingCriteria(contentListingId, uid, language, subject, age, grade, medium, board, did, audience, channel, facets);
        }
    }
}
