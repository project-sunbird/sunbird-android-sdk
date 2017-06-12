package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.CoRelation;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GEInteract extends Telemetry {

    private static final String EID = "GE_INTERACT";
    private String fileSize;

    public GEInteract(GameData gameData, String subType, String stageId, String type) {
        this(gameData, subType, stageId, type, null);
    }

    public GEInteract(GameData gameData, String subType, String stageId, String type, String fileSize) {
        super(gameData, EID);
        this.fileSize = fileSize;
        setEks(createEKS(subType, stageId, type));
    }

    public GEInteract(GameData gameData, String stageId, String type, String subType, String extType, List positionList, List<Map<String, Object>> valueList, String id, String tid, String uri) {
        super(gameData, EID);
        setEks(createEKS(stageId, type, subType, extType, positionList, valueList, id, tid, uri));
    }

    protected HashMap<String, Object> createEKS(String stageId, String type, String subType, String extType, List positionList, List<Map<String, Object>> valueList, String id, String tid, String uri) {
        HashMap<String, Object> eks = new HashMap<>();

        eks.put("extype", !StringUtil.isNullOrEmpty(extType) ? extType : "");
        eks.put("id", !StringUtil.isNullOrEmpty(id) ? id : "");
        eks.put("pos", !CollectionUtil.isNullOrEmpty(positionList) ? positionList : new ArrayList<>());
        eks.put("stageid", !StringUtil.isNullOrEmpty(stageId) ? stageId : "");
        eks.put("subtype", !StringUtil.isNullOrEmpty(subType) ? subType : "");
        eks.put("tid", !StringUtil.isNullOrEmpty(tid) ? tid : "");
        eks.put("type", type);
        eks.put("uri", !StringUtil.isNullOrEmpty(uri) ? uri : "");
        eks.put("values", !CollectionUtil.isNullOrEmpty(valueList) ? valueList : new ArrayList<>());

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
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {

        private GameData gameData;
        private String stageId = "";
        private String type = "";
        private String subType = "";
        private String exType = "";
        private String id = "";
        private List<Map<String, String>> pos = new ArrayList<>();
        private List<Map<String, Object>> values = new ArrayList<>();
        private String targetResourceId = "";
        private String uri = "";
        private List<CoRelation> coRelation;

        public Builder(GameData gameData) {
            this.gameData = gameData;
        }

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

        public Builder id(String id) {
            this.id = id;
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

        public Builder values(String key, String value) {
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put(key, value);
            this.values.add(valueMap);
            return this;
        }

        public Builder values(List<Map<String, Object>> mapList) {
            this.values.addAll(mapList);
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

        public Builder coRelation(List<CoRelation> coRelation) {
            this.coRelation = new ArrayList<>();
            this.coRelation.addAll(coRelation);
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
            GEInteract event = new GEInteract(gameData, stageId, type, subType, exType, pos, values, id, targetResourceId, uri);
            event.addCoRelation(coRelation);
            return event;
        }
    }


}
