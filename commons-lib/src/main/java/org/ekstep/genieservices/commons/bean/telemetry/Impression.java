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
 * Created by swayangjit on 15/11/17.
 */

public class Impression extends Telemetry {

    private static final String EID = "IMPRESSION";

    private Impression(String type, String subtype, String pageid, String uri, Visit visit) {
        super(EID);
        setEData(createEData(type, subtype, pageid, uri, visit));
    }

    protected Map<String, Object> createEData(String type, String subtype, String pageid, String uri, Visit visit) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", type);
        eData.put("subtype", !StringUtil.isNullOrEmpty(subtype) ? subtype : "");
        eData.put("pageid", !StringUtil.isNullOrEmpty(pageid) ? pageid : "");
        eData.put("uri", !StringUtil.isNullOrEmpty(uri) ? uri : "");
        eData.put("visits", visit);
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {

        private String type;
        private String subType;
        private String pageid;
        private String uri;
        private String objid;
        private String objtype;
        private String objVer;
        private String section;
        private int index;
        private List<CorrelationData> correlationData;

        /**
         * Impression type (list, detail, view, edit, workflow, search)
         */
        public Builder type(String type) {
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
        public Builder pageId(String pageid) {
            this.pageid = pageid;
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
         * Unique id for the object visited
         */
        public Builder visitedTo(String objid) {
            this.objid = objid;
            return this;
        }

        /**
         * Type of the object visited
         */
        public Builder visitedObjectType(String objtype) {
            this.objtype = objtype;
            return this;
        }

        /**
         * Version of the object visited
         */
        public Builder visitedObjectVersion(String objVer) {
            this.objVer = objVer;
            return this;
        }

        /**
         * Free flowing text
         */
        public Builder visitedSection(String section) {
            this.section = section;
            return this;
        }

        /**
         * Index of the object within a given list
         */
        public Builder visitedObjectIndex(int index) {
            this.index = index;
            return this;
        }


        public Impression build() {
            Impression event = new Impression(type, subType, pageid, uri, new Visit(objid, objtype, objVer, section, index));
            event.setCoRrelationdata(correlationData);
            return event;
        }
    }

}
