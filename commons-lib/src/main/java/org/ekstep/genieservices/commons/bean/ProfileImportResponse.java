package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class ProfileImportResponse implements Serializable {

    private int imported;
    private int failed;

    public int getImported() {
        return imported;
    }

    public void setImported(int imported) {
        this.imported = imported;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }
}
