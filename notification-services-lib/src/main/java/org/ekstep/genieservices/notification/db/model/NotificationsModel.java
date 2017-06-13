package org.ekstep.genieservices.notification.db.model;

import org.ekstep.genieservices.commons.bean.Notification;
import org.ekstep.genieservices.commons.db.contract.NotificationEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Indraja Machani on 6/12/2017.
 */

public class NotificationsModel implements IReadable, IUpdatable, ICleanable {
    private List<NotificationModel> mNotifications;

    private String mFilterCondition = null;
    private IDBSession mDBSession;


    public static NotificationsModel build(IDBSession dbSession, String filterCondition) {
        NotificationsModel notificationsModel = new NotificationsModel(dbSession, filterCondition);
        return notificationsModel;
    }

    public static NotificationsModel build(String filterCondition) {
        NotificationsModel notificationsModel = new NotificationsModel(filterCondition);
        return notificationsModel;
    }

    private NotificationsModel(IDBSession dbSession, String filterCondition) {
        mDBSession = dbSession;
        mFilterCondition = filterCondition;
        mNotifications = new ArrayList<>();
    }

    private NotificationsModel(String filterCondition) {
        mFilterCondition = filterCondition;
        mNotifications = new ArrayList<>();
    }

    public static String getFilterCondition() {
        String isValidNotification = String.format(Locale.US, "%s <= '%s' AND %s > '%s'",
                NotificationEntry.COLUMN_NAME_NOTIFICATION_DISPLAY_TIME,
                new Date().getTime(), NotificationEntry.COLUMN_NAME_EXPIRY_TIME, new Date().getTime());
        return String.format(Locale.US, " where %s", isValidNotification);
    }

    public Void update() {
        mDBSession.update(this);
        return null;
    }

    public Void delete() {
        mDBSession.clean(this);
        return null;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        // return null;
        if (resultSet != null && resultSet.moveToFirst()) {
            do {
                NotificationModel notification = NotificationModel.build();
                notification.readWithoutMoving(resultSet);
                mNotifications.add(notification);
            } while (resultSet.moveToNext());
        }

        return this;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        with(contentValues, NotificationEntry.COLUMN_NAME_IS_READ, 1);
        return contentValues;
    }

    public void with(ContentValues contentValues, String key, int value) {
        contentValues.put(key, value);
    }

    @Override
    public String getTableName() {
        return NotificationEntry.TABLE_NAME;
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s <= '%s' AND %s = '%s'",
                NotificationEntry.COLUMN_NAME_NOTIFICATION_RECEIVED_AT, new Date().getTime(),
                NotificationEntry.COLUMN_NAME_IS_READ, 0);
    }

    @Override
    public void clean() {

    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, " where %s", String.format(Locale.US, "%s <= '%s'",
                NotificationEntry.COLUMN_NAME_EXPIRY_TIME, new Date().getTime()));
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, " order by %s desc", NotificationEntry.COLUMN_NAME_NOTIFICATION_RECEIVED_AT);
    }

    @Override
    public String filterForRead() {
        return mFilterCondition;
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[0];
    }

    @Override
    public String limitBy() {
        return "";
    }

    public ArrayList<Map<String, Object>> asMap() {
        ArrayList<Map<String, Object>> contentsMap = new ArrayList<>();
        for (NotificationModel notification : this.mNotifications) {
            if (notification.getReadStatus() == 0) {
                notification.setReadStatus(1);
            }
            contentsMap.add(notification.getNotificationJsonMap());
        }
        return contentsMap;
    }

    public List<Notification> getNotificationBeans() {
        List<Notification> notifications = new ArrayList<>();
        for (NotificationModel notificationModel : this.mNotifications) {
            if (notificationModel.getReadStatus() == 0) {
                notificationModel.setReadStatus(1);
            }
            Notification notification = new Notification();
            notification.setNotificationJson(notificationModel.getNotificationJson());
            notifications.add(notification);
        }
        return notifications;
    }

    //Checks if any unread notification is there or not if there then update the read status(Only to avoid DbException)
    public void updateNotifications() {
        int count = 0;
        for (NotificationModel notification : mNotifications) {
            if (notification.getReadStatus() == 0) {
                count++;
            }
        }
        if (count > 0) {
            mDBSession.read(this);
        }

    }

    public static NotificationsModel find(IDBSession dbSession, String filter) {
        NotificationsModel contentsModel = new NotificationsModel(dbSession, filter);
        dbSession.read(contentsModel);

        if (contentsModel.getNotifications() == null) {
            return null;
        } else {
            return contentsModel;
        }
    }

    public List<NotificationModel> getNotifications() {
        return mNotifications;
    }


}
