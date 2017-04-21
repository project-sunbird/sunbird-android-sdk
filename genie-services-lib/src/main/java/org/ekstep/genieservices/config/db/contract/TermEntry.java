package org.ekstep.genieservices.config.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

/**
 * Created on 17/2/17.
 *
 * @author swayangjit
 */
public abstract class TermEntry implements BaseColumns {

    public static final String TABLE_NAME = "term";
    public static final String COLUMN_NAME_IDENTIFIER = "identifier";
    public static final String COLUMN_NAME_TERM_TYPE = "facet_type";
    public static final String COLUMN_NAME_TERM_JSON = "facet_json";

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + TermEntry.TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_IDENTIFIER + DbConstants.TEXT_TYPE + "," +
                COLUMN_NAME_TERM_TYPE + DbConstants.TEXT_TYPE + "," +
                COLUMN_NAME_TERM_JSON + DbConstants.TEXT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
