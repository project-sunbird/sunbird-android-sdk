package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 12/3/18.
 *
 * @author anil
 */
public class ChannelDetailsRequest {

    private String channelId;
    private boolean refreshChannelDetails;

    private ChannelDetailsRequest(String channelId, boolean refreshChannelDetails) {
        this.channelId = channelId;
        this.refreshChannelDetails = refreshChannelDetails;
    }

    public String getChannelId() {
        return channelId;
    }

    public boolean isRefreshChannelDetails() {
        return refreshChannelDetails;
    }

    public static class Builder {

        private String channelId;
        private boolean refreshChannelDetails;

        public Builder forChannel(String channelId) {
            if (StringUtil.isNullOrEmpty(channelId)) {
                throw new IllegalArgumentException("channelId should not be null or empty.");
            }
            this.channelId = channelId;
            return this;
        }

        /**
         * The channel details are refreshed from the server only if this flag is set.
         */
        public Builder refreshChannelDetailsFromServer() {
            this.refreshChannelDetails = true;
            return this;
        }

        public ChannelDetailsRequest build() {
            if (StringUtil.isNullOrEmpty(channelId)) {
                throw new IllegalStateException("channelId required.");
            }

            return new ChannelDetailsRequest(channelId, refreshChannelDetails);
        }
    }
}
