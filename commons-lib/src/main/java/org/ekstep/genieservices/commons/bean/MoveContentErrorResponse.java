package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.MoveContentStatus;
import org.ekstep.genieservices.commons.bean.enums.ScanStorageStatus;

import java.io.Serializable;

/**
 * This class holds the identifier, status of content when duplicate content is found in destination.
 * <p>
 * Status - {@link MoveContentStatus}
 * shriharsh
 */

public class MoveContentErrorResponse implements Serializable {

    private String identifier;
    private MoveContentStatus status;

    public MoveContentErrorResponse(String identifier, MoveContentStatus status) {
        this.identifier = identifier;
        this.status = status;
    }

    public MoveContentStatus getStatus() {
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
        MoveContentErrorResponse that = (MoveContentErrorResponse) o;
        return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

}
