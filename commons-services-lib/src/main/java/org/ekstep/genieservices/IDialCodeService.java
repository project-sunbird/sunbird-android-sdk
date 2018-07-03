package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.DialCodeRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

/**
 * Created by swayangjit on 2/7/18.
 */
public interface IDialCodeService {
    GenieResponse<Map<String, Object>> getDialCode(DialCodeRequest dialCodeRequest);
}
