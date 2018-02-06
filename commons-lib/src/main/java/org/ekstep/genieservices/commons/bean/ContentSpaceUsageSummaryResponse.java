package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created on 5/2/18.
 *
 * @author anil
 */
public class ContentSpaceUsageSummaryResponse implements Serializable {

    private String path;
    private Long sizeOnDevice;

    public ContentSpaceUsageSummaryResponse(String path, Long sizeOnDevice) {
        this.path = path;
        this.sizeOnDevice = sizeOnDevice;
    }

    public String getPath() {
        return path;
    }

    public Long getSizeOnDevice() {
        return sizeOnDevice;
    }
}
