package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public abstract class GameEntry implements BaseColumns {

    public static final String TABLE_NAME = "game_list";
    public static final String COLUMN_NAME_DATA = "data";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + GameEntry.TABLE_NAME + " (" +
                GameEntry._ID + " INTEGER PRIMARY KEY," +
                GameEntry.COLUMN_NAME_DATA + DbConstants.TEXT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME;
    }

}
