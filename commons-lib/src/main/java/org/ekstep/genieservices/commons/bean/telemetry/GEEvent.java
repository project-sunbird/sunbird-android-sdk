package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GameData;

import java.util.Map;

/**
 * Created by swayangjit on 7/5/17.
 */

public class GEEvent extends Telemetry {

    public GEEvent(GameData gameData, String eid, Map<String, Object> eks) {
        super(gameData, eid);
        setEks(eks);
    }


    public static class Builder {
        private GameData gameData;
        private String gameVersion;
        private String eid;
        private Map<String, Object> eks;

        public Builder(GameData gameData, String eid) {
            this.gameData = gameData;
            this.eid = eid;
        }

        public GEEvent.Builder eks(Map<String, Object> eks) {
            this.eks = eks;
            return this;
        }

        public GEEvent build() {
            return new GEEvent(gameData, eid, eks);
        }
    }
}
