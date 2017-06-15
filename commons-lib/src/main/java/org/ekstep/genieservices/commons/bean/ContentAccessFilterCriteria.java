package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.ContentType;

/**
 * Created on 5/11/2017.
 *
 * @author anil
 */
public class ContentAccessFilterCriteria {

    private String contentId;
    private String uid;

    private ContentAccessFilterCriteria(String contentId, String uid) {
        this.contentId = contentId;
        this.uid = uid;
    }

    public String getContentId() {
        return contentId;
    }

    public String getUid() {
        return uid;
    }

    public static class Builder {
        private String contentId;
        private String uid;

        public Builder forContent(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder byUser(String uid) {
            this.uid = uid;
            return this;
        }

        public ContentAccessFilterCriteria build() {
            return new ContentAccessFilterCriteria(contentId, uid);
        }
    }

}
