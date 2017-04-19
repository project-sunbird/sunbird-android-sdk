package org.ekstep.genieservices.notification.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

/**
 * Created on 24/1/17.
 *
 * @author swayangjit
 */
public class NotificationEntry implements BaseColumns {

    public static final String TABLE_NAME = "notifications";
    public static final String COLUMN_NAME_MESSAGE_ID = "message_id";
    public static final String COLUMN_NAME_EXPIRY_TIME = "expiry_time";
    public static final String COLUMN_NAME_NOTIFICATION_DISPLAY_TIME = "display_time";
    public static final String COLUMN_NAME_NOTIFICATION_RECEIVED_AT = "received_at";
    public static final String COLUMN_NAME_NOTIFICATION_JSON = "notification_json";
    public static final String COLUMN_NAME_IS_READ = "is_read";

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + NotificationEntry.TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_MESSAGE_ID + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_EXPIRY_TIME + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_NOTIFICATION_DISPLAY_TIME + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_NOTIFICATION_RECEIVED_AT + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_NOTIFICATION_JSON + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_IS_READ + DbConstants.INT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
