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
public class ExData extends Telemetry {

    private static final String EID = "EXDATA";

    private ExData(String type, String data) {
        super(EID);
        setEData(createEData(type, data));
    }

    private Map<String, Object> createEData(String type, String data) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", type);
        eData.put("data", data);
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {
        private String type;
        private String data;

        /**
         * Free flowing text. For ex: partnerdata, xapi etc
         */
        public Builder type(String type) {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalArgumentException("type should not be null or empty.");
            }

            this.type = type;
            return this;
        }

        /**
         * Serialized data (can be either encrypted/encoded/stringified)
         */
        public Builder data(String data) {
            if (StringUtil.isNullOrEmpty(data)) {
                throw new IllegalArgumentException("data should not be null or empty.");
            }

            this.data = data;
            return this;
        }

        public ExData build() {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalStateException("type is required.");
            }

            if (StringUtil.isNullOrEmpty(data)) {
                throw new IllegalStateException("data is required.");
            }

            return new ExData(type, data);
        }
    }
}
