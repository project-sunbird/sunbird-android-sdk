package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Notification;
import org.ekstep.genieservices.commons.bean.NotificationFilterCriteria;

import java.util.List;

/**
 * This is the interface with all the required APIs to perform necessary operations related to Notifications.
 */

public interface INotificationService {

    /**
     * This api adds the notification.
     *
     * @return
     */
    GenieResponse<Void> addNotification(Notification notification);

    /**
     * `1`
     * This api updates the status of all notifications.
     *
     * @return
     */
    GenieResponse<Notification> updateNotification(Notification notification);

    /**
     * This api gets all the unread notifications.
     *
     * @return
     */
    GenieResponse<Integer> getUnreadNotificationCount(NotificationFilterCriteria notificationFilterCriteria);

    /**
     * This api deletes the specific notification.
     *
     * @param msgId
     * @return
     */
    GenieResponse<Void> deleteNotification(int msgId);

    /**
     * This api gets all the notifications.
     *
     * @return
     */
    GenieResponse<List<Notification>> getAllNotifications(NotificationFilterCriteria notificationFilterCriteria);
}
