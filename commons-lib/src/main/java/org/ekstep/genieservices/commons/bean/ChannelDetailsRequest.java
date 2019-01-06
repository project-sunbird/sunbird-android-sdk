package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 12/3/18.
 *
 * @author anil
 */
public class ChannelDetailsRequest {

    private String channelId;
    private String filePath;

    private ChannelDetailsRequest(String channelId, String filePath) {
        this.channelId = channelId;
        this.filePath = filePath;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getFilePath() {
        return filePath;
    }

    public static class Builder {

        private String channelId;
        private String filePath;

        public Builder forChannel(String channelId) {
            if (StringUtil.isNullOrEmpty(channelId)) {
                throw new IllegalArgumentException("channelId should not be null or empty.");
            }
            this.channelId = channelId;
            return this;
        }

        public Builder fromFilePath(String filePath) {
            if (StringUtil.isNullOrEmpty(filePath)) {
                throw new IllegalArgumentException("filePath should not be null or empty.");
            }

            this.filePath = filePath;
            return this;
        }

        public ChannelDetailsRequest build() {
            if (StringUtil.isNullOrEmpty(channelId)) {
                throw new IllegalStateException("channelId required.");
            }

            return new ChannelDetailsRequest(channelId, filePath);
        }
    }
}
