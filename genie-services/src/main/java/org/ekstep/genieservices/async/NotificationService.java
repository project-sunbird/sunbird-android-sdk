package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.INotificationService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Notification;

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
    public void addNotification(final String notificationJson, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return notificationService.addNotification(notificationJson);
            }
        });
    }

    /**
     * This api updates the status of all notifications.
     *
     * @param responseHandler
     */
    public void updateAllNotificationStatus(IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return notificationService.updateAllNotificationStatus();
            }
        });
    }

    /**
     * This api updates the status of a specific notification.
     *
     * @param notificationId
     * @param responseHandler
     */
    public void updateNotificationStatus(final int notificationId, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return notificationService.updateNotificationStatus(notificationId);
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
     * This api gets all the notifications.
     *
     * @param responseHandler
     */
    public void getUnreadNotificationCount(IResponseHandler<Integer> responseHandler) {
        new AsyncHandler<Integer>(responseHandler).execute(new IPerformable<Integer>() {
            @Override
            public GenieResponse<Integer> perform() {
                return notificationService.getUnreadNotificationCount();
            }
        });
    }


    /**
     * This api gets all the notifications.
     *
     * @param responseHandler
     */
    public void getAllNotifications(IResponseHandler<List<Notification>> responseHandler) {
        new AsyncHandler<List<Notification>>(responseHandler).execute(new IPerformable<List<Notification>>() {
            @Override
            public GenieResponse<List<Notification>> perform() {
                return notificationService.getAllNotifications();
            }
        });
    }

}
