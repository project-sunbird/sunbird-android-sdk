package org.ekstep.genieservices.commons.db.operations;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.ContentValues;

/**
 * @author anil
 */
public interface IWriteToDb {

    ContentValues getContentValues();

    void updateId(long id);

    String getTableName();

    void beforeWrite(AppContext context);

}
