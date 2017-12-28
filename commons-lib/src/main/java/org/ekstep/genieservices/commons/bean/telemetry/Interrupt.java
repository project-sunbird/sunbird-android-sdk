package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 24/11/17.
 *
 * @author swayangjit
 */
public class Interrupt extends Telemetry {

    private static final String EID = "INTERRUPT";

    private Interrupt(String type, String pageId) {
        super(EID);
        setEData(createEData(type, pageId));
    }

    private Map<String, Object> createEData(String type, String pageId) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", type);
        if (!StringUtil.isNullOrEmpty(pageId)) {
            eData.put("pageid", pageId);
        }
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {
        private String type;
        private String pageId;

        /**
         * Type of interrupt [m:background, m:resume]
         */
        public Builder type(String type) {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalArgumentException("type shouldn't be null or empty.");
            }
            this.type = type;
            return this;
        }

        /**
         * Page id where the interrupt has happened
         */
        public Builder pageId(String pageId) {
            this.pageId = pageId;
            return this;
        }

        public Interrupt build() {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalStateException("type is required.");
            }

            return new Interrupt(type, pageId);
        }
    }
}
