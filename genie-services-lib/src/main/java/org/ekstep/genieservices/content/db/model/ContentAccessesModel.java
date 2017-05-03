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

    private String uid;
    private String identifier;

    private List<ContentAccessModel> contentAccessModelList;

    private ContentAccessesModel(AppContext appContext, String filter) {
        this.appContext = appContext;
        this.filterCondition = filter;
    }

    private ContentAccessesModel(AppContext appContext, String uid, String identifier) {
        this.appContext = appContext;
        this.uid = uid;
        this.identifier = identifier;
    }

    public static ContentAccessesModel find(AppContext appContext, String filter) {
        ContentAccessesModel contentAccessesModel = new ContentAccessesModel(appContext, filter);
        appContext.getDBSession().read(contentAccessesModel);
        return contentAccessesModel;
    }

    public static void deleteByUid(AppContext appContext, String uid) {
        ContentAccessesModel contentAccessesModel = new ContentAccessesModel(appContext, uid, null);
        appContext.getDBSession().clean(contentAccessesModel);
    }

    public static void deleteByContentIdentifier(AppContext appContext, String identifier) {
        ContentAccessesModel contentAccessesModel = new ContentAccessesModel(appContext, null, identifier);
        appContext.getDBSession().clean(contentAccessesModel);
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
        if (uid != null) {   // Delete all row by uid
            return String.format(Locale.US, "where %s = '%s' ", ContentAccessEntry.COLUMN_NAME_UID, uid);
        } else {    // Delete all row by content identifier
            return String.format(Locale.US, "where %s = '%s' ", ContentAccessEntry.COLUMN_NAME_IDENTIFIER, identifier);
        }
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
