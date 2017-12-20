package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.CorrelationData;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 15/11/17.
 *
 * @author anil
 */
public class Interact extends Telemetry {

    private static final String EID = "INTERACT";

    private Interact(String type, String subtype, String id, String pageId, List<Map<String, String>> positionList, List<Map<String, Object>> valueList) {
        super(EID);
        setEData(createEData(type, subtype, id, pageId, positionList, valueList));
    }

    protected Map<String, Object> createEData(String type, String subType, String id, String pageId, List<Map<String, String>> positionList, List<Map<String, Object>> valueList) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", type);
        if (!StringUtil.isNullOrEmpty(subType)) {
            eData.put("subtype", subType);
        }
        if (!StringUtil.isNullOrEmpty(id)) {
            eData.put("id", id);
        }
        if (!StringUtil.isNullOrEmpty(pageId)) {
            eData.put("pageid", pageId);
        }

        if (!CollectionUtil.isNullOrEmpty(positionList) || !CollectionUtil.isNullOrEmpty(valueList)) {
            Map<String, Object> extra = new HashMap<>();
            if (!CollectionUtil.isNullOrEmpty(positionList)) {
                extra.put("pos", positionList);
            }
            if (!CollectionUtil.isNullOrEmpty(valueList)) {
                extra.put("values", valueList);
            }
            eData.put("extra", extra);
        }

        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {

        private String type;
        private String subType;
        private String id;
        private String pageId;
        private List<Map<String, String>> pos = new ArrayList<>();
        private List<Map<String, Object>> values = new ArrayList<>();
        private List<CorrelationData> correlationData;
        private String objId;
        private String objType;
        private String objVer;
        private Rollup rollup;

        /**
         * Type of interaction TOUCH,DRAG,DROP,PINCH,ZOOM,SHAKE,ROTATE,SPEAK,LISTEN,WRITE,DRAW,START,ENDCHOOSE,ACTIVATE,SHOW,HIDE,SCROLL,HEARTBEAT,OTHER
         */
        public Builder interactionType(InteractionType type) {
            if (type == null) {
                throw new IllegalArgumentException("interactionType should not be null.");
            }
            this.type = type.getValue();
            return this;
        }

        /**
         * Additional types for a global type. For ex: for an audio the type is LISTEN and thesubtype can be one of PLAY,PAUSE,STOP,RESUME,END
         */
        public Builder subType(String subType) {
            this.subType = subType;
            return this;
        }

        /**
         * Resource (button, screen, page, etc) id on which the interaction happened - use systemidentifiers when reporting device events
         */
        public Builder resourceId(String id) {
            this.id = id;
            return this;
        }

        /**
         * Stage or page id on which the event happened
         */
        public Builder pageId(String pageId) {
            if (StringUtil.isNullOrEmpty(pageId)) {
                throw new IllegalArgumentException("pageId shouldn't be null or empty.");
            }
            this.pageId = pageId;
            return this;
        }


        /**
         * List of {@link CorrelationData}
         */
        public Builder correlationData(List<CorrelationData> correlationData) {
            this.correlationData = new ArrayList<>();
            if (!CollectionUtil.isNullOrEmpty(correlationData)) {
                this.correlationData.addAll(correlationData);
            }

            return this;
        }

        /**
         * Extra attributes for an interaction
         */
        public Builder values(List<Map<String, Object>> values) {
            this.values = values;
            return this;
        }

        /**
         * Positional attributes. For ex: Drag and Drop has two positional attributes. One where the drag has started and the drop point
         */
        public Builder positions(String x, String y, String z) {
            Map<String, String> posMap = new HashMap<>();
            posMap.put("x", x);
            posMap.put("y", y);
            posMap.put("z", z);
            this.pos.add(posMap);
            return this;
        }

        /**
         * Id of the object. For ex: content id incase of content
         */
        public Builder objectId(String objId) {
            this.objId = objId;
            return this;
        }

        /**
         * Type of the object. For ex: "Content", "Community", "User" etc.
         */
        public Builder objectType(String objType) {
            this.objType = objType;
            return this;
        }

        /**
         * version of the object
         */
        public Builder objectVersion(String objVer) {
            this.objVer = objVer;
            return this;
        }

        /**
         * hierarchyLevel to be computed of the object. Only 4 levels are allowed.
         */
        public Builder hierarchyLevel(Rollup rollup) {
            this.rollup = rollup;
            return this;
        }


        public Interact build() {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalStateException("interactionType is required.");
            }

            if (StringUtil.isNullOrEmpty(pageId)) {
                throw new IllegalStateException("pageId is required.");
            }

            Interact event = new Interact(type, subType, id, pageId, pos, values);
            event.setCoRrelationdata(correlationData);
            event.setObject(objId != null ? objId : "", objType != null ? objType : "", objVer != null ? objVer : "", rollup);
            return event;
        }
    }
}
