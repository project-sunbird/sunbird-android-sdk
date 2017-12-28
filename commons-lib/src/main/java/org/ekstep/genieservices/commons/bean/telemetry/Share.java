package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 15/11/17.
 *
 * @author swayangjit
 */
public class Share extends Telemetry {

    private static final String EID = "SHARE";

    private Share(String direction, String dataType, List<Map<String, Object>> contents) {
        super(EID);
        setEData(createEData(direction, dataType, contents));
    }

    private Map<String, Object> createEData(String direction, String dataType, List<Map<String, Object>> items) {
        Map<String, Object> map = new HashMap<>();
        map.put("dir", direction);
        map.put("type", dataType);
        map.put("items", items);
        return map;
    }

    public static class Builder {
        private String direction;
        private String dataType;
        private List<Map<String, Object>> items = new ArrayList<>();

        public Builder directionExport() {
            this.direction = "Out";
            return this;
        }

        public Builder directionImport() {
            this.direction = "In";
            return this;
        }

        public Builder dataTypeFile() {
            this.dataType = "File";
            return this;
        }

        public Builder dataTypeLink() {
            this.dataType = "Link";
            return this;
        }

        public Builder dataTypeMessage() {
            this.dataType = "Message";
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
        public Builder addItem(String type, String origin, String identifier, Double pkgVersion, int transferCount, String size) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("origin", origin);
            itemMap.put("id", identifier);
            if ("Content".equalsIgnoreCase(type) || "EXPLODEDCONTENT".equals(type)) {
                itemMap.put("type", type);
                itemMap.put("ver", String.valueOf(pkgVersion));
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

            this.items.add(itemMap);
            return this;
        }

        public Share build() {
            return new Share(direction, dataType, items);
        }
    }
}
