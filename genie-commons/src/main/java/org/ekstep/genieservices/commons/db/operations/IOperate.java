package org.ekstep.genieservices.commons.db.operations;

import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.ServiceDbHelper;


public interface IOperate {
    Void perform(SQLiteDatabase db);
    SQLiteDatabase getConnection(ServiceDbHelper dbHelper);
    void beforePerform(AppContext context);
}
