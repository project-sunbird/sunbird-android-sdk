package org.ekstep.genieservices.commons;

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

    private FormRequest(String type, String subType, String action, String rootOrgId, String framework) {
        this.type = type;
        this.subType = subType;
        this.action = action;
        this.rootOrgId = rootOrgId;
        this.framework = framework;
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

    public static class Builder {
        private String type;
        private String subType;
        private String action;
        private String rootOrgId;
        private String framework;

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
            return new FormRequest(type, subType, action, rootOrgId, framework);
        }
    }
}
