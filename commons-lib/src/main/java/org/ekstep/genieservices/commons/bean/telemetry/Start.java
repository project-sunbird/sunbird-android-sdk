package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swayangjit on 15/11/17.
 */

public class Start extends Telemetry {

    private static final String EID = "START";

    private Start(String type, DeviceSpecification dSpec, String loc, String mode, long duration, String pageid) {
        super(EID);
        setEData(createEData(type, dSpec, loc, mode, duration, pageid));
    }

    protected Map<String, Object> createEData(String type, DeviceSpecification dSpec, String loc, String mode, long duration, String pageid) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", !StringUtil.isNullOrEmpty(type) ? type : "");
        eData.put("dspec", dSpec != null ? dSpec : new DeviceSpecification());
        eData.put("loc", !StringUtil.isNullOrEmpty(loc) ? loc : "");
        eData.put("mode", !StringUtil.isNullOrEmpty(mode) ? mode : "");
        if (duration > 0) {
            eData.put("duration", duration);
        }
        eData.put("pageid", !StringUtil.isNullOrEmpty(pageid) ? pageid : "");
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {

        private String type;
        private DeviceSpecification dspec;
        private String loc;
        private String mode;
        private long duration;
        private String pageid;

        /**
         * Type of event generator
         */
        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /**
         * Device specification of device .
         */
        public Builder deviceSpec(DeviceSpecification dspec) {
            this.dspec = dspec;
            return this;
        }

        /**
         * Location of the device.
         */
        public Builder loc(String loc) {
            this.loc = loc;
            return this;
        }

        /**
         * Mode of start. For "player" it would be "play/edit/preview". For Workflow it would be Review/Flag/Publish. For editor it could be "content", "textbook", "generic", "lessonplan".
         */
        public Builder mode(String mode) {
            this.mode = mode;
            return this;
        }

        /**
         * Time taken to initialize/start.
         */
        public Builder duration(long duration) {
            this.duration = duration;
            return this;
        }

        /**
         * Page/Stage id where the start has happened.
         */
        public Builder pageid(String pageid) {
            this.pageid = pageid;
            return this;
        }

        public Start build() {
            Start event = new Start(type, dspec, loc, mode, duration, pageid);
            return event;
        }
    }
}
