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
    private ContentType[] contentTypes;

    private ContentAccessFilterCriteria(String contentId, String uid, ContentType[] contentTypes) {
        this.contentId = contentId;
        this.uid = uid;
        this.contentTypes = contentTypes;
    }

    public String getContentId() {
        return contentId;
    }

    public String getUid() {
        return uid;
    }

    public ContentType[] getContentTypes() {
        return contentTypes;
    }

    public static class Builder {
        private String contentId;
        private String uid;
        private ContentType[] contentTypes;

        public Builder contentId(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder uid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder contentTypes(ContentType[] contentTypes) {
            this.contentTypes = contentTypes;
            return this;
        }

        public ContentAccessFilterCriteria build() {
            return new ContentAccessFilterCriteria(contentId, uid, contentTypes);
        }
    }

}
