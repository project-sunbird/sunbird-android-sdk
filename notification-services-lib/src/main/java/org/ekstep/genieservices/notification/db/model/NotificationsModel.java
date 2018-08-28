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

/**
 * Created by Indraja Machani on 6/12/2017.
 */

public class NotificationsModel implements IReadable, IUpdatable, ICleanable {
    private List<NotificationModel> mNotifications;

    private String mFilterCondition = null;
    private IDBSession mDBSession;

    private NotificationsModel(IDBSession dbSession, String filterCondition) {
        mDBSession = dbSession;
        mFilterCondition = filterCondition;
        mNotifications = new ArrayList<>();
    }

    public static NotificationsModel build(IDBSession dbSession, String filterCondition) {
        NotificationsModel notificationsModel = new NotificationsModel(dbSession, filterCondition);
        return notificationsModel;
    }

    public static NotificationsModel find(IDBSession dbSession, String filter) {
        NotificationsModel notificationsModel = new NotificationsModel(dbSession, filter);
        dbSession.read(notificationsModel);

        if (notificationsModel.getNotifications() == null) {
            return null;
        } else {
            return notificationsModel;
        }
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

    public Void update() {
        mDBSession.update(this);
        return null;
    }

    public Void delete() {
        mDBSession.clean(this);
        return null;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationEntry.COLUMN_NAME_IS_READ, 1);
        return contentValues;
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

    public List<Notification> getNotificationBeans() {
        List<Notification> notifications = new ArrayList<>();
        for (NotificationModel notificationModel : this.mNotifications) {
            if (notificationModel.getReadStatus() == 0) {
                notificationModel.setReadStatus(1);
            }
            Notification notification = new Notification();
            notification.setMsgid((int) notificationModel.getNotificationMessageId());
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

    public List<NotificationModel> getNotifications() {
        return mNotifications;
    }

}
