package org.ekstep.genieservices.content.db.model;

import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 5/4/2017.
 *
 * @author anil
 */
public class ContentsModel implements IReadable {

    private IDBSession mDBSession;
    private String filterCondition;
    private boolean onlySize;
    private long totalSize;

    private List<ContentModel> contentModelList;

    private ContentsModel(IDBSession dbSession, String filter) {
        this.mDBSession = dbSession;
        this.filterCondition = filter;
    }

    private ContentsModel(IDBSession dbSession, boolean onlySize) {
        this.mDBSession = dbSession;
        this.onlySize = onlySize;
    }

    public static ContentsModel find(IDBSession dbSession, String filter) {
        ContentsModel contentsModel = new ContentsModel(dbSession, filter);
        dbSession.read(contentsModel);

        if (contentsModel.contentModelList == null) {
            return null;
        } else {
            return contentsModel;
        }
    }

    public static ContentsModel findWithCustomQuery(IDBSession dbSession, String query) {
        ContentsModel contentsModel = new ContentsModel(dbSession, null);
        dbSession.read(contentsModel, query);

        if (contentsModel.getContentModelList() == null) {
            return null;
        } else {
            return contentsModel;
        }
    }

    public static long totalSizeOnDevice(IDBSession dbSession, String query) {
        ContentsModel model = new ContentsModel(dbSession, true);
        dbSession.read(model, query);
        return model.totalSize;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            contentModelList = new ArrayList<>();
            if (onlySize) {
                totalSize = resultSet.getLong(0);
            } else {

                do {
                    ContentModel content = ContentModel.build(mDBSession);
                    content.readWithoutMoving(resultSet);

                    contentModelList.add(content);
                } while (resultSet.moveToNext());
            }
        }

        return this;
    }

    @Override
    public String getTableName() {
        return ContentEntry.TABLE_NAME;
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, " order by %s desc, %s desc", ContentEntry.COLUMN_NAME_LOCAL_LAST_UPDATED_ON, ContentEntry.COLUMN_NAME_SERVER_LAST_UPDATED_ON);
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

    public List<ContentModel> getContentModelList() {
        return contentModelList;
    }

}
