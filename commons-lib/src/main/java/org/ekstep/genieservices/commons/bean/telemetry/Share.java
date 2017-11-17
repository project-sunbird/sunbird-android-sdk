package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 15/11/17.
 */

public class Share extends TelemetryV3 {

    private static final String EID = "SHARE";


    private Share(String direction, String dataType, List<Map<String, Object>> contents) {
        super(EID);
        setEData(createEData(direction, dataType, contents));
    }

    private Map<String, Object> createEData(String direction, String dataType, List<Map<String, Object>> contents) {
        Map<String, Object> map = new HashMap<>();
        map.put("dir", direction);
        map.put("type", dataType);
        map.put("items", contents);
        return map;
    }

    public static class Builder {
        private String direction;
        private String dataType;
        private int count;
        private long size;
        private List<Map<String, Object>> contents = new ArrayList<>();

        public Share.Builder directionExport() {
            this.direction = "out";
            return this;
        }

        public Share.Builder directionImport() {
            this.direction = "in";
            return this;
        }

        public Share.Builder dataTypeFile() {
            this.dataType = "File";
            return this;
        }

        public Share.Builder dataTypeLink() {
            this.dataType = "Link";
            return this;
        }

        public String itemTypeContent() {
            return this.dataType = "CONTENT";
        }

        public String itemTypeExplodedContent() {
            return this.dataType = "EXPLODEDCONTENT";
        }

        public String itemTypeTelemetry() {
            return this.dataType = "TELEMETRY";
        }


        public String itemTypeProfile() {
            return this.dataType = "PROFILE";
        }

        /**
         * Adds Transferred Item details.
         */
        public Share.Builder addItem(String type, String origin, String identifier, Double pkgVersion, int transferCount, String size) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("origin", origin);
            itemMap.put("id", identifier);
            if ("Content".equalsIgnoreCase(type) || "EXPLODEDCONTENT".equals(type)) {
                itemMap.put("type", type);
                itemMap.put("ver", pkgVersion);
                //Add Params
                List<Map<String, Object>> paramsList = new ArrayList<>();
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("transfers", transferCount);
                paramsMap.put("size", size);
                paramsList.add(paramsMap);

                itemMap.put("params", paramsList);
            } else if ("Profile".equalsIgnoreCase(type)) {
                itemMap.put("type", "Profile");
            } else if ("Telemetry".equalsIgnoreCase(type)) {
                itemMap.put("type", "Telemetry");
            }



            //Add Origin
            Map<String, Object> originMap = new HashMap<>();
            originMap.put("id", origin);
            originMap.put("type", "Device");

            itemMap.put("origin", originMap);

            this.contents.add(itemMap);
            return this;
        }

        public Share build() {
            return new Share(direction, dataType, contents);
        }
    }
}
