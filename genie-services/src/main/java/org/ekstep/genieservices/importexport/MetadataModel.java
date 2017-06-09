package org.ekstep.genieservices.importexport;

import org.ekstep.genieservices.commons.db.contract.MetaEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class MetadataModel implements IReadable {

    private Map<String, Object> metadata;

    private MetadataModel() {
    }

    public static MetadataModel findAll(IDBSession dbSession) {
        MetadataModel model = new MetadataModel();
        dbSession.read(model);

        if (model.metadata == null) {
            return null;
        } else {
            return model;
        }
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            metadata = new HashMap<>();
            do {
                metadata.put(resultSet.getString(1), resultSet.getString(2));
            } while (resultSet.moveToNext());
        }
        return this;
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return "";
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[0];
    }

    @Override
    public String limitBy() {
        return "";
    }

    @Override
    public String getTableName() {
        return MetaEntry.TABLE_NAME;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
