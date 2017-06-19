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
     * This api adds the notification
     *
     * @param responseHandler
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
     * This api updates the status of all notifications.
     *
     * @param responseHandler
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
     * This api deletes the specific notification.
     *
     * @param msgId
     * @param responseHandler
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
     * This api gets unread notifications count.
     *
     * @param responseHandler
     */
    public void getUnreadNotificationCount(final NotificationFilterCriteria criteria, IResponseHandler<Integer> responseHandler) {
        new AsyncHandler<Integer>(responseHandler).execute(new IPerformable<Integer>() {
            @Override
            public GenieResponse<Integer> perform() {
                return notificationService.getUnreadNotificationCount(criteria);
            }
        });
    }


    /**
     * This api gets all the notifications.
     *
     * @param responseHandler
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
