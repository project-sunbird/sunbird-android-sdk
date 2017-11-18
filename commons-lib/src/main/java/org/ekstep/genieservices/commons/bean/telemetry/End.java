package org.ekstep.genieservices.commons.bean.telemetry;

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

public class End extends Telemetry {

    private static final String EID = "END";

    private End(String type, String mode, int duration, String pageid, List<Map<String, Object>> summaryList) {
        super(EID);
        createEData(type, mode, duration, pageid, summaryList);
    }

    protected Map<String, Object> createEData(String type, String mode, int duration, String pageid, List<Map<String, Object>> summaryList) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", !StringUtil.isNullOrEmpty(type) ? type : "");
        eData.put("mode", !StringUtil.isNullOrEmpty(mode) ? mode : "");
        eData.put("duration", duration);
        eData.put("pageid", !StringUtil.isNullOrEmpty(pageid) ? pageid : "");
        eData.put("summary", !CollectionUtil.isNullOrEmpty(summaryList) ? summaryList : new ArrayList<>());
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {

        private String type;
        private String mode;
        private int duration;
        private String pageid;
        private List<Map<String, Object>> summaryList = null;

        /**
         * Type of event generator
         */
        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /**
         * Mode of end. For "player" it would be "play/edit/preview". For Workflow it would be Review/Flag/Publish. For editor it could be "content", "textbook", "generic", "lessonplan".
         */
        public Builder mode(String mode) {
            this.mode = mode;
            return this;
        }

        /**
         * Total duration from start to end in seconds
         */
        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        /**
         * Page/Stage id where the end has happened.
         */
        public Builder pageid(String pageid) {
            this.pageid = pageid;
            return this;
        }

        /**
         * Summary of the actions done between start and end. For ex: "progress" for player session, "nodesModified" for collection editor.
         */
        public Builder addSummary(Map<String, Object> summary) {
            if (summaryList == null) {
                summaryList = new ArrayList<>();
            }
            if (summary != null) {
                this.summaryList.add(summary);
            }
            return this;
        }

        public End build() {
            End event = new End(type, mode, duration, pageid, summaryList);
            return event;
        }
    }
}
