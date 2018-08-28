package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.ContentImportStatus;

/**
 * This class holds the identifier, status of content.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContentImportResponse that = (ContentImportResponse) o;
        return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

}
