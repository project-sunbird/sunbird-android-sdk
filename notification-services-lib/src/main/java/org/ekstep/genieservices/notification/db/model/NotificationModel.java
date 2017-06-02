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

    private NotificationModel(String notificationJson) {
        this.mNotificationJson = notificationJson;
    }

    public static NotificationModel build(IDBSession dbSession, String notificationJson) {
        NotificationModel notificationModel = new NotificationModel(notificationJson);
        return notificationModel;
    }

    @Override
    public ContentValues getContentValues() {
        return null;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        return null;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        return null;
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
}
