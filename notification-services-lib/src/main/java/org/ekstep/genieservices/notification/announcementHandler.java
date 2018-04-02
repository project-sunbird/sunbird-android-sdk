package org.ekstep.genieservices.notification;

import org.ekstep.genieservices.commons.bean.ReceivedAnnouncementRequest;
import org.ekstep.genieservices.commons.bean.UserInboxRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by indraja on 30/3/18.
 */

public class announcementHandler {

    public static Map<String, Object> getUserInboxRequestMap(UserInboxRequest userInboxRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("limit", userInboxRequest.getLimit());
        requestMap.put("offset", userInboxRequest.getOffset());
        return requestMap;
    }

    public static Map<String, Object> getReceivedAnnouncementRequestMap(ReceivedAnnouncementRequest receivedAnnouncementRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("announcementId", receivedAnnouncementRequest.getAnnouncementId());
        requestMap.put("channel", receivedAnnouncementRequest.getChannel());
        return requestMap;
    }

}
