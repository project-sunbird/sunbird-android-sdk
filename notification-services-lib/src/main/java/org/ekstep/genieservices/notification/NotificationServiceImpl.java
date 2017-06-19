package org.ekstep.genieservices.notification;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.INotificationService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Notification;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.exception.InvalidDataException;
import org.ekstep.genieservices.notification.db.model.NotificationModel;
import org.ekstep.genieservices.notification.db.model.NotificationsModel;

import java.util.List;

public class NotificationServiceImpl extends BaseService implements INotificationService {

    public NotificationServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Void> addNotification(String notificationJson) {
        try {
            NotificationModel notification = NotificationHandler.convertNotificationMapToModel(mAppContext.getDBSession(), notificationJson);
            NotificationModel oldNotification = NotificationModel.find(mAppContext.getDBSession(), notification.getNotificationMessageId());

            if (oldNotification != null) {
                notification.update();
            } else {
                notification.save();
            }

            return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        } catch (Exception e) {
            e.printStackTrace();
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE,
                    ServiceConstants.FAILED_TO_ADD_UPDATE_NOTIFICATION, "");
        }
    }

    @Override
    public GenieResponse<Void> updateAllNotificationStatus() {
        // -1 to update all the notifications
        int msgId = -1;
        return updateNotificationStatus(msgId);
    }

    @Override
    public GenieResponse<Void> updateNotificationStatus(int msgId) {
        String errorMessage = "Failed to update the notification";
        try {
            NotificationsModel notificationsUpdate = NotificationsModel.build(mAppContext.getDBSession(), NotificationHandler.getFilterConditionToUpdate(msgId));
            notificationsUpdate.update();
            return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        } catch (Exception e) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE, errorMessage, errorMessage);
        }
    }

    @Override
    public GenieResponse<Integer> getUnreadNotificationCount() {
        String errorMessage = "Not able to fetch notifications";
        GenieResponse<Integer> genieResponse = null;
        try {
            genieResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            genieResponse.setResult(NotificationHandler.getUnreadNotificationCount(mAppContext.getDBSession()));
            return genieResponse;
        } catch (DbException e) {
            e.printStackTrace();
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE, errorMessage, errorMessage);
        } catch (InvalidDataException e) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE, errorMessage, errorMessage);
        }
    }

    @Override
    public GenieResponse<Void> deleteNotification(int msgId) {
        String errorMessage = "Failed to delete notification";
        try {
            NotificationModel notification = NotificationModel.build(mAppContext.getDBSession(), msgId);
            mAppContext.getDBSession().clean(notification);

            return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        } catch (Exception e) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE, errorMessage, errorMessage);
        }
    }

    @Override
    public GenieResponse<List<Notification>> getAllNotifications() {
        try {
            NotificationsModel notifications = NotificationsModel.build(mAppContext.getDBSession(), NotificationHandler.getFilterCondition());

            //Deletes all expired notifications
            mAppContext.getDBSession().clean(notifications);
            //Reads all valid notifications
            mAppContext.getDBSession().read(notifications);

            GenieResponse successResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            List<Notification> notificationBeans = notifications.getNotificationBeans();
//
            successResponse.setResult(notificationBeans);
            return successResponse;
        } catch (Exception e) {
            String errorMessage = "Error while getting notifications";
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE, errorMessage, errorMessage);
        }
    }
}
