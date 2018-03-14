package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 13/3/18.
 *
 * @author anil
 */
public class CategoryDetailsRequest {

    private String frameworkId;
    private String channelId;

    private CategoryDetailsRequest(String frameworkId, String channelId) {
        this.frameworkId = frameworkId;
        this.channelId = channelId;
    }

    public String getFrameworkId() {
        return frameworkId;
    }

    public String getChannelId() {
        return channelId;
    }

    public static class Builder {
        private String frameworkId;
        private String channelId;

        public Builder forFramework(String frameworkId) {
            if (StringUtil.isNullOrEmpty(frameworkId)) {
                throw new IllegalArgumentException("frameworkId should not be null or empty.");
            }
            this.frameworkId = frameworkId;
            return this;
        }

        public Builder forChannel(String channelId) {
            if (StringUtil.isNullOrEmpty(channelId)) {
                throw new IllegalArgumentException("channelId should not be null or empty.");
            }
            this.channelId = channelId;
            return this;
        }

        public CategoryDetailsRequest build() {
            if (StringUtil.isNullOrEmpty(frameworkId)) {
                throw new IllegalStateException("frameworkId required.");
            }
            if (StringUtil.isNullOrEmpty(channelId)) {
                throw new IllegalStateException("channelId required.");
            }

            return new CategoryDetailsRequest(frameworkId, channelId);
        }
    }
}
