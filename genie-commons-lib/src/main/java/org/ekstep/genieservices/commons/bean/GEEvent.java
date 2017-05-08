package org.ekstep.genieservices.commons.bean;

import java.util.Map;

/**
 * Created by swayangjit on 7/5/17.
 */

public class GEEvent extends BaseTelemetry{
    private String eid;

    public GEEvent(String gameId,String gameVersion,String eid,Map<String,Object> eks){
        super(gameId,gameVersion);
        this.eid=eid;
        setEks(eks);
    }

    @Override
    protected String getEID() {
        return eid;
    }

    public static class Builder {
        private String gameId;
        private String gameVersion;
        private String eid;
        private Map<String,Object> eks;

        public Builder(String id, String version,String eid){
            this.gameId=id;
            this.gameVersion=version;
            this.eid=eid;
        }
        public GEEvent.Builder eks(Map<String,Object> eks) {
            this.eks = eks;
            return this;
        }

        public GEEvent build() {
            return new GEEvent(gameId,gameVersion,eid,eks);
        }
    }
}
