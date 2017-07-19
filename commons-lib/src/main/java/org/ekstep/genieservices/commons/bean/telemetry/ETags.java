package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.List;

/**
 * Created on 7/13/2017.
 *
 * @author anil
 */
public class ETags {

    private List<String> app;
    private List<String> partner;
    private List<String> dims;

    public List<String> getApp() {
        return app;
    }

    public void setApp(List<String> app) {
        this.app = app;
    }

    public List<String> getPartner() {
        return partner;
    }

    public void setPartner(List<String> partner) {
        this.partner = partner;
    }

    public List<String> getDims() {
        return dims;
    }

    public void setDims(List<String> dims) {
        this.dims = dims;
    }
}
