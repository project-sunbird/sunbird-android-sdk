package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * @author indraja on 29/3/18.
 */
public class AnnouncementRequest {

    private String announcementId;

    private AnnouncementRequest(String announcementId) {
        this.announcementId = announcementId;
    }

    public String getAnnouncementId() {
        return announcementId;
    }

    public static class Builder {

        private String announcementId;

        public Builder forAnnouncement(String announcementId) {
            if (StringUtil.isNullOrEmpty(announcementId)) {
                throw new IllegalArgumentException("announcementId should not be null or empty.");
            }
            this.announcementId = announcementId;
            return this;
        }

        public AnnouncementRequest build() {
            if (StringUtil.isNullOrEmpty(announcementId)) {
                throw new IllegalStateException("announcementId required.");
            }

            return new AnnouncementRequest(announcementId);
        }
    }
}
