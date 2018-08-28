package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class TelemetryExportResponse implements Serializable {

    private String exportedFilePath;

    public String getExportedFilePath() {
        return exportedFilePath;
    }

    public void setExportedFilePath(String exportedFilePath) {
        this.exportedFilePath = exportedFilePath;
    }
}
