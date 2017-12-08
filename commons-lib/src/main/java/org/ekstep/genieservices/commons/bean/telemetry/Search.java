package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swayangjit on 7/12/17.
 */

public class Search extends Telemetry {

    private static final String EID = "SEARCH";

    private Search(String type, String query, Map<String, Object> filters, Map<String, Object> sort, String correlationid, int size) {
        super(EID);
        setEData(createEData(type, query, filters, sort, correlationid, size));
    }

    protected Map<String, Object> createEData(String type, String query, Map<String, Object> filters, Map<String, Object> sort, String correlationid, int size) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", type);
        eData.put("query", query);
        if (filters != null && !filters.isEmpty()) {
            eData.put("filters", filters);
        }

        if (sort != null && !sort.isEmpty()) {
            eData.put("sort", sort);
        }

        eData.put("correlationid", !StringUtil.isNullOrEmpty(correlationid) ? correlationid : "");
        eData.put("size", size);
        eData.put("topn", new ArrayList<Map<String, Object>>() {
        });
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public static class Builder {
        private String type;
        private String query;
        private Map<String, Object> filters;
        private Map<String, Object> sort;
        private String correlationid;
        private int size;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /**
         * Search query string
         */
        public Builder query(String query) {
            if (query == null) {
                throw new IllegalArgumentException("query shouldn't be null");
            }
            this.query = query;
            return this;
        }

        /**
         * Additional filters
         */
        public Builder filters(Map<String, Object> filters) {
            this.filters = filters;
            return this;
        }

        /**
         * Additional sort parameters
         */
        public Builder sort(Map<String, Object> sort) {
            this.sort = sort;
            return this;
        }

        /**
         * Server generated correlation id
         */
        public Builder correlationId(String correlationid) {
            this.correlationid = correlationid;
            return this;
        }

        /**
         * Number of search results
         */
        public Builder size(int size) {
            this.size = size;
            return this;
        }


        public Search build() {
            return new Search(type, query, filters, sort, correlationid, size);
        }
    }

}
