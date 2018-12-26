package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 12/18/18.
 *
 * @author anil
 */
public class AcceptTermsAndConditionsRequest {

    private String version;

    private AcceptTermsAndConditionsRequest(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public static class Builder {
        private String version;

        public Builder version(String version) {
            if (StringUtil.isNullOrEmpty(version)) {
                throw new IllegalArgumentException("version should not be null or empty.");
            }
            this.version = version;
            return this;
        }

        public AcceptTermsAndConditionsRequest build() {
            if (StringUtil.isNullOrEmpty(version)) {
                throw new IllegalStateException("version required.");
            }

            return new AcceptTermsAndConditionsRequest(version);
        }
    }
}
