package org.ekstep.genieservices.commons.db.model;

import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 12/6/17.
 *
 * @author anil
 */
public class CustomReadersModel implements IReadable {

    private IDBSession dbSession;
    private String query;
    private List<String> dataList;

    private CustomReadersModel(IDBSession dbSession, String query) {
        this.dbSession = dbSession;
        this.query = query;
    }

    public static CustomReadersModel find(IDBSession dbSession, String query) {
        CustomReadersModel customReaderModel = new CustomReadersModel(dbSession, query);
        dbSession.read(customReaderModel, query);

        if (customReaderModel.dataList == null) {
            return null;
        } else {
            return customReaderModel;
        }
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            dataList = new ArrayList<>();

            do {
                dataList.add(resultSet.getString(0));
            } while (resultSet.moveToNext());
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
        return null;
    }

    @Override
    public String limitBy() {
        return null;
    }

    public List<String> getDataList() {
        return dataList;
    }
}
