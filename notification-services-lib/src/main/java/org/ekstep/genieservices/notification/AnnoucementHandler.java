package org.ekstep.genieservices.notification;

import org.ekstep.genieservices.commons.bean.UserInboxRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by indraja on 30/3/18.
 */

public class AnnoucementHandler {

    public static Map<String, Object> getUserInboxRequestMap(UserInboxRequest userInboxRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("limit", userInboxRequest.getLimit());
        requestMap.put("offset", userInboxRequest.getOffset());
        return requestMap;
    }

}
