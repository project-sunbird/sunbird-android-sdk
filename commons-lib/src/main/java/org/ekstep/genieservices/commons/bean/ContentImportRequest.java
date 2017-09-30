package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * This class accepts,
 *  * - contentImportMap
 */
public class ContentImportRequest {

    private Map<String, Object> contentImportMap;

    private ContentImportRequest(Map<String, Object> contentImportMap) {
        this.contentImportMap = contentImportMap;
    }

    public Map<String, Object> getContentImportMap() {
        return contentImportMap;
    }

    public static class Builder {
        private Map<String, Object> contentImportMap;

        public Builder() {
            this.contentImportMap = new HashMap<>();
        }

        /**
         * Method to add ContentImport object
         */
        public Builder add(ContentImport contentImport) {
            if (contentImport == null) {
                throw new IllegalStateException("ContentImport cant be null");
            }

            if (StringUtil.isNullOrEmpty(contentImport.getContentId())) {
                throw new IllegalStateException("Identifier required.");
            }

            if (StringUtil.isNullOrEmpty(contentImport.getDestinationFolder())) {
                throw new IllegalStateException("To folder required.");
            }

            this.contentImportMap.put(contentImport.getContentId(), contentImport);
            return this;
        }

        public ContentImportRequest build() {
            return new ContentImportRequest(this.contentImportMap);
        }
    }
}
