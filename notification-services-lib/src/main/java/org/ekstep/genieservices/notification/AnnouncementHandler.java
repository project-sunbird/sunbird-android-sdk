package org.ekstep.genieservices.notification;

import org.ekstep.genieservices.commons.bean.UpdateAnnouncementStateRequest;
import org.ekstep.genieservices.commons.bean.AnnouncementListRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by indraja on 30/3/18.
 */

public class AnnouncementHandler {

    public static Map<String, Object> getUserInboxRequestMap(AnnouncementListRequest announcementListRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("limit", announcementListRequest.getLimit());
        requestMap.put("offset", announcementListRequest.getOffset());
        return requestMap;
    }

    public static Map<String, Object> getUpdateAnnouncementRequestMap(UpdateAnnouncementStateRequest updateAnnouncementStateRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("announcementId", updateAnnouncementStateRequest.getAnnouncementId());
        requestMap.put("channel", UpdateAnnouncementStateRequest.Constant.CHANNEL);
        return requestMap;
    }

}
