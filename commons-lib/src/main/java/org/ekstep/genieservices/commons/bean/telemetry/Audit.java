package org.ekstep.genieservices.commons.bean.telemetry;

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
public class Audit extends Telemetry {

    private static final String EID = "AUDIT";

    private Audit(List<String> props, String currentState, String prevState, String actorType, String actorId) {
        super(EID);
        setEData(createEData(props, currentState, prevState));
        Actor actor = new Actor(actorType);
        actor.setId(actorId);
        setActor(actor);
    }

    private Map<String, Object> createEData(List<String> props, String currentState, String prevState) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("props", props);

        if (!StringUtil.isNullOrEmpty(currentState)) {
            eData.put("state", currentState);
        }

        if (!StringUtil.isNullOrEmpty(prevState)) {
            eData.put("prevstate", prevState);
        }

        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {

        private String env;
        private List<String> props;
        private String currentState;
        private String prevState;
        private String actorType;
        private String actorId;
        private String objId;
        private String objType;
        private String objVer;
        private Rollup rollup;

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

        public Builder updatedProperties(List<String> properties) {
            this.props = properties;
            return this;
        }

        public Builder currentState(String currentState) {
            this.currentState = currentState;
            return this;
        }

        public Builder previousState(String prevState) {
            this.prevState = prevState;
            return this;
        }

        public Builder actorType(String actorType) {
            this.actorType = actorType;
            return this;
        }

        public Builder actorId(String actorId) {
            this.actorId = actorId;
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

        public Audit build() {
            if (props == null) {
                props = new ArrayList<>();
            }

            Audit audit = new Audit(props, currentState, prevState, actorType, actorId != null ? actorId : "");
            audit.setEnvironment(env);
            audit.setObject(objId != null ? objId : "", objType != null ? objType : "", objVer != null ? objVer : "", rollup);
            return audit;
        }
    }
}
