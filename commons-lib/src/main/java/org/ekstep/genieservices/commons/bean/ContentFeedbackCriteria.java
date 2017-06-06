package org.ekstep.genieservices.commons.bean;

/**
 * Created on 6/6/2017.
 *
 * @author anil
 */
public class ContentFeedbackCriteria {

    private String uid;
    private String contentId;

    private ContentFeedbackCriteria(String uid, String contentId) {
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

        public Builder(String uid, String contentId) {
            this.uid = uid;
            this.contentId = contentId;
        }

        public Builder uid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder contentId(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public ContentFeedbackCriteria build() {
            return new ContentFeedbackCriteria(uid, contentId);
        }
    }
}
