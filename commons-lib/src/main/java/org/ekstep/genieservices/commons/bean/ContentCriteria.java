package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 5/12/2017.
 *
 * @author anil
 */
public class ContentCriteria {

    private String uid;
    private ContentType[] contentTypes;
    private boolean attachFeedback;
    private boolean attachContentAccess;

    private ContentCriteria(String uid, ContentType[] contentTypes, boolean attachFeedback, boolean attachContentAccess) {
        this.uid = uid;
        this.contentTypes = contentTypes;
        this.attachFeedback = attachFeedback;
        this.attachContentAccess = attachContentAccess;
    }

    public String getUid() {
        return uid;
    }

    public ContentType[] getContentTypes() {
        return contentTypes;
    }

    public boolean attachFeedback() {
        return attachFeedback;
    }

    public boolean attachContentAccess() {
        return attachContentAccess;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {
        private String uid;
        private ContentType[] contentTypes;
        private boolean attachFeedback;
        private boolean attachContentAccess;

        /**
         * User id to get the content in order to access by that user.
         * And also required when want feedback and content access.
         */
        public Builder userId(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder contentTypes(ContentType[] contentTypes) {
            this.contentTypes = contentTypes;
            return this;
        }

        /**
         * Pass true if want feedback, provided by given uid else false.
         */
        public Builder withFeedback(boolean b) {
            this.attachFeedback = b;
            return this;
        }

        /**
         * Pass true if want content access by given uid else false.
         */
        public Builder withContentAccess(boolean b) {
            this.attachContentAccess = b;
            return this;
        }

        public ContentCriteria build() {
            return new ContentCriteria(uid, contentTypes, attachFeedback, attachContentAccess);
        }
    }
}
