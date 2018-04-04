package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.AnnouncementStatus;
import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * @author indraja on 30/3/18.
 */
public class UpdateAnnouncementStateRequest {

    private String announcementId;
    private AnnouncementStatus announcementStatus;

    private UpdateAnnouncementStateRequest(String announcementId, AnnouncementStatus announcementStatus) {
        this.announcementId = announcementId;
        this.announcementStatus = announcementStatus;
    }

    public String getAnnouncementId() {
        return announcementId;
    }

    public AnnouncementStatus getAnnouncementStatus() {
        return announcementStatus;
    }

    public interface Constant {
        String CHANNEL = "mobile";
    }

    public static class Builder {
        private String announcementId;
        private AnnouncementStatus announcementStatus;

        public Builder forAnnouncement(String announcementId) {
            if (StringUtil.isNullOrEmpty(announcementId)) {
                throw new IllegalArgumentException("announcementId should not be null or empty.");
            }
            this.announcementId = announcementId;
            return this;
        }

        public Builder status(AnnouncementStatus status) {
            if (status == null) {
                throw new IllegalArgumentException("announcement status should not be null.");
            }
            this.announcementStatus = status;
            return this;
        }

        public UpdateAnnouncementStateRequest build() {
            if (StringUtil.isNullOrEmpty(announcementId)) {
                throw new IllegalStateException("announcementId required.");
            }

            if (announcementStatus == null) {
                throw new IllegalStateException("announcement status required.");
            }

            return new UpdateAnnouncementStateRequest(announcementId, announcementStatus);
        }
    }
}
