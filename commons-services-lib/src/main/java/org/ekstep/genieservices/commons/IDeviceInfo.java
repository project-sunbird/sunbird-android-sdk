package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.bean.telemetry.DeviceSpecification;

/**
 * Created on 24/4/17.
 *
 * @author shriharsh
 */

public interface IDeviceInfo {

    String getDeviceID();

    DeviceSpecification getDeviceDetails();

}
