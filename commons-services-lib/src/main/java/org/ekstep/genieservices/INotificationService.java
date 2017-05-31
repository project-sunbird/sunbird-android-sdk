package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * This is the interface with all the required APIs to perform necessary operations related to Notifications.
 */

public interface INotificationService {

    /**
     * This api adds the notification.
     *
     * @return
     */
    GenieResponse<Void> addNotification();

    /**
     * This api updates the status of all notifications.
     *
     * @return
     */
    GenieResponse<Void> updateAllNotificationStatus();

    /**
     * This api updates the status of a specific notification.
     *
     * @param msgId
     * @return
     */
    GenieResponse<Void> updateNotificationStatus(int msgId);

    /**
     * This api gets all the unread notifications.
     *
     * @return
     */
    GenieResponse<Void> getUnreadNotificationCount();

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
    GenieResponse<Void> getAllNotifications();
}
