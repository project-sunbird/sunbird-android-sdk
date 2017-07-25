package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GETransfer extends Telemetry {

    private static final String EID = "GE_TRANSFER";

    private GETransfer(String direction, String dataType, int count, long size, List<Map<String, Object>> contents) {
        super(EID);
        setEks(createEKS(direction, dataType, count, size, contents));
    }

    private Map<String, Object> createEKS(String direction, String dataType, int count, long size, List<Map<String, Object>> contents) {
        Map<String, Object> map = new HashMap<>();
        map.put("direction", direction);
        map.put("datatype", dataType);
        map.put("count", count);
        map.put("size", size);
        map.put("contents", contents);
        return map;
    }

    public static class Builder {
        private String direction;
        private String dataType;
        private int count;
        private long size;
        private List<Map<String, Object>> contents = new ArrayList<>();

        public Builder directionExport() {
            this.direction = "EXPORT";
            return this;
        }

        public Builder directionImport() {
            this.direction = "IMPORT";
            return this;
        }

        public Builder dataTypeContent() {
            this.dataType = "CONTENT";
            return this;
        }

        public Builder dataTypeExplodedContent() {
            this.dataType = "EXPLODEDCONTENT";
            return this;
        }

        public Builder dataTypeTelemetry() {
            this.dataType = "TELEMETRY";
            return this;
        }


        public Builder dataTypeProfile() {
            this.dataType = "PROFILE";
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public Builder size(long size) {
            this.size = size;
            return this;
        }

        /**
         * Use this for dataType profile and telemetry.
         */
        public Builder addContent(String origin, String transferId, int transferCount) {
            Map<String, Object> contentMap = new HashMap<>();
            contentMap.put("origin", origin);
            contentMap.put("transferId", transferId);
            contentMap.put("transferCount", transferCount);

            this.contents.add(contentMap);
            return this;
        }

        /**
         * Use this for dataType content.
         */
        public Builder addContent(String origin, String identifier, Double pkgVersion, int transferCount) {
            Map<String, Object> contentMap = new HashMap<>();
            contentMap.put("origin", origin);
            contentMap.put("identifier", identifier);
            contentMap.put("pkgVersion", pkgVersion);
            contentMap.put("transferCount", transferCount);

            this.contents.add(contentMap);
            return this;
        }

        public GETransfer build() {
            return new GETransfer(direction, dataType, count, size, contents);
        }
    }

}
