package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;


/**
 * This class holds the exportedFilePath data which is where the exported content is stored.
 *
 */
public class ContentExportResponse implements Serializable {

    private String exportedFilePath;

    public String getExportedFilePath() {
        return exportedFilePath;
    }

    public void setExportedFilePath(String exportedFilePath) {
        this.exportedFilePath = exportedFilePath;
    }
}
