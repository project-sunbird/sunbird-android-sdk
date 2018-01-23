package org.ekstep.genieservices.commons.bean.telemetry;

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
public class Log extends Telemetry {

    private static final String EID = "LOG";

    private Log(String type, String level, String message, String pageId, List<Map<String, Object>> params) {
        super(EID);
        setEData(createEData(type, level, message, pageId, params));
    }

    private Map<String, Object> createEData(String type, String level, String message, String pageId, List<Map<String, Object>> params) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", type);
        eData.put("level", level);
        eData.put("message", message);
        if (!StringUtil.isNullOrEmpty(pageId)) {
            eData.put("pageid", pageId);
        }
        if (!CollectionUtil.isNullOrEmpty(params)) {
            eData.put("params", params);
        }
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public interface Level {
        String TRACE = "TRACE";
        String DEBUG = "DEBUG";
        String INFO = "INFO";
        String WARN = "WARN";
        String ERROR = "ERROR";
        String FATAL = "FATAL";
    }

    public static class Builder {

        private String env;
        private String type;
        private String level;
        private String message;
        private String pageId;
        private List<Map<String, Object>> params;
        private String actorType;


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
         * Type of log (system, process, api_access, api_call, job, app_update etc)
         */
        public Builder type(String type) {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalArgumentException("type should not be null or empty.");
            }

            this.type = type;
            return this;
        }

        /**
         * Level of the log. TRACE, DEBUG, INFO, WARN, ERROR, FATAL
         */
        public Builder level(String level) {
            if (StringUtil.isNullOrEmpty(level)) {
                throw new IllegalArgumentException("level should not be null or empty.");
            }

            this.level = level;
            return this;
        }

        /**
         * Log message
         */
        public Builder message(String message) {
            if (StringUtil.isNullOrEmpty(message)) {
                throw new IllegalArgumentException("message should not be null or empty.");
            }

            this.message = message;
            return this;
        }

        /**
         * Page where the log event has happened
         */
        public Builder pageId(String pageId) {
            this.pageId = pageId;
            return this;
        }

        /**
         * Additional params in the log message
         */
        public Builder addParam(String key, Object value) {
            if (params == null) {
                params = new ArrayList<>();
            }
            if (key != null && value != null) {
                Map<String, Object> map = new HashMap<>();
                map.put(key, value);
                this.params.add(map);
            }

            return this;
        }

        /**
         * Type of actor who created the event
         */
        public Builder actorType(String actorType) {
            this.actorType = actorType;
            return this;
        }

        public Log build() {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalStateException("type is required.");
            }

            if (StringUtil.isNullOrEmpty(level)) {
                throw new IllegalStateException("level is required.");
            }

            if (StringUtil.isNullOrEmpty(message)) {
                throw new IllegalStateException("message is required.");
            }

            if (StringUtil.isNullOrEmpty(env)) {
                throw new IllegalStateException("env is required.");
            }

            Log event = new Log(type, level, message, pageId, params);
            event.setActor(new Actor(actorType));
            event.setEnvironment(env);
            return event;
        }
    }
}
