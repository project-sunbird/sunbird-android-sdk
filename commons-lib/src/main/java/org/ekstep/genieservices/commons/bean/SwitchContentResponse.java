package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.MoveContentStatus;
import org.ekstep.genieservices.commons.bean.enums.SwitchContentStatus;

import java.io.Serializable;

/**
 * This class tells about the status of action after switching.
 * <p>
 * Status - {@link MoveContentStatus}
 * shriharsh
 */

public class SwitchContentResponse implements Serializable {

    private SwitchContentStatus status;

    public SwitchContentResponse(SwitchContentStatus status) {
        this.status = status;
    }

    public SwitchContentStatus getStatus() {
        return status;
    }

}
