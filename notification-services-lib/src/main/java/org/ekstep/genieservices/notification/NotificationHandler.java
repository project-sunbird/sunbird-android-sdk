package org.ekstep.genieservices.notification;

import org.ekstep.genieservices.commons.bean.Notification;
import org.ekstep.genieservices.commons.bean.NotificationFilterCriteria;
import org.ekstep.genieservices.commons.bean.enums.NotificationStatus;
import org.ekstep.genieservices.commons.db.contract.NotificationEntry;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.notification.db.model.NotificationModel;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Indraja Machani on 6/12/2017.
 */

public class NotificationHandler {
    private static final String TAG = NotificationHandler.class.getSimpleName();

    private static final long MINUTE_MILLIS = 60 * 1000;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    private static final String KEY_MSG_ID = "msgid";
    private static final String KEY_RELATIVETIME = "relativetime";
    private static final String KEY_VALIDITY = "validity";
    private static final String KEY_TIME = "time";

    public static NotificationModel convertNotificationMapToModel(IDBSession dbSession, Notification notification) {
        String notificationJsonString = GsonUtil.toJson(notification);
        Map notificationData = GsonUtil.fromJson(notificationJsonString, Map.class);

        double msdId = readMsdId(notificationData);
        String displayTime = readDisplayTime(notificationData);
        long expiryTime = getComputeExpiryTime(notificationData, displayTime);
        Date receivedAt = new Date();

        NotificationModel notificationModel = NotificationModel.build(dbSession, msdId,
                displayTime, expiryTime, receivedAt, notificationJsonString);
        return notificationModel;
    }

    private static double readMsdId(Map notificationData) {
        if (notificationData.containsKey(KEY_MSG_ID)) {
            return Double.valueOf(notificationData.get(KEY_MSG_ID).toString());
        }
        return -1;
    }

    private static String readDisplayTime(Map notificationData) {
        String displayTime = null;
        if (notificationData.containsKey(KEY_TIME)) {
            displayTime = notificationData.get(KEY_TIME).toString();
        } else {
            displayTime = DateUtil.format(new Date(), DateUtil.DATE_FORMAT);
        }
        return displayTime;
    }

    private static long getComputeExpiryTime(Map notificationData, String displayTime) {
        long expiryTime = -1;
        try {
            if (notificationData.containsKey(KEY_RELATIVETIME)) {
                //Its Local notification
                Double relativeTime = Double.valueOf(notificationData.get(KEY_RELATIVETIME).toString());
                expiryTime = DateUtil.convertLocalTimeMillis(displayTime) + DAY_MILLIS;

            } else if (notificationData.containsKey(KEY_VALIDITY)) {
                //Its FCM push notification
                double validity = 0;
                validity = Double.valueOf(notificationData.get(KEY_VALIDITY).toString());
                expiryTime = DateUtil.convertLocalTimeMillis(displayTime) + (int) validity * MINUTE_MILLIS;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            expiryTime = new Date().getTime() + DAY_MILLIS;
        }

        return expiryTime;
    }

    public static String getFilterConditionToUpdate(double msgId) {
        //Update query to update  given  notification msg id
        if (msgId != -1) {
            String isValidNotification = String.format(Locale.US, "%s = '%s'", NotificationEntry.COLUMN_NAME_MESSAGE_ID, msgId);
            return String.format(Locale.US, " %s", isValidNotification);
        }
        //Update query for to update all valid notifications's status
        else {
            String isValidNotification = String.format(Locale.US, "%s = %s AND %s <= '%s' AND %s > '%s'",
                    NotificationEntry.COLUMN_NAME_IS_READ, 0, NotificationEntry.COLUMN_NAME_NOTIFICATION_DISPLAY_TIME, new Date().getTime(), NotificationEntry.COLUMN_NAME_EXPIRY_TIME,
                    new Date().getTime() - DAY_MILLIS);
            return String.format(Locale.US, " %s", isValidNotification);
        }
    }

    private static String getFilterConditionToGetAllNotifications() {
        String isValidNotification = String.format(Locale.US, "%s <= '%s' AND %s > '%s'", NotificationEntry.COLUMN_NAME_NOTIFICATION_DISPLAY_TIME, new Date().getTime(), NotificationEntry.COLUMN_NAME_EXPIRY_TIME, new Date().getTime());
        return String.format(Locale.US, " where %s", isValidNotification);
    }

    private static String getFilterConditionToGetUnreadNotifications() {
        String isValidNotification = String.format(Locale.US, "%s <= '%s' AND %s > '%s' AND %s = '%s'", NotificationEntry.COLUMN_NAME_NOTIFICATION_DISPLAY_TIME, new Date().getTime(), NotificationEntry.COLUMN_NAME_EXPIRY_TIME, new Date().getTime(), NotificationEntry.COLUMN_NAME_IS_READ, 0);
        return String.format(Locale.US, " where %s", isValidNotification);
    }

    private static String getFilterConditionToGetReadNotifications() {
        String isValidNotification = String.format(Locale.US, "%s <= '%s' AND %s > '%s' AND %s = '%s'", NotificationEntry.COLUMN_NAME_NOTIFICATION_DISPLAY_TIME, new Date().getTime(), NotificationEntry.COLUMN_NAME_EXPIRY_TIME, new Date().getTime(), NotificationEntry.COLUMN_NAME_IS_READ, 1);
        return String.format(Locale.US, " where %s", isValidNotification);
    }

    public static String getFilterCondition(NotificationFilterCriteria criteria) {
        NotificationStatus status = criteria.getNotificationStatus();

        if (status.getValue().equalsIgnoreCase(NotificationStatus.ALL.getValue()))
            return getFilterConditionToGetAllNotifications();
        else if (status.getValue().equalsIgnoreCase(NotificationStatus.UNREAD.getValue()))
            return getFilterConditionToGetUnreadNotifications();
        else if (status.getValue().equalsIgnoreCase(NotificationStatus.READ.getValue()))
            return getFilterConditionToGetReadNotifications();
        else
            return null;
    }

}
