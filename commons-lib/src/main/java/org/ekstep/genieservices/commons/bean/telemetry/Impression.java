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

    private Impression(String type, String subtype, String pageid, String uri, List<Visit> visits) {
        super(EID);
        setEData(createEData(type, subtype, pageid, uri, visits));
    }

    protected Map<String, Object> createEData(String type, String subtype, String pageid, String uri, List<Visit> visits) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", type);
        eData.put("subtype", !StringUtil.isNullOrEmpty(subtype) ? subtype : "");
        eData.put("pageid", !StringUtil.isNullOrEmpty(pageid) ? pageid : "");
        eData.put("uri", !StringUtil.isNullOrEmpty(uri) ? uri : "");
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
        private String pageid;
        private String uri;
        private List<CorrelationData> correlationData;
        private List<Visit> visitList;

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
         * Object Visits description
         */
        public Builder addVisits(Visit visit) {
            if (visitList == null) {
                visitList = new ArrayList<>();
            }
            if (visit != null) {
                this.visitList.add(visit);
            }
            return this;
        }


        public Impression build() {
            Impression event = new Impression(type, subType, pageid, uri, visitList);
            event.setCoRrelationdata(correlationData);
            return event;
        }
    }

}
