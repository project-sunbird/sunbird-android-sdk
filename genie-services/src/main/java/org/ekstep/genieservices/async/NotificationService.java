package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.INotificationService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.List;

/**
 * Created by swayangjit on 25/5/17.
 */

public class NotificationService {
    private INotificationService notificationService;

    public NotificationService(GenieService genieService) {
        this.notificationService = genieService.getNotificationService();
    }

    public void addNotification(IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return notificationService.addNotification();
            }
        });
    }

    public void updateAllNotificationStatus(IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return notificationService.updateAllNotificationStatus();
            }
        });
    }

    public void updateNotificationStatus(final int notificationId,IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return notificationService.updateNotificationStatus(notificationId);
            }
        });
    }

    public void deleteNotification(final int msgId, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return notificationService.deleteNotification(msgId);
            }
        });
    }

    public void getAllNotifications(IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return notificationService.getAllNotifications();
            }
        });
    }
}
