package org.ekstep.genieservices.commons.bean.telemetry;

/**
 * Created on 7/12/2017.
 * <p>
 * Producer information. Generally the App which is creating the event.
 *
 * @author anil
 */
public class ProducerData {

    /**
     * Producer ID. For ex: For EkStep it would be "portal" or "genie".
     */
    private String id;

    /**
     * version of the App
     */
    private String ver;

    public ProducerData(String id, String ver) {
        this.id = id;
        this.ver = ver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}
