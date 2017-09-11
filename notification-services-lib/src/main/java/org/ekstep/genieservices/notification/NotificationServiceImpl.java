package org.ekstep.genieservices.notification;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.INotificationService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Notification;
import org.ekstep.genieservices.commons.bean.NotificationFilterCriteria;
import org.ekstep.genieservices.notification.db.model.NotificationModel;
import org.ekstep.genieservices.notification.db.model.NotificationsModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationServiceImpl extends BaseService implements INotificationService {
    private static final String TAG = NotificationServiceImpl.class.getSimpleName();

    public NotificationServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Void> addNotification(Notification notification) {

        if (notification != null) {
            NotificationModel notificationModel = NotificationHandler.convertNotificationMapToModel(mAppContext.getDBSession(), notification);
            NotificationModel oldNotification = NotificationModel.findById(mAppContext.getDBSession(), notification.getMsgid());

            if (oldNotification != null) {
                notificationModel.update();
            } else {
                notificationModel.save();
            }

            return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        }
        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.ADD_FAILED, ServiceConstants.ErrorMessage.FAILED_TO_ADD_UPDATE_NOTIFICATION, TAG);
    }

    @Override
    public GenieResponse<Notification> updateNotification(Notification notification) {
        // -1 to update all the notifications
        NotificationsModel notificationsUpdate = NotificationsModel.build(mAppContext.getDBSession(), NotificationHandler.getFilterConditionToUpdate(notification.getMsgid()));
        notificationsUpdate.update();
        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

    @Override
    public GenieResponse<Void> deleteNotification(int msgId) {
        NotificationModel notification = NotificationModel.findById(mAppContext.getDBSession(), msgId);
        if (notification != null) {
            notification.delete();
            return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        }
        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.DELETE_FAILED, ServiceConstants.ErrorMessage.FAILED_TO_DELETE_NOTIFICATION, TAG);

    }

    @Override
    public GenieResponse<List<Notification>> getAllNotifications(NotificationFilterCriteria criteria) {

        NotificationsModel notificationModel = NotificationsModel.build(mAppContext.getDBSession(), NotificationHandler.getFilterCondition(criteria));
        GenieResponse<List<Notification>> successResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        //Deletes all expired notifications
        notificationModel.delete();
        //Reads all valid notifications
        NotificationsModel notifications = NotificationsModel.find(mAppContext.getDBSession(), NotificationHandler.getFilterCondition(criteria));
        if (notifications == null) {
            successResponse.setResult(new ArrayList<Notification>());
        } else {
            successResponse.setResult(notifications.getNotificationBeans());
        }
        return successResponse;
    }
}
