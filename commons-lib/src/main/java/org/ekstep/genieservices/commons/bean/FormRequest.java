package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created by swayangjit on 29/5/18.
 */
public class FormRequest {

    private String type;
    private String subType;
    private String action;
    private String rootOrgId;
    private String framework;
    private String defaultFormPath;
    private Double defaultTtl;  // In hours

    private FormRequest(String type, String subType, String action, String rootOrgId, String framework,
                        String defaultFormPath, Double defaultTtl) {
        this.type = type;
        this.subType = subType;
        this.action = action;
        this.rootOrgId = rootOrgId;
        this.framework = framework;
        this.defaultFormPath = defaultFormPath;
        this.defaultTtl = defaultTtl;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public String getAction() {
        return action;
    }

    public String getRootOrgId() {
        return rootOrgId;
    }

    public String getFramework() {
        return framework;
    }

    public String getDefaultFormPath() {
        return defaultFormPath;
    }

    public Double getDefaultTtl() {
        return defaultTtl;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (!StringUtil.isNullOrEmpty(type)) {
            builder.append(type);
        }
        if (!StringUtil.isNullOrEmpty(subType)) {
            builder.append(subType);
        }
        if (!StringUtil.isNullOrEmpty(action)) {
            builder.append(action);
        }
        if (!StringUtil.isNullOrEmpty(rootOrgId)) {
            builder.append(rootOrgId);
        }
        if (!StringUtil.isNullOrEmpty(framework)) {
            builder.append(false);
        }

        return builder.toString();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public static class Builder {
        private String type;
        private String subType;
        private String action;
        private String rootOrgId;
        private String framework;
        private String defaultFormPath;
        private Double defaultTtl;

        public Builder() {
            this.defaultTtl = 3d;
        }

        public Builder type(String type) {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalArgumentException("type required.");
            }
            this.type = type;
            return this;
        }

        public Builder subType(String subType) {
            if (StringUtil.isNullOrEmpty(subType)) {
                throw new IllegalArgumentException("subType required.");
            }
            this.subType = subType;
            return this;
        }

        public Builder action(String action) {
            if (StringUtil.isNullOrEmpty(action)) {
                throw new IllegalArgumentException("action required.");
            }
            this.action = action;
            return this;
        }

        public Builder rootOrgId(String rootOrgId) {
            this.rootOrgId = rootOrgId;
            return this;
        }

        public Builder framework(String framework) {
            this.framework = framework;
            return this;
        }

        public Builder defaultFormPath(String defaultFormPath) {
            if (StringUtil.isNullOrEmpty(defaultFormPath)) {
                throw new IllegalArgumentException("defaultFormPath should not be null or empty.");
            }

            this.defaultFormPath = defaultFormPath;
            return this;
        }

        public Builder defaultTtl(Double defaultTtl) {
            this.defaultTtl = defaultTtl;
            return this;
        }

        public FormRequest build() {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalStateException("type required.");
            }

            if (StringUtil.isNullOrEmpty(subType)) {
                throw new IllegalStateException("subType required.");
            }

            if (StringUtil.isNullOrEmpty(action)) {
                throw new IllegalStateException("action required.");
            }

            if (StringUtil.isNullOrEmpty(defaultFormPath)) {
                throw new IllegalStateException("defaultFormPath required.");
            }

            return new FormRequest(type, subType, action, rootOrgId, framework, defaultFormPath, defaultTtl);
        }
    }
}
