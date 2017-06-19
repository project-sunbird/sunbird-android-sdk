package org.ekstep.genieservices.notification.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.NotificationEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 5/28/2017.
 *
 * @author anil
 */
public class NotificationModel implements IWritable, IUpdatable, IReadable, ICleanable {

    private IDBSession mDBSession;

    private Long id = -1L;

    private double mMsgId;
    private long mExpiryTime;
    private String mDisplayTime;
    private Date receivedAt;
    private String mNotificationJson;
    private int isRead = 0;

    private long displayTimeinMillis;
    private long receivedAtTime;

    private static final String KEY_ISREAD = "isRead";

    private NotificationModel() {
    }

    private NotificationModel(IDBSession dbSession, double msgId) {
        this.mDBSession = dbSession;
        this.mMsgId = msgId;
    }

    private NotificationModel(IDBSession dbSession, double mMsgId, String mDisplayTime, long mExpiryTime,
                              Date receivedAt, String mNotificationJson) {
        this.mDBSession = dbSession;
        this.mMsgId = mMsgId;
        this.mExpiryTime = mExpiryTime;
        this.mDisplayTime = mDisplayTime;
        this.receivedAt = receivedAt;
        this.mNotificationJson = mNotificationJson;
    }

    public static NotificationModel build() {
        NotificationModel notificationModel = new NotificationModel();
        return notificationModel;
    }

    public static NotificationModel build(IDBSession dbSession, double msgId) {
        NotificationModel notificationModel = new NotificationModel(dbSession, msgId);
        return notificationModel;
    }

    public static NotificationModel build(IDBSession dbSession, double mMsgId, String mDisplayTime, long mExpiryTime,
                                          Date receivedAt, String mNotificationJson) {
        NotificationModel notificationModel = new NotificationModel(dbSession, mMsgId, mDisplayTime, mExpiryTime,
                receivedAt, mNotificationJson);
        return notificationModel;
    }

    public static NotificationModel find(IDBSession dbSession, double msgId) {
        NotificationModel notification = new NotificationModel(dbSession, msgId);
        dbSession.read(notification);

        if (notification.id == -1L) {
            return null;
        } else {
            return notification;
        }
    }

    public Void save() {
        mDBSession.create(this);
        return null;
    }

    public Void update() {
        mDBSession.update(this);
        return null;
    }

    @Override
    public ContentValues getContentValues() {
//        return null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationEntry.COLUMN_NAME_MESSAGE_ID, mMsgId);
        contentValues.put(NotificationEntry.COLUMN_NAME_EXPIRY_TIME, mExpiryTime);
        contentValues.put(NotificationEntry.COLUMN_NAME_NOTIFICATION_DISPLAY_TIME, getDisplayTime());
        contentValues.put(NotificationEntry.COLUMN_NAME_NOTIFICATION_RECEIVED_AT, receivedAt.getTime());
        with(contentValues, NotificationEntry.COLUMN_NAME_NOTIFICATION_JSON, mNotificationJson);
        contentValues.put(NotificationEntry.COLUMN_NAME_IS_READ, isRead);

        return contentValues;
    }

    private void with(ContentValues contentValues, String key, String value) {
        if (value != null) {
            contentValues.put(key, value);
        }
    }

    private long getDisplayTime() {
        long displayTime = 0;
        try {
            displayTime = DateUtil.convertLocalTimeMillis(mDisplayTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return displayTime;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        // return null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationEntry.COLUMN_NAME_MESSAGE_ID, mMsgId);
        contentValues.put(NotificationEntry.COLUMN_NAME_EXPIRY_TIME, mExpiryTime);
        contentValues.put(NotificationEntry.COLUMN_NAME_NOTIFICATION_DISPLAY_TIME, getDisplayTime());
        contentValues.put(NotificationEntry.COLUMN_NAME_NOTIFICATION_RECEIVED_AT, receivedAt.getTime());
        with(contentValues, NotificationEntry.COLUMN_NAME_NOTIFICATION_JSON, mNotificationJson);
        contentValues.put(NotificationEntry.COLUMN_NAME_IS_READ, isRead);
        return contentValues;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        // return null;
        if (resultSet != null && resultSet.moveToFirst()) {
            readWithoutMoving(resultSet);
        }

        return this;
    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(0);
        mMsgId = resultSet.getLong(resultSet.getColumnIndex(NotificationEntry.COLUMN_NAME_MESSAGE_ID));
        mExpiryTime = resultSet.getLong(resultSet.getColumnIndex(NotificationEntry.COLUMN_NAME_EXPIRY_TIME));
        displayTimeinMillis = resultSet.getLong(resultSet.getColumnIndex(NotificationEntry.COLUMN_NAME_NOTIFICATION_DISPLAY_TIME));
        mDisplayTime = resultSet.getString(resultSet.getColumnIndex(NotificationEntry.COLUMN_NAME_NOTIFICATION_DISPLAY_TIME));

        receivedAtTime = resultSet.getLong(resultSet.getColumnIndex(NotificationEntry.COLUMN_NAME_NOTIFICATION_RECEIVED_AT));
        receivedAt = new Date(receivedAtTime);

        mNotificationJson = resultSet.getString(resultSet.getColumnIndex(NotificationEntry.COLUMN_NAME_NOTIFICATION_JSON));
        isRead = resultSet.getInt(resultSet.getColumnIndex(NotificationEntry.COLUMN_NAME_IS_READ));
    }

    @Override
    public String getTableName() {
        return NotificationEntry.TABLE_NAME;
    }

    @Override
    public void clean() {
        id = -1L;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "WHERE %s = %f", NotificationEntry.COLUMN_NAME_MESSAGE_ID, mMsgId);
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, " order by %s desc", NotificationEntry.COLUMN_NAME_NOTIFICATION_RECEIVED_AT);
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "WHERE %s = ?", NotificationEntry.COLUMN_NAME_MESSAGE_ID);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[]{String.valueOf(mMsgId)};
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s = '%s'", NotificationEntry.COLUMN_NAME_MESSAGE_ID, mMsgId);
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    public String getNotificationJson() {
        return mNotificationJson;
    }

    public String getmDisplayTime() {
        return mDisplayTime;
    }

    public long getmExpiryTime() {
        return mExpiryTime;
    }

    public double getNotificationMessageId() {
        return mMsgId;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public long getDisplayTimeinMillis() {
        return displayTimeinMillis;
    }

    public long getReceivedAtTime() {
        return receivedAtTime;
    }

    public int getReadStatus() {
        return isRead;
    }

    public void setReadStatus(int isRead) {
        this.isRead = isRead;
    }

}
