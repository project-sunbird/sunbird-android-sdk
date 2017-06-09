package org.ekstep.genieservices.commons.db.operations;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public interface IDataSource {
    IDBSession getImportDataSource(String filePath);
}
