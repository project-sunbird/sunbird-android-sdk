package org.ekstep.genieservices.commons.db.core;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.impl.ContentValues;

/**
 * @author anil
 */
public interface IWriteToDb {

    ContentValues getContentValues();

    void updateId(long id);

    String getTableName();

    void beforeWrite(AppContext context);

}
