package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 13/3/18.
 *
 * @author anil
 */
public class FrameworkDetailsRequest {

    private String frameworkId;
    private boolean refreshFrameworkDetails;
    private boolean defaultFrameworkDetails;
    private String defaultFrameworkPath;

    private FrameworkDetailsRequest(String frameworkId, boolean refreshFrameworkDetails,
                                    boolean defaultFrameworkDetails, String defaultFrameworkPath) {
        this.frameworkId = frameworkId;
        this.refreshFrameworkDetails = refreshFrameworkDetails;
        this.defaultFrameworkDetails = defaultFrameworkDetails;
        this.defaultFrameworkPath = defaultFrameworkPath;
    }

    public String getFrameworkId() {
        return frameworkId;
    }

    public boolean isRefreshFrameworkDetails() {
        return refreshFrameworkDetails;
    }

    public boolean isDefaultFrameworkDetails() {
        return defaultFrameworkDetails;
    }

    public String getDefaultFrameworkPath() {
        return defaultFrameworkPath;
    }

    public static class Builder {

        private String frameworkId;
        private boolean refreshFrameworkDetails;
        private boolean defaultFrameworkDetails;
        private String defaultFrameworkPath;

        public Builder forFramework(String frameworkId) {
            if (StringUtil.isNullOrEmpty(frameworkId)) {
                throw new IllegalArgumentException("frameworkId should not be null or empty.");
            }
            this.frameworkId = frameworkId;
            return this;
        }

        public Builder defaultFrameworkDetails() {
            this.defaultFrameworkDetails = true;
            return this;
        }

        /**
         * The framework details are refreshed from the server only if this flag is set.
         */
        public Builder refreshFrameworkDetailsFromServer() {
            this.refreshFrameworkDetails = true;
            return this;
        }

        public Builder defaultFrameworkPath(String defaultFrameworkPath) {
            if (StringUtil.isNullOrEmpty(defaultFrameworkPath)) {
                throw new IllegalArgumentException("defaultFrameworkPath should not be null or empty.");
            }

            this.defaultFrameworkPath = defaultFrameworkPath;
            return this;
        }

        public FrameworkDetailsRequest build() {
            if (!defaultFrameworkDetails) {
                if (StringUtil.isNullOrEmpty(frameworkId)) {
                    throw new IllegalStateException("frameworkId required.");
                }
            }

            if (StringUtil.isNullOrEmpty(defaultFrameworkPath)) {
                throw new IllegalStateException("defaultFrameworkPath required.");
            }

            return new FrameworkDetailsRequest(frameworkId, refreshFrameworkDetails,
                    defaultFrameworkDetails, defaultFrameworkPath);
        }
    }
}
