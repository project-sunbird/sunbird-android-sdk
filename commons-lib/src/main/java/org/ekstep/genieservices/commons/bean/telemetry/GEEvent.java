package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.Map;

/**
 * Created on 7/5/17.
 *
 * @author swayangjit
 */
public class GEEvent extends Telemetry {

    private GEEvent(GameData gameData, String eid, Map<String, Object> eks) {
        super(eid);
        setGdata(gameData);
        setEks(eks);
    }

    public static class Builder {
        private GameData gameData;
        private String eid;
        private Map<String, Object> eks;

        public Builder(String eid) {
            this.eid = eid;
        }

        public Builder gameData(GameData gameData) {
            this.gameData = gameData;
            return this;
        }

        public Builder eks(Map<String, Object> eks) {
            this.eks = eks;
            return this;
        }

        public GEEvent build() {
            return new GEEvent(gameData, eid, eks);
        }
    }
}
