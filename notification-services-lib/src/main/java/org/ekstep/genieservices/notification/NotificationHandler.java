package org.ekstep.genieservices.notification;

import org.ekstep.genieservices.commons.db.contract.NotificationEntry;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.MapUtil;
import org.ekstep.genieservices.commons.utils.TimeUtil;
import org.ekstep.genieservices.notification.db.model.NotificationModel;
import org.ekstep.genieservices.notification.db.model.NotificationsModel;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    public static NotificationModel getNotificationModel(IDBSession dbSession, String notificationJson) {
        Map notificationData = MapUtil.toMap(notificationJson);

        double mMsgId = readMsdId(notificationData);
        String mDisplayTime = readDisplayTime(notificationData);
        long mExpiryTime = getComputeExpiryTime(notificationData, mDisplayTime);
        Date receivedAt = new Date();

        NotificationModel notificationModel = NotificationModel.build(dbSession, mMsgId,
                mDisplayTime, mExpiryTime, receivedAt, notificationJson);
        return notificationModel;
    }

    private static double readMsdId(Map notificationData) {
        if (notificationData.containsKey(KEY_MSG_ID)) {
            return Double.valueOf(notificationData.get(KEY_MSG_ID).toString());
        }
        return -1;
    }

    private static String readDisplayTime(Map notificationData) {
        String mDisplayTime = null;
        if (notificationData.containsKey(KEY_TIME)) {
            mDisplayTime = notificationData.get(KEY_TIME).toString();
        } else {
            mDisplayTime = TimeUtil.getEpochTimeStamp();
        }
        return mDisplayTime;
    }

    private static long getComputeExpiryTime(Map notificationData, String mDisplayTime) {
        long mExpiryTime = -1;
        try {
            if (notificationData.containsKey(KEY_RELATIVETIME)) {
                //Its Local notification
                Double relativeTime = Double.valueOf(notificationData.get(KEY_RELATIVETIME).toString());
                mExpiryTime = TimeUtil.convertLocalTimeMillis(mDisplayTime) +/*relativeTime.longValue()* HOUR_MILLIS*/DAY_MILLIS;

            } else if (notificationData.containsKey(KEY_VALIDITY)) {
                //Its FCM push notification
                double validity = 0;
                validity = Double.valueOf(notificationData.get(KEY_VALIDITY).toString());
                mExpiryTime = TimeUtil.convertLocalTimeMillis(mDisplayTime) + (int) validity * MINUTE_MILLIS;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            mExpiryTime = new Date().getTime() + DAY_MILLIS;
        }

        return mExpiryTime;
    }

    public static String getUpdateCondition(int msgId) {
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

    private static String getFilterCondition() {
        String isValidNotification = String.format(Locale.US, "%s <= '%s' AND %s > '%s' AND %s = '%s'", NotificationEntry.COLUMN_NAME_NOTIFICATION_DISPLAY_TIME, new Date().getTime(), NotificationEntry.COLUMN_NAME_EXPIRY_TIME, new Date().getTime(), NotificationEntry.COLUMN_NAME_IS_READ, 0);
        return String.format(Locale.US, " where %s", isValidNotification);
    }

    private static int getAllUnReadNotifications(IDBSession dbSession) {
        NotificationsModel notifications = NotificationsModel.build(getFilterCondition());
        dbSession.read(notifications);
        List<NotificationModel> notificationList = notifications.getNotifications();
        return notificationList.size();
    }

    // TODO it will be removed in future, once if it is not required
    public static Map<String, Object> getUnreadNotificationCountMap(IDBSession dbSession) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count", NotificationHandler.getAllUnReadNotifications(dbSession));
        return map;
    }

    // TODO it will be removed in future, once if it is not required
    public static Map<String, Object> getAllNotifications(NotificationsModel notifications) {
        Map<String, Object> result = new HashMap<>();
        result.put("notifications", notifications.asMap());
        return result;
    }

    public static Integer getUnreadNotificationCount(IDBSession dbSession) {
        return NotificationHandler.getAllUnReadNotifications(dbSession);
    }

}
