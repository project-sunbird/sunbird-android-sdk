package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.Map;

/**
 * Created on 7/5/17.
 *
 * @author swayangjit
 */
public class GEEvent extends Telemetry {

    private GEEvent(String eid, GameData gameData, ETags eTags, Map<String, Object> eks) {
        super(eid);
        setGdata(gameData);
        setEtags(eTags);
        setEks(eks);
    }

    public static class Builder {
        private String eid;
        private GameData gameData;
        private ETags eTags;
        private Map<String, Object> eks;

        public Builder(String eid) {
            this.eid = eid;
        }

        public Builder gameData(GameData gameData) {
            this.gameData = gameData;
            return this;
        }

        public Builder eTags(ETags eTags) {
            this.eTags = eTags;
            return this;
        }

        public Builder eks(Map<String, Object> eks) {
            this.eks = eks;
            return this;
        }

        public GEEvent build() {
            return new GEEvent(eid, gameData, eTags, eks);
        }
    }
}
