package org.ekstep.genieservices.commons.db.model;

import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

/**
 * Created on 16/5/17.
 *
 * @author swayangjit
 */
public class CustomReaderModel implements IReadable {

    private IDBSession dbSession;
    private String query;
    private String data;

    private CustomReaderModel(IDBSession dbSession, String query) {
        this.dbSession = dbSession;
        this.query = query;
    }

    public static CustomReaderModel find(IDBSession dbSession, String query) {
        CustomReaderModel customReaderModel = new CustomReaderModel(dbSession, query);
        dbSession.read(customReaderModel, query);

        if (customReaderModel.data == null) {
            return null;
        } else {
            return customReaderModel;
        }
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            data = cursor.getString(0);
        }
        return this;
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public String orderBy() {
        return null;
    }

    @Override
    public String filterForRead() {
        return null;
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[0];
    }

    @Override
    public String limitBy() {
        return null;
    }

    public String getData() {
        return data;
    }
}
