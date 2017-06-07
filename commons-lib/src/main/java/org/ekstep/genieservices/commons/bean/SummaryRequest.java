package org.ekstep.genieservices.commons.bean;

/**
 * Created on 7/6/17.
 * shriharsh
 */

public class SummaryRequest {

    private String uid;
    private String contentId;

    private SummaryRequest(String uid, String contentId) {
        this.uid = uid;
        this.contentId = contentId;
    }

    public String getUid() {
        return uid;
    }

    public String getContentId() {
        return contentId;
    }

    public static class Builder {
        private String uid;
        private String contentId;

        public Builder() {
        }

        public Builder byContent(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder byUser(String uid) {
            this.uid = uid;
            return this;
        }

        public SummaryRequest build() {
            return new SummaryRequest(uid, contentId);
        }
    }

}
