package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 5/3/2017.
 *
 * @author anil
 */
public class ContentAccessesModel implements IReadable, ICleanable {

    private IDBSession mDBSession;
    private String filterCondition;

    private List<ContentAccessModel> contentAccessModelList;

    private ContentAccessesModel(IDBSession dbSession, String filter) {
        this.mDBSession = dbSession;
        this.filterCondition = filter;
    }

    public static ContentAccessesModel find(IDBSession dbSession, String filter) {
        ContentAccessesModel contentAccessesModel = new ContentAccessesModel(dbSession, filter);
        dbSession.read(contentAccessesModel);
        return contentAccessesModel;
    }

    public static ContentAccessesModel findByUid(IDBSession dbSession, String uid) {
        String filter = String.format(Locale.US, " where %s = '%s' ", ContentAccessEntry.COLUMN_NAME_UID, uid);
        ContentAccessesModel contentAccessesModel = new ContentAccessesModel(dbSession, filter);

        dbSession.read(contentAccessesModel);

        if (contentAccessesModel.getContentAccessModelList() == null) {
            return null;
        } else {
            return contentAccessesModel;
        }
    }

    public static ContentAccessesModel findByContentIdentifier(IDBSession dbSession, String identifier) {
        String filter = String.format(Locale.US, " where %s = '%s' ", ContentAccessEntry.COLUMN_NAME_IDENTIFIER, identifier);
        ContentAccessesModel contentAccessesModel = new ContentAccessesModel(dbSession, filter);
        dbSession.read(contentAccessesModel);

        if (contentAccessesModel.getContentAccessModelList() == null) {
            return null;
        } else {
            return contentAccessesModel;
        }
    }

    public Void delete() {
        mDBSession.clean(this);
        return null;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            contentAccessModelList = new ArrayList<>();

            do {
                ContentAccessModel contentAccess = ContentAccessModel.buildContentAccess(mDBSession);

                contentAccess.readWithoutMoving(resultSet);

                contentAccessModelList.add(contentAccess);
            } while (resultSet.moveToNext());
        }

        return this;
    }

    @Override
    public String getTableName() {
        return ContentAccessEntry.TABLE_NAME;
    }

    @Override
    public void clean() {

    }

    @Override
    public String selectionToClean() {
        return filterCondition;
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, " order by %s desc", ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP);
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

    public List<ContentAccessModel> getContentAccessModelList() {
        return contentAccessModelList;
    }

}
