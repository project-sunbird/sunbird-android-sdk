package org.ekstep.genieservices.commons.bean;

/**
 * Created on 6/6/2017.
 *
 * @author anil
 */
public class ContentDeleteCriteria {

    private String contentId;
    private boolean isChildContent;

    private ContentDeleteCriteria(String contentId, boolean isChildContent) {
        this.contentId = contentId;
        this.isChildContent = isChildContent;
    }

    public String getContentId() {
        return contentId;
    }

    public boolean isChildContent() {
        return isChildContent;
    }

    public static class Builder {
        private String contentId;
        private boolean isChildContent;

        public Builder(String contentId, boolean isChildContent) {
            this.contentId = contentId;
            this.isChildContent = isChildContent;
        }

        public Builder contentId(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder childContent(boolean isChildContent) {
            this.isChildContent = isChildContent;
            return this;
        }

        public ContentDeleteCriteria build() {
            return new ContentDeleteCriteria(contentId, isChildContent);
        }
    }
}
