package org.ekstep.genieservices.commons.bean;

/**
 * This class holds the contentId and uid used for filtering the feedback of a content.
 *
 */
public class ContentFeedbackFilterCriteria {

    private String uid;
    private String contentId;

    private ContentFeedbackFilterCriteria(String uid, String contentId) {
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

        public Builder byUser(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder forContent(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public ContentFeedbackFilterCriteria build() {
            return new ContentFeedbackFilterCriteria(uid, contentId);
        }
    }
}
