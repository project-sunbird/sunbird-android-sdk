package org.ekstep.genieservices.commons.db.core;

public interface IReadDb {

    IReadDb read(IResultSet cursor);

    String getTableName();

    String orderBy();

    String filterForRead();

    String[] selectionArgsForFilter();

    String limitBy();

}
