package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 15/11/17.
 *
 * @author swayangjit
 */
public class Error extends Telemetry {

    private static final String EID = "ERROR";

    private Error(String errorCode, String errorType, String stacktrace, String pageId) {
        super(EID);
        setEData(createEData(errorCode, errorType, stacktrace, pageId));
    }

    private Map<String, Object> createEData(String errorCode, String errorType, String stacktrace, String pageId) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("err", errorCode);
        eData.put("errtype", errorType);
        eData.put("stacktrace", stacktrace);
        if (!StringUtil.isNullOrEmpty(pageId)) {
            eData.put("pageid", pageId);
        }
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public interface Type {
        String SYSTEM = "SYSTEM";
        String MOBILE_APP = "MOBILEAPP";
        String CONTENT = "CONTENT";
    }

    public static class Builder {
        private String errorCode;
        private String errorType;
        private String stacktrace;
        private String pageId;

        /**
         * Error code.
         */
        public Builder errorCode(String errorCode) {
            if (StringUtil.isNullOrEmpty(errorCode)) {
                throw new IllegalArgumentException("errorCode should not be null or empty.");
            }
            this.errorCode = errorCode;
            return this;
        }

        /**
         * Error type classification - "SYSTEM", "MOBILEAPP", "CONTENT"
         */
        public Builder errorType(String errorType) {
            if (StringUtil.isNullOrEmpty(errorType)) {
                throw new IllegalArgumentException("errorType should not be null or empty.");
            }
            this.errorType = errorType;
            return this;
        }

        /**
         * Detailed error data/stack trace.
         */
        public Builder stacktrace(String stacktrace) {
            if (StringUtil.isNullOrEmpty(stacktrace)) {
                throw new IllegalArgumentException("stacktrace should not be null or empty.");
            }
            this.stacktrace = stacktrace;
            return this;
        }

        /**
         * Page/Stage id where the end has happened.
         */
        public Builder pageId(String pageId) {
            this.pageId = pageId;
            return this;
        }

        public Error build() {
            if (StringUtil.isNullOrEmpty(errorCode)) {
                throw new IllegalStateException("errorCode is required.");
            }

            if (StringUtil.isNullOrEmpty(errorType)) {
                throw new IllegalStateException("errorType is required.");
            }

            if (StringUtil.isNullOrEmpty(stacktrace)) {
                throw new IllegalStateException("stacktrace is required.");
            }

            return new Error(errorCode, errorType, stacktrace, pageId);
        }
    }

}
