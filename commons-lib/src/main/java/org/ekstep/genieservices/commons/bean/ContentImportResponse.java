package org.ekstep.genieservices.commons.bean;

/**
 * This class holds the identifier, status and filePath after importing the content.
 * <p>
 * Status -
 * -1 for unknown
 * 0 - not started
 * 1 - started download
 * 2- imported successfully
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
