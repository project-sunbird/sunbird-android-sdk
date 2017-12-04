package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * This class accepts,
 * * - contentImportMap
 */
public class ContentImportRequest {

    private Map<String, Object> contentImportMap;
    private String[] contentStatusArray;

    private ContentImportRequest(Map<String, Object> contentImportMap, String[] contentStatusArray) {
        this.contentImportMap = contentImportMap;
        this.contentStatusArray = contentStatusArray;
    }

    public Map<String, Object> getContentImportMap() {
        return contentImportMap;
    }

    public String[] getContentStatusArray() {
        return contentStatusArray;
    }

    public static class Builder {
        private Map<String, Object> contentImportMap;
        private String[] contentStatusArray;

        public Builder() {
            this.contentImportMap = new HashMap<>();
        }

        /**
         * Method to add ContentImport object
         */
        public Builder add(ContentImport contentImport) {
            if (contentImport == null) {
                throw new IllegalArgumentException("ContentImport cant be null");
            }

            if (StringUtil.isNullOrEmpty(contentImport.getContentId())) {
                throw new IllegalArgumentException("Identifier required.");
            }

            if (StringUtil.isNullOrEmpty(contentImport.getDestinationFolder())) {
                throw new IllegalArgumentException("To folder required.");
            }

            this.contentImportMap.put(contentImport.getContentId(), contentImport);
            return this;
        }

        public Builder allowUnlistedContent() {
            this.contentStatusArray = new String[]{"Live", "Unlisted"};
            return this;
        }

        public ContentImportRequest build() {
            if (this.contentImportMap.isEmpty()) {
                throw new IllegalStateException("Add atleast one content to import");
            }

            if (contentStatusArray == null || contentStatusArray.length == 0) {
                this.contentStatusArray = new String[]{"Live"};
            }

            return new ContentImportRequest(contentImportMap, contentStatusArray);
        }
    }
}
