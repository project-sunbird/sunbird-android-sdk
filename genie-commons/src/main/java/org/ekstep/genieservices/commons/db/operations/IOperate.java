package org.ekstep.genieservices.commons.db.operations;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.db.ServiceDbHelper;


public interface IOperate {
    Void perform(SQLiteDatabase db);
    SQLiteDatabase getConnection(ServiceDbHelper dbHelper);
    void beforePerform(Context context);
}
