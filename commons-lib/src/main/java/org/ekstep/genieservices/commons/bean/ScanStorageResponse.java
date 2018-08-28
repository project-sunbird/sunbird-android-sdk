package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.ScanStorageStatus;

import java.io.Serializable;

/**
 * This class holds the identifier, status of content after scanning.
 * <p>
 * Status - {@link ScanStorageStatus}
 * shriharsh
 */

public class ScanStorageResponse implements Serializable {

    private String identifier;
    private ScanStorageStatus status;

    public ScanStorageResponse(String identifier, ScanStorageStatus status) {
        this.identifier = identifier;
        this.status = status;
    }

    public ScanStorageStatus getStatus() {
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
        ScanStorageResponse that = (ScanStorageResponse) o;
        return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

}
