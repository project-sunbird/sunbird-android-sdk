package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created by swayangjit on 25/5/17.
 */

public interface INotificationService {

    GenieResponse<Void> addNotification();

    GenieResponse<Void> updateAllNotificationStatus();

    GenieResponse<Void> updateNotificationStatus(int msgId);

    GenieResponse<Void> getUnreadNotificationCount();

    GenieResponse<Void> deleteNotification(int msgId);

    GenieResponse<Void> getAllNotifications();
}
