package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.CorrelationData;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class holds the data about the Telemetry event
 */
public class Telemetry {

    private static final String TELEMETRY_VERSION = "3.0";

    /**
     * unique event ID.
     */
    private String eid;

    /**
     * epoch timestamp of event capture in epoch format (time in milli-seconds. For ex: 1442816723).
     */
    private long ets;

    /**
     * version of the event data structure, currently "3".
     */
    private String ver;

    /**
     * Who did the event
     * Actor of the event
     */
    private Actor actor;

    /**
     * Who did the event
     * Context in which the event has occured.
     */
    private Context context;

    /**
     * What is the target of the event
     * Object which is the subject of the event
     */
    private TelemetryObject object;

    private Map<String, Object> edata;

    private List<String> tags;

    public Telemetry(String eid) {
        this.eid = eid;
        this.ver = TELEMETRY_VERSION;
        this.ets = DateUtil.getEpochTime();
        this.actor = new Actor();
        this.context = new Context();
        this.edata = new HashMap<>();
    }

    public String getEid() {
        return this.eid;
    }

    public String getVer() {
        return this.ver;
    }

    public long getEts() {
        return ets;
    }

    public Map<String, Object> getEdata() {
        return edata;
    }

    public void setEData(Map<String, Object> edata) {
        this.edata = edata;
    }

    public void setCoRrelationdata(List<CorrelationData> correlationData) {
        this.context.setCdata(correlationData);
    }

    public void setObject(String id, String type, String ver) {
        this.object = new TelemetryObject(id, type, ver);
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Context getContext() {
        return context;
    }

    public TelemetryObject getObject() {
        return object;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
