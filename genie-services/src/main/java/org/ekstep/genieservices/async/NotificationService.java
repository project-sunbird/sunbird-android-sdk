package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.INotificationService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Notification;
import org.ekstep.genieservices.commons.bean.NotificationFilterCriteria;

import java.util.List;

/**
 * This class provides all the required APIs to perform necessary operations related to Notifications on a separate thread.
 */

public class NotificationService {
    private INotificationService notificationService;

    public NotificationService(GenieService genieService) {
        this.notificationService = genieService.getNotificationService();
    }

    /**
     * This api adds the notification.
     * <p>
     * On successful of adding the notification, the response will return status as TRUE and with {@link Notification} in the result.
     * <p>
     * <p>
     * On failing to add the notification, the response will return status as FALSE with the following error code
     * <p>ADD_FAILED
     *
     * @param notification    - {@link Notification}
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void addNotification(final Notification notification, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return notificationService.addNotification(notification);
            }
        });
    }

    /**
     * `
     * This api updates the status of notification
     * <p>
     * <p>
     * On successful of updating the notification status, the response will return status as TRUE and with {@link Notification} in the result.
     * <p>
     * <p>
     * On failing to update the notification, the response will return status as FALSE with the following error code
     * <p>UPDATE_FAILED
     * <p>
     *
     * @param notification    - {@link Notification}
     * @param responseHandler - {@link IResponseHandler<Notification>}
     */
    public void updateNotification(final Notification notification, IResponseHandler<Notification> responseHandler) {
        new AsyncHandler<Notification>(responseHandler).execute(new IPerformable<Notification>() {
            @Override
            public GenieResponse<Notification> perform() {
                return notificationService.updateNotification(notification);
            }
        });
    }

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
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void deleteNotification(final int msgId, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return notificationService.deleteNotification(msgId);
            }
        });
    }

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
     * @param criteria        - {@link NotificationFilterCriteria}
     * @param responseHandler - {@link IResponseHandler<List<Notification>>}
     */
    public void getAllNotifications(final NotificationFilterCriteria criteria, IResponseHandler<List<Notification>> responseHandler) {
        new AsyncHandler<List<Notification>>(responseHandler).execute(new IPerformable<List<Notification>>() {
            @Override
            public GenieResponse<List<Notification>> perform() {
                return notificationService.getAllNotifications(criteria);
            }
        });
    }

}
