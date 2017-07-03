package org.ekstep.genieservices.commons.bean;

/**
 * This class accepts contentListingId, subject, age, grade, medium, board, audience array, channel array and device id while building the criteria for content listing request.
 */
public class ContentListingCriteria {

    private String contentListingId;
    private String subject;
    private int age;
    private int grade;
    private String medium;
    private String board;
    private String[] audience;
    private String[] channel;
    private String did;

    public ContentListingCriteria(String contentListingId, String subject, int age, int grade, String medium, String board, String did, String[] audience, String[] channel) {
        this.contentListingId = contentListingId;
        this.subject = subject;
        this.age = age;
        this.grade = grade;
        this.medium = medium;
        this.board = board;
        this.audience = audience;
        this.channel = channel;
        this.did = did;
    }

    public String getContentListingId() {
        return contentListingId;
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

    public String[] getAudience() {
        return audience;
    }

    public String[] getChannel() {
        return channel;
    }

    public String getDid() {
        return did;
    }

    public static class Builder {

        private String contentListingId;
        private String subject;
        private int age;
        private int grade;
        private String medium;
        private String board;
        private String[] audience;
        private String[] channel;
        private String did;

        public Builder listingId(String contentListingId) {
            this.contentListingId = contentListingId;
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

        public Builder channel(String[] audience) {
            this.channel = channel;
            return this;
        }

        public ContentListingCriteria build() {
            return new ContentListingCriteria(contentListingId, subject, age, grade, medium, board, did, audience, channel);
        }
    }
}
