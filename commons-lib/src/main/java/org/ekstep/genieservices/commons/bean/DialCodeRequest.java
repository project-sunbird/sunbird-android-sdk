package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created by swayangjit on 07/2/18.
 */
public class DialCodeRequest {

    private String identifier;

    private DialCodeRequest(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static class Builder {
        private String dialCode;

        public Builder dialCode(String dialCode) {
            if (StringUtil.isNullOrEmpty(dialCode)) {
                throw new IllegalArgumentException("dialCode required.");
            }
            this.dialCode = dialCode;
            return this;
        }


        public DialCodeRequest build() {
            if (StringUtil.isNullOrEmpty(dialCode)) {
                throw new IllegalStateException("dialCode required.");
            }

            return new DialCodeRequest(dialCode);
        }
    }
}
