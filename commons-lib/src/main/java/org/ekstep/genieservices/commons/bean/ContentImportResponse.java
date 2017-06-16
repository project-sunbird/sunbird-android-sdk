package org.ekstep.genieservices.commons.bean;

/**
 * Created by swayangjit on 26/5/17.
 */

public class ContentImportResponse {

    private String identifier;
    //-1 unknown, 0 - not started, 1 - started download, 2- imported successfully
    private int status;

    public ContentImportResponse(String identifier, int status) {
        this.identifier = identifier;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getIdentifier() {
        return identifier;
    }
}
