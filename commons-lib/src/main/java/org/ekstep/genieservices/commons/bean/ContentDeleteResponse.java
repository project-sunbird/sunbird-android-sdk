package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.ContentDeleteStatus;

/**
 * This class holds the identifier, status of content.
 * <p>
 * Status - {@link ContentDeleteStatus}
 * <p>
 * Created on 10/3/2017.
 *
 * @author anil
 */

public class ContentDeleteResponse {

    private String identifier;
    private ContentDeleteStatus status;

    public ContentDeleteResponse(String identifier, ContentDeleteStatus status) {
        this.identifier = identifier;
        this.status = status;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ContentDeleteStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContentDeleteResponse that = (ContentDeleteResponse) o;
        return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }
}
