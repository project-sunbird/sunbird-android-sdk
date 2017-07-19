package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 27/4/17.
 *
 * @author swayangjit
 */
public class GEServiceAPICall extends Telemetry {

    private static final String EID = "GE_SERVICE_API_CALL";

    private GEServiceAPICall(String service, String method, boolean status, String error, String message, List<String> errorMessages, Object result) {
        super(EID);
        setEks(createEKS(service, method, status ? "Successful" : "Failed", error, message, errorMessages, result));
    }

    public void setParams(Map params) {
        HashMap<String, Object> eks = (HashMap<String, Object>) getEData().get("eks");
        if (eks != null) {
            eks.put("request", params);
        }
    }

    protected HashMap<String, Object> createEKS(String service, String method, String status, String error, String message, List<String> errorMessages, Object result) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("service", service);
        eks.put("method", method);
        eks.put("status", status);
        eks.put("error", error);
        eks.put("message", message);
        eks.put("errorMessages", errorMessages);
        eks.put("message", message);
        eks.put("result", result);
        return eks;
    }

    public void setMode(String mode) {
        HashMap<String, Object> eks = (HashMap<String, Object>) getEData().get("eks");
        if (eks != null) {
            eks.put("mode", mode);
        }
    }

    public static class Builder {
        private String service;
        private String method;
        private GenieResponse response;
        private HashMap result;
        private Map params;
        private String mode;

        public Builder service(String service) {
            this.service = service;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder response(GenieResponse response) {
            this.response = response;
            return this;
        }

        public Builder result(HashMap result) {
            this.result = result;
            return this;
        }

        public Builder request(Map params) {
            this.params = params;
            return this;
        }

        public Builder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public GEServiceAPICall build() {
            GEServiceAPICall event = new GEServiceAPICall(service, method, response.getStatus(), response.getError(),
                    response.getMessage(), response.getErrorMessages(), result);
            event.setParams(this.params);
            event.setMode(mode);

            return event;
        }
    }
}
