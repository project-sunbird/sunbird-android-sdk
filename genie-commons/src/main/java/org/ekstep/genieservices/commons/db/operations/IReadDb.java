package org.ekstep.genieservices.commons.db.operations;

import org.ekstep.genieservices.commons.db.operations.impl.SqliteResultSet;

public interface IReadDb {
    IReadDb read(SqliteResultSet cursor);
    String getTableName();
    String orderBy();
    String filterForRead();
    String[] selectionArgsForFilter();
    String limitBy();
}
