package org.ekstep.genieservices.commons.db.model;

import org.ekstep.genieservices.commons.db.contract.NoSqlEntry;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 07/19/2018.
 *
 * @author swayangjit
 */
public class NoSqlModelListModel implements IReadable, ICleanable {

    private IDBSession mDBSession;
    private String filterCondition;

    private List<NoSqlModel> noSqlModelList;

    private NoSqlModelListModel(IDBSession mDBSession, String filterCondition) {
        this.mDBSession = mDBSession;
        this.filterCondition = filterCondition;
    }

    public static NoSqlModelListModel find(IDBSession dbSession) {
        NoSqlModelListModel noSqlModelListModel = new NoSqlModelListModel(dbSession, "");
        dbSession.read(noSqlModelListModel);
        if (noSqlModelListModel.getNoSqlModelList() == null) {
            return null;
        } else {
            return noSqlModelListModel;
        }
    }

    public static NoSqlModelListModel findWithCustomQuery(IDBSession dbSession, String query) {
        NoSqlModelListModel noSqlModelListModel = new NoSqlModelListModel(dbSession, "");
        dbSession.read(noSqlModelListModel, query);

        if (noSqlModelListModel.getNoSqlModelList() == null) {
            return null;
        } else {
            return noSqlModelListModel;
        }
    }

    public static NoSqlModelListModel findWithFilter(IDBSession dbSession, String filterCondition) {
        NoSqlModelListModel noSqlModelListModel = new NoSqlModelListModel(dbSession, filterCondition);
        dbSession.read(noSqlModelListModel);

        if (noSqlModelListModel.getNoSqlModelList() == null) {
            return null;
        } else {
            return noSqlModelListModel;
        }
    }

    public Void delete() {
        mDBSession.clean(this);
        return null;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            noSqlModelList = new ArrayList<>();

            do {
                NoSqlModel noSqlModel = NoSqlModel.build(mDBSession);

                noSqlModel.readWithoutMoving(resultSet);

                noSqlModelList.add(noSqlModel);
            } while (resultSet.moveToNext());
        }

        return this;
    }

    @Override
    public String getTableName() {
        return NoSqlEntry.TABLE_NAME;
    }

    @Override
    public void clean() {
        noSqlModelList = null;
    }

    @Override
    public String selectionToClean() {
        return filterCondition;
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, " order by %s desc", NoSqlEntry.COLUMN_NAME_KEY);
    }

    @Override
    public String filterForRead() {
        return filterCondition;
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "";
    }

    public List<NoSqlModel> getNoSqlModelList() {
        return noSqlModelList;
    }

}
