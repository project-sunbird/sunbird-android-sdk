package org.ekstep.genieservices.commons.db.core;

import org.ekstep.genieservices.commons.db.core.impl.SqliteResultSet;

public interface IReadDb {

    IReadDb read(SqliteResultSet cursor);

    String getTableName();

    String orderBy();

    String filterForRead();

    String[] selectionArgsForFilter();

    String limitBy();

}
