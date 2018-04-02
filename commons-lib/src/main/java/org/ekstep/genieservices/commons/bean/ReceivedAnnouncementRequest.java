package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * @author indraja on 30/3/18.
 */
public class ReceivedAnnouncementRequest {

    private String announcementId;
    private String channel;

    private ReceivedAnnouncementRequest(String announcementId, String channel) {
        this.announcementId = announcementId;
        this.channel = channel;
    }

    public String getAnnouncementId() {
        return announcementId;
    }

    public String getChannel() {
        return channel;
    }

    public static class Builder {

        private String announcementId;
        private String channel;

        public Builder forAnnouncement(String announcementId) {
            if (StringUtil.isNullOrEmpty(announcementId)) {
                throw new IllegalArgumentException("announcementId should not be null or empty.");
            }
            this.announcementId = announcementId;
            return this;
        }

        public Builder channel(String channel) {
            if (StringUtil.isNullOrEmpty(channel)) {
                throw new IllegalArgumentException("channel should not be null or empty.");
            }
            this.channel = channel;
            return this;
        }

        public ReceivedAnnouncementRequest build() {
            if (StringUtil.isNullOrEmpty(announcementId)) {
                throw new IllegalStateException("announcementId required.");
            }

            if (StringUtil.isNullOrEmpty(channel)) {
                throw new IllegalStateException("channel required.");
            }

            return new ReceivedAnnouncementRequest(announcementId, channel);
        }
    }
}
