package org.ekstep.genieservices.commons.bean;

/**
 * Created by swayangjit on 26/5/17.
 */

public class ContentImportResponse {

    private String identifier;
    //-1 unknown, 0 - not started, 1 - started download, 2- imported successfully
    private int status;
    private String filePath;

    public ContentImportResponse(String identifier, int status, String filePath) {
        this.identifier = identifier;
        this.status = status;
        this.filePath = filePath;
    }

    public int getStatus() {
        return status;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getFilePath() {
        return filePath;
    }
}
