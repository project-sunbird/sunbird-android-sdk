package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

/**
 * This class represents the table structure of PartnerEntry
 */
public abstract class PartnerEntry implements BaseColumns {

    public static final String TABLE_NAME = "partners";
    public static final String COLUMN_NAME_UID = "partnerID";
    public static final String COLUMN_NAME_KEY = "publicKey";
    public static final String COLUMN_NAME_KEY_ID = "publicKeyID";

    /**
     * This method gives the SQL create statement for creating {@link PartnerEntry} table
     *
     * @return
     */
    public static final String getCreateEntry() {
        return "CREATE TABLE " + PartnerEntry.TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + DbConstants.COMMA_SEP +
                COLUMN_NAME_UID + DbConstants.TEXT_TYPE + " UNIQUE NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_KEY + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_KEY_ID + DbConstants.TEXT_TYPE + " NOT NULL" +
                " )";
    }

    /**
     * This method gives the SQL drop statement for deleting {@link PartnerEntry} table
     *
     * @return
     */
    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
