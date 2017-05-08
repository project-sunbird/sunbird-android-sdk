package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.CommonConstants;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 27/4/17.
 */

public abstract class BaseTelemetry {
    private final Map<String, Object> edata = new HashMap<>();
    private String ts;
    private String ver;
    private String sid = "";
    private String uid = "";
    private String did;
    private long ets;
    private GameData gdata;
    private List<Map<String, Object>> tags = new ArrayList();
    private List<CoRelation> cdata;

    public BaseTelemetry(String gameId, String gameVersion) {
        setGdata(gameId, gameVersion);
        this.ver = CommonConstants.TELEMETRY_VERSION;
        this.ets = DateUtil.getEpochTime();
    }

    public void addCoRelation(List<CoRelation> coRelation) {
        this.cdata = new ArrayList<>();
        if (coRelation != null) {
            this.cdata.addAll(coRelation);
        }
    }

    public void addTag(Map<String, Object> map) {
        this.tags.add(map);
    }

    public void setEks(Map<String, Object> eks) {
        edata.put("eks", eks);
    }

    public void setEks(Object eks) {
        edata.put("eks", eks);
    }

    public Map<String, Object> getEData() {
        return this.edata;
    }

    public GameData getGdata() {
        return this.gdata;
    }

    public void setGdata(String gameID, String gameVersion) {
        String gdataId = isValidId(gameID) ? gameID : CommonConstants.GENIE_SERVICE_GID;
        this.gdata = new GameData(gdataId, gameVersion);
    }

    public String getTs() {
        return this.ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getVer() {
        return this.ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getSid() {
        return this.sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDid() {
        return this.did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public long getEts() {
        return this.ets;
    }

    public void setEts(long ets) {
        this.ets = ets;
    }

    protected boolean isValidId(String gameID) {
        return gameID != null && !gameID.trim().isEmpty();
    }

    protected abstract String getEID();

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
