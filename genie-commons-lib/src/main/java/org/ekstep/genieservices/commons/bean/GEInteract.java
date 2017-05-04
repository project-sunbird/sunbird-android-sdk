package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.ITelemetry;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GEInteract extends BaseTelemetry implements ITelemetry {

    private final String eid = "GE_INTERACT";
    private String fileSize;

    public GEInteract(String gameID, String gameVersion, String subType, String stageId, String type) {
        this(gameID, gameVersion, subType, stageId, type, null);
    }

    public GEInteract(String gameID, String gameVersion, String subType, String stageId, String type, String fileSize) {
        super(gameID, gameVersion);
        this.fileSize = fileSize;
        setEks(createEKS(subType, stageId, type));
    }

    public GEInteract(String gameID, String gameVersion, String stageId, String type, String subType,String extType,List positionList, List<Map> valueList,String id,String tid,String uri) {
        super(gameID, gameVersion);
    }

    protected HashMap<String, Object> createEKS(String stageId, String type, String subType,String extType,List positionList, List<Map> valueList,String id,String tid,String uri) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("extype", "");
        eks.put("id", "");
        eks.put("pos", new ArrayList<>());
        eks.put("stageid", stageId);
        eks.put("subtype", subType);
        eks.put("tid", "");
        eks.put("type", type);
        eks.put("uri", "");
        eks.put("values", createValues());

        return eks;
    }

    protected HashMap<String, Object> createEKS(String subType, String stageId, String type) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("extype", "");
        eks.put("id", "");
        eks.put("pos", new ArrayList<>());
        eks.put("stageid", stageId);
        eks.put("subtype", subType);
        eks.put("tid", "");
        eks.put("type", type);
        eks.put("uri", "");
        eks.put("values", createValues());

        return eks;
    }

    private List<Map> createValues() {
        List<Map> values = new ArrayList<>();
        if (!StringUtil.isNullOrEmpty(fileSize)) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("SizeOfDataInKB", fileSize);
            values.add(objectMap);
        }

        return values;
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


    public static class Builder {

        private String stageId = "";
        private String type = "";
        private String subType = "";
        private String exType = "";
        private String id = "";
        private List<Map<String, String>> pos = new ArrayList<>();
        private List<Map<String, Object>> values = new ArrayList<>();
        private String targetResourceId = "";
        private String uri = "";

        public Builder stageId(String stageId) {
            this.stageId = stageId;
            return this;
        }

        public Builder interActionType(InteractionType type) {
            this.type = type.getValue();
            return this;
        }

        public Builder subType(String subType) {
            this.subType = subType;
            return this;
        }

        public Builder exType(String subType) {
            this.subType = subType;
            return this;
        }

        public Builder resourceId(String resourceId) {
            this.id = resourceId;
            return this;
        }

        public Builder values(String key, Object value) {
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put(key, value);
            this.values.add(valueMap);
            return this;
        }

        public Builder values(Map<String, Object> map) {
            this.values.add(map);
            return this;
        }

        public Builder targetResourceId(String tid) {
            this.targetResourceId = tid;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder positions(String x, String y, String z) {
            Map<String, String> posMap = new HashMap<>();
            posMap.put("x", x);
            posMap.put("y", y);
            posMap.put("z", z);
            this.pos.add(posMap);
            return this;
        }


        public GEInteract build() {
//            GEInteract event = new GEInteract();
            return null;
        }
    }


}
