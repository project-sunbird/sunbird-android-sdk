package org.ekstep.genieservices.commons.db.operations;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public interface IDataSource {
    IDBSession getReadOnlyDataSource(String filePath);

    IDBSession getReadWriteDataSource(String filePath);
}
