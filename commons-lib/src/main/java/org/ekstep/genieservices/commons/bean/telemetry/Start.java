package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.CorrelationData;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 15/11/17.
 *
 * @author swayangjit
 */
public class Start extends Telemetry {

    private static final String EID = "START";

    private Start(String type, DeviceSpecification dSpec, String loc, String mode, long duration, String pageId) {
        super(EID);
        setEData(createEData(type, dSpec, loc, mode, duration, pageId));
    }

    private Map<String, Object> createEData(String type, DeviceSpecification deviceSpecification, String loc, String mode, long duration, String pageId) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", !StringUtil.isNullOrEmpty(type) ? type : "");

        if (deviceSpecification != null) {
            eData.put("dspec", deviceSpecification);
        }
        if (!StringUtil.isNullOrEmpty(loc)) {
            eData.put("loc", loc);
        }
        if (!StringUtil.isNullOrEmpty(mode)) {
            eData.put("mode", mode);
        }
        if (duration > 0) {
            eData.put("duration", duration);
        }
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

        private String env;
        private String type;
        private DeviceSpecification deviceSpecification;
        private String loc;
        private String mode;
        private long duration;
        private String pageId;
        private String objId;
        private String objType;
        private String objVer;
        private Rollup rollup;
        private List<CorrelationData> correlationData;

        /**
         * Unique environment where the event has occured.
         */
        public Builder environment(String env) {
            if (StringUtil.isNullOrEmpty(env)) {
                throw new IllegalArgumentException("environment shouldn't be null or empty.");
            }
            this.env = env;
            return this;
        }

        /**
         * Type of event generator
         */
        public Builder type(String type) {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalArgumentException("type shouldn't be null or empty.");
            }

            this.type = type;
            return this;
        }

        /**
         * Device specification of device .
         */
        public Builder deviceSpecification(DeviceSpecification deviceSpecification) {
            this.deviceSpecification = deviceSpecification;
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
        public Builder pageId(String pageId) {
            this.pageId = pageId;
            return this;
        }

        /**
         * Id of the object. For ex: content id incase of content
         */
        public Builder objectId(String objId) {
            this.objId = objId;
            return this;
        }

        /**
         * Type of the object. For ex: "Content", "Community", "User" etc.
         */
        public Builder objectType(String objType) {
            this.objType = objType;
            return this;
        }

        /**
         * version of the object
         */
        public Builder objectVersion(String objVer) {
            this.objVer = objVer;
            return this;
        }

        /**
         * hierarchyLevel to be computed of the object. Only 4 levels are allowed.
         */
        public Builder hierarchyLevel(Rollup rollup) {
            this.rollup = rollup;
            return this;
        }

        /**
         * List of {@link CorrelationData}
         */
        public Builder correlationData(List<CorrelationData> correlationData) {
            this.correlationData = new ArrayList<>();
            if (!CollectionUtil.isNullOrEmpty(correlationData)) {
                this.correlationData.addAll(correlationData);
            }

            return this;
        }

        public Start build() {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalStateException("type is required");
            }

            if (StringUtil.isNullOrEmpty(env)) {
                throw new IllegalStateException("env is required.");
            }

            Start event = new Start(type, deviceSpecification, loc, mode, duration, pageId);
            event.setObject(objId != null ? objId : "", objType != null ? objType : "", objVer != null ? objVer : "", rollup);
            event.setEnvironment(env);
            event.setCoRrelationdata(correlationData);
            return event;
        }
    }
}
