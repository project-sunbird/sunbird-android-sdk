package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.ContentImportStatus;

/**
 * This class holds the identifier, status and filePath after importing the content.
 * <p>
 * Status - {@link ContentImportStatus}
 */
public class ContentImportResponse {

    private String identifier;
    private ContentImportStatus status;

    public ContentImportResponse(String identifier, ContentImportStatus status) {
        this.identifier = identifier;
        this.status = status;
    }

    public ContentImportStatus getStatus() {
        return status;
    }

    public String getIdentifier() {
        return identifier;
    }
}
