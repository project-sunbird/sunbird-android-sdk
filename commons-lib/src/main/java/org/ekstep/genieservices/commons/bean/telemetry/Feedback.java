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
public class Feedback extends Telemetry {

    private static final String EID = "FEEDBACK";

    private Feedback(float rating, String comments, String id, String type, String version) {
        super(EID);
        setEData(createData(rating, comments));
        setObject(id, type, version, null);
    }

    private Map<String, Object> createData(float rating, String comments) {
        HashMap<String, Object> eData = new HashMap<>();
        eData.put("rating", rating);
        eData.put("comments", comments);
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {

        private String env;
        private float rating;
        private String comments;
        private String id;
        private String version;
        private String type;


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
         * Numeric score (+1 for like, -1 for dislike, or 4.5 stars given in a rating)
         */
        public Builder rating(float rating) {
            this.rating = rating;
            return this;
        }

        /**
         * Text feedback (if any)
         */
        public Builder comments(String comments) {
            this.comments = comments;
            return this;
        }

        /**
         * Id of the object. For ex: content id incase of content
         */
        public Builder objectId(String id) {
            if (StringUtil.isNullOrEmpty(id)) {
                throw new IllegalArgumentException("objectId should not be null or empty.");
            }

            this.id = id;
            return this;
        }

        /**
         * Type of the object. For ex: "Content", "Community", "User" etc.
         */
        public Builder objectType(String type) {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalArgumentException("objectType should not be null or empty.");
            }

            this.type = type;
            return this;
        }

        /**
         * Version of the object
         */
        public Builder objectVersion(String version) {
            this.version = version;
            return this;
        }

        public Feedback build() {

            if (StringUtil.isNullOrEmpty(env)) {
                throw new IllegalStateException("env is required.");
            }

            if (StringUtil.isNullOrEmpty(id)) {
                throw new IllegalStateException("objectId is required.");
            }

            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalStateException("objectType is required.");
            }

            Feedback event = new Feedback(rating, comments, id, type, version);
            event.setEnvironment(env);

            return event;
        }
    }
}
