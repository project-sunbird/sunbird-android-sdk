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

    private Audit(List<String> props, String currentState, String prevState, String actorType) {
        super(EID);
        setEData(createEData(props, currentState, prevState));
        setActor(new Actor(actorType));
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

        public Audit build() {
            if (props == null) {
                props = new ArrayList<>();
            }

            Audit audit = new Audit(props, currentState, prevState, actorType);
            audit.setEnvironment(env);

            return audit;
        }
    }
}
