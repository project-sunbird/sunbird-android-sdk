package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.CorrelationData;
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
 * @author swayangjit
 */
public class Impression extends Telemetry {

    private static final String EID = "IMPRESSION";

    private Impression(String type, String subtype, String pageId, String uri, List<Visit> visits) {
        super(EID);
        setEData(createEData(type, subtype, pageId, uri, visits));
    }

    private Map<String, Object> createEData(String type, String subtype, String pageId, String uri, List<Visit> visits) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", type);
        eData.put("pageid", pageId);

        if (!StringUtil.isNullOrEmpty(subtype)) {
            eData.put("subtype", subtype);
        }

        if (StringUtil.isNullOrEmpty(uri)) {
            uri = pageId;
        }
        eData.put("uri", uri);

        if (!CollectionUtil.isNullOrEmpty(visits)) {
            eData.put("visits", visits);
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
        private String pageId;
        private String uri;
        private List<CorrelationData> correlationData;
        private String objId;
        private String objType;
        private String objVer;
        private Rollup rollup;
        private List<Visit> visitList;

        /**
         * Impression type (list, detail, view, edit, workflow, search)
         */
        public Builder type(String type) {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalArgumentException("type shouldn't be null or empty.");
            }
            this.type = type;
            return this;
        }

        /**
         * Additional subtype. "Paginate", "Scroll"
         */
        public Builder subType(String subType) {
            this.subType = subType;
            return this;
        }

        /**
         * Unique page id
         */
        public Builder pageId(String pageId) {
            if (StringUtil.isNullOrEmpty(pageId)) {
                throw new IllegalArgumentException("pageId shouldn't be null or empty.");
            }
            this.pageId = pageId;
            return this;
        }

        /**
         * Relative URL of the content
         */
        public Builder uri(String uri) {
            this.uri = uri;
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

        /**
         * Object Visits description
         */
        public Builder addVisit(Visit visit) {
            if (visitList == null) {
                visitList = new ArrayList<>();
            }
            if (visit != null) {
                this.visitList.add(visit);
            }
            return this;
        }

        public Impression build() {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalStateException("type is required.");
            }

            if (StringUtil.isNullOrEmpty(pageId)) {
                throw new IllegalStateException("pageId is required.");
            }
            Impression event = new Impression(type, subType, pageId, uri, visitList);
            event.setObject(objId != null ? objId : "", objType != null ? objType : "", objVer != null ? objVer : "", rollup);
            event.setCoRrelationdata(correlationData);
            return event;
        }
    }

}
