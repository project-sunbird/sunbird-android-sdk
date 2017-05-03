package org.ekstep.genieservices.content.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.content.db.contract.ContentAccessEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 5/3/2017.
 *
 * @author anil
 */
public class ContentAccessesModel implements IReadable, ICleanable {

    private AppContext appContext;
    private String filterCondition;

    private List<ContentAccessModel> contentAccessModelList;

    private ContentAccessesModel(AppContext appContext, String filter) {
        this.appContext = appContext;
        this.filterCondition = filter;
    }

    public static ContentAccessesModel find(AppContext appContext, String filter) {
        ContentAccessesModel contentAccessesModel = new ContentAccessesModel(appContext, filter);
        appContext.getDBSession().read(contentAccessesModel);
        return contentAccessesModel;
    }

    public static ContentAccessesModel findByUid(AppContext appContext, String uid) {
        String filter = String.format(Locale.US, " where %s = '%s' ", ContentAccessEntry.COLUMN_NAME_UID, uid);
        ContentAccessesModel contentAccessesModel = new ContentAccessesModel(appContext, filter);

        appContext.getDBSession().read(contentAccessesModel);

        if (contentAccessesModel.getContentAccessModelList() == null) {
            return null;
        } else {
            return contentAccessesModel;
        }
    }

    public static ContentAccessesModel findByContentIdentifier(AppContext appContext, String identifier) {
        String filter = String.format(Locale.US, " where %s = '%s' ", ContentAccessEntry.COLUMN_NAME_IDENTIFIER, identifier);
        ContentAccessesModel contentAccessesModel = new ContentAccessesModel(appContext, filter);
        appContext.getDBSession().read(contentAccessesModel);

        if (contentAccessesModel.getContentAccessModelList() == null) {
            return null;
        } else {
            return contentAccessesModel;
        }
    }

    public Void delete() {
        appContext.getDBSession().clean(this);
        return null;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            contentAccessModelList = new ArrayList<>();

            do {
                ContentAccessModel contentAccess = ContentAccessModel.buildContentAccess(appContext);

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
