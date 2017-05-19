package org.ekstep.genieservices.content.db.model;

import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.StringUtil;

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

    private List<ContentModel> contentModelList;

    private ContentsModel(IDBSession dbSession, String filter) {
        this.mDBSession = dbSession;
        this.filterCondition = filter;
    }

    public static ContentsModel findAllContentsWithIdentifiers(IDBSession dbSession, List<String> identifiers) {
        String filter = String.format(Locale.US, " where %s in ('%s') ", ContentEntry.COLUMN_NAME_IDENTIFIER, StringUtil.join("','", identifiers));

        return find(dbSession, filter);
    }

    public static ContentsModel find(IDBSession dbSession, String filter) {
        ContentsModel contentsModel = new ContentsModel(dbSession, filter);

        dbSession.read(contentsModel);

        if (contentsModel.getContentModelList() == null) {
            return null;
        } else {
            return contentsModel;
        }
    }

    public static ContentsModel find(IDBSession dbSession, String filter, List<String> identifiers, String orderBy) {
        ContentsModel contentsModel = new ContentsModel(dbSession, filter);

        String query = String.format(Locale.US, "Select * from %s where %s in ('%s') %s %s", contentsModel.getTableName(), ContentEntry.COLUMN_NAME_IDENTIFIER, StringUtil.join("','", identifiers), filter, orderBy);
        dbSession.read(contentsModel, query);

        if (contentsModel.getContentModelList() == null) {
            return null;
        } else {
            return contentsModel;
        }
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            contentModelList = new ArrayList<>();

            do {
                ContentModel content = ContentModel.build();

                content.readWithoutMoving(resultSet);

                contentModelList.add(content);
            } while (resultSet.moveToNext());
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
