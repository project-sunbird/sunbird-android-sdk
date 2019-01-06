package org.ekstep.genieservices.content.db.model;

import org.ekstep.genieservices.commons.db.contract.ContentMarkerEntry;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContentMarkersModel implements IReadable, ICleanable {

    private IDBSession mDBSession;
    private String filterCondition;

    private List<ContentMarkerModel> contentMarkerModelList;

    private ContentMarkersModel(IDBSession dbSession, String filter) {
        this.mDBSession = dbSession;
        this.filterCondition = filter;
    }

    public static ContentMarkersModel find(IDBSession dbSession, String filter) {
        ContentMarkersModel contentMarkersModel = new ContentMarkersModel(dbSession, filter);
        dbSession.read(contentMarkersModel);

        if (contentMarkersModel.getContentMarkerModelList() == null) {
            return null;
        } else {
            return contentMarkersModel;
        }
    }

    public static ContentMarkersModel findWithCustomQuery(IDBSession dbSession, String query) {
        ContentMarkersModel contentMarkersModel = new ContentMarkersModel(dbSession, null);
        dbSession.read(contentMarkersModel, query);

        if (contentMarkersModel.getContentMarkerModelList() == null) {
            return null;
        } else {
            return contentMarkersModel;
        }
    }

    public Void delete() {
        mDBSession.clean(this);
        return null;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            contentMarkerModelList = new ArrayList<>();

            do {
                ContentMarkerModel contentMarkerModel = ContentMarkerModel.build(mDBSession);
                contentMarkerModel.readWithoutMoving(resultSet);

                contentMarkerModelList.add(contentMarkerModel);
            } while (resultSet.moveToNext());

        }

        return this;
    }

    @Override
    public String getTableName() {
        return ContentMarkerEntry.TABLE_NAME;
    }

    @Override
    public void clean() {
        contentMarkerModelList = null;
    }

    @Override
    public String selectionToClean() {
        return filterCondition;
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, " order by %s desc", ContentMarkerEntry.COLUMN_NAME_EPOCH_TIMESTAMP);
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

    public List<ContentMarkerModel> getContentMarkerModelList() {
        return contentMarkerModelList;
    }
}
