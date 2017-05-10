package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public abstract class PartnerEntry implements BaseColumns {

    public static final String TABLE_NAME = "partners";
    public static final String COLUMN_NAME_UID = "partnerID";
    public static final String COLUMN_NAME_KEY = "publicKey";
    public static final String COLUMN_NAME_KEY_ID = "publicKeyID";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + PartnerEntry.TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + DbConstants.COMMA_SEP +
                COLUMN_NAME_UID + DbConstants.TEXT_TYPE + " UNIQUE NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_KEY + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_KEY_ID + DbConstants.TEXT_TYPE + " NOT NULL" +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
