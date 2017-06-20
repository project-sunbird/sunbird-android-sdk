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
     * <p>
     * On successful of adding the notification, the response will return status as TRUE and with {@link Notification} in the result.
     * <p>
     * <p>
     * On failing to add the notification, the response will return status as FALSE with the following error code
     * <p>ADD_FAILED
     *
     * @param notification - {@link Notification}
     * @return {@link GenieResponse<Notification>}
     */
    GenieResponse<Void> addNotification(Notification notification);

    /**
     * `
     * This api updates the status of the notification
     * <p>
     * <p>
     * On successful of updating the notification status, the response will return status as TRUE and with {@link Notification} in the result.
     * <p>
     * <p>
     * On failing to update the notification, the response will return status as FALSE with the following error code
     * <p>UPDATE_FAILED
     * <p>
     *
     * @param notification - {@link Notification}
     * @return {@link GenieResponse<Notification>}
     */
    GenieResponse<Notification> updateNotification(Notification notification);

    /**
     * This api deletes the specific notification by using msgId.
     * <p>
     * <p>
     * On successful deletion of the notification, the response will return status as TRUE in the result.
     * <p>
     * <p>
     * On failing to delete the notification, the response will return status as FALSE with the following error code
     * <p>DELETE_FAILED
     * <p>
     *
     * @param msgId
     */
    GenieResponse<Void> deleteNotification(int msgId);

    /**
     * This api gets all the notifications by using with criteria {@link NotificationFilterCriteria} param
     * <p>
     * <p>
     * On successful finding of the list of notifications, the response will return status as TRUE in the result.
     * <p>
     * <p>
     * On failing to find the notifications, the response will return status as FALSE with the following error code
     * <p>NO_NOTIFICATIONS_FOUND
     * <p>
     *
     * @param criteria - {@link NotificationFilterCriteria}
     * @return {@link GenieResponse<List<Notification>>}
     */
    GenieResponse<List<Notification>> getAllNotifications(NotificationFilterCriteria criteria);
}
