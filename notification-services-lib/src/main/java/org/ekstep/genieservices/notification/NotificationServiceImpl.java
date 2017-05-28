package org.ekstep.genieservices.notification;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.INotificationService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;

public class NotificationServiceImpl  extends BaseService implements INotificationService{

    public NotificationServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Void> addNotification() {
        return null;
    }

    @Override
    public GenieResponse<Void> updateAllNotificationStatus() {
        return null;
    }

    @Override
    public GenieResponse<Void> updateNotificationStatus(int msgId) {
        return null;
    }

    @Override
    public GenieResponse<Void> getUnreadNotificationCount() {
        return null;
    }

    @Override
    public GenieResponse<Void> deleteNotification(int msgId) {
        return null;
    }

    @Override
    public GenieResponse<Void> getAllNotifications() {
        return null;
    }
}
