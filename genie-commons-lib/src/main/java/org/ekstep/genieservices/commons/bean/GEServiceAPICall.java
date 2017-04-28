package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.ITelemetry;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 27/4/17.
 */

public class GEServiceAPICall extends BaseTelemetry implements ITelemetry {
    private final String eid = "GE_SERVICE_API_CALL" ;
    private final Map<String,Object> edata;

    public static final String MODE_WIFI = "WIFI";
    public static final String MODE_MDATA = "MDATA";
    public static final String MODE_LOCAL = "LOCAL";
    public static final String MODE_NO_NETWORK = "NO_NETWORK";

    public GEServiceAPICall(String service, String method, boolean status, String error,
                            String message, List<String> errorMessages, Object result,
                            String gameID, String gameVersion) {
        edata=new HashMap<>();
        edata.put("eks",createEKS(service,method,status,error,message,errorMessages,result));

        String gdataId = (gameID == null || gameID.trim().isEmpty()) ? "genieservice.android" : gameID;
//        String gdataVersion = (gameVersion == null || gameVersion.trim().isEmpty())
//                ? BuildConfig.VERSION_NAME
//                : gameVersion;
        setGdata(new GameData(gdataId, gameVersion));
        setVer("2.0");
        setEts(DateUtil.getEpochTime());
    }

    public void setParams(Map params){
        HashMap<String, Object> eks = (HashMap<String, Object>) edata.get("eks");
        if(eks!=null){
            eks.put("request",params);
        }
    }

    protected HashMap<String, Object> createEKS(String service, String method, boolean status, String error, String message, List<String> errorMessages, Object result) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("service", service);
        eks.put("method", method);
        eks.put("status", status ? "Successful":"Failed");
        eks.put("error", error);
        eks.put("message", message);
        eks.put("errorMessages", errorMessages);
        eks.put("message", message);
        eks.put("result", result);
        return eks;
    }

    @Override
    public String getEID() {
        return eid;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public List<String> getErrors() {
        return null;
    }

    public void setMode(String mode) {
        HashMap<String, Object> eks = (HashMap<String, Object>) edata.get("eks");
        if (eks != null) {
            eks.put("mode", mode);
        }
    }

    public static class Builder {
        private String service;
        private String method;
        private GenieResponse response;
        private String gameID;
        private String gameVersion;
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

        public GEServiceAPICall build() {
            GEServiceAPICall event = new GEServiceAPICall(service, method, response.getStatus(), response.getError(),
                    response.getMessage(), response.getErrorMessages(), response.getResult(),
                    gameID, gameVersion);
            event.setParams(this.params);
            event.setMode(mode);

            return event;
        }

        public Builder gameID(String gameID) {
            this.gameID = gameID;
            return this;
        }

        public Builder gameVersion(String gameVersion) {
            this.gameVersion = gameVersion;
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
    }
}
