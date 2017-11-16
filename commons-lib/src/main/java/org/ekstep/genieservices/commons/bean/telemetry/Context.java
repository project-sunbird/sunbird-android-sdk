package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.CorrelationData;

import java.util.List;

/**
 * Created by swayangjit on 16/11/17.
 */

public class Context {

    private String channel;
    private ProducerData pdata;
    private String env;
    private String sid;
    private String did;
    private List<CorrelationData> cdata;

    public Context() {
        this.channel = "";
        this.env = "";
        this.env = "";
        this.did = "";
        this.sid = "";
    }

    public void setCdata(List<CorrelationData> cdata) {
        this.cdata = cdata;
    }
}
