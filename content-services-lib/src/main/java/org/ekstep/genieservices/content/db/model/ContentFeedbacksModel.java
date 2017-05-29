package org.ekstep.genieservices.content.db.model;

import org.ekstep.genieservices.commons.db.contract.ContentFeedbackEntry;
import org.ekstep.genieservices.commons.db.core.ICleanable;
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
public class ContentFeedbacksModel implements IReadable, ICleanable {

    private IDBSession mDBSession;
    private String filterCondition;

    private List<ContentFeedbackModel> contentFeedbackModelList;

    private ContentFeedbacksModel(IDBSession dbSession, String filter) {
        this.mDBSession = dbSession;
        this.filterCondition = filter;
    }

    public static ContentFeedbacksModel findByUid(IDBSession dbSession, String uid) {
        String filter = String.format(Locale.US, " where %s = '%s' ", ContentFeedbackEntry.COLUMN_NAME_UID, uid);
        ContentFeedbacksModel contentFeedbacksModel = new ContentFeedbacksModel(dbSession, filter);

        dbSession.read(contentFeedbacksModel);

        if (contentFeedbacksModel.getContentFeedbackModelList() == null) {
            return null;
        } else {
            return contentFeedbacksModel;
        }
    }

    public static ContentFeedbacksModel findByContentIdentifier(IDBSession dbSession, String identifier) {
        String filter = String.format(Locale.US, " where %s = '%s' ", ContentFeedbackEntry.COLUMN_NAME_CONTENT_ID, identifier);
        ContentFeedbacksModel contentFeedbacksModel = new ContentFeedbacksModel(dbSession, filter);
        dbSession.read(contentFeedbacksModel);

        if (contentFeedbacksModel.getContentFeedbackModelList() == null) {
            return null;
        } else {
            return contentFeedbacksModel;
        }
    }

    public Void delete() {
        mDBSession.clean(this);
        return null;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            contentFeedbackModelList = new ArrayList<>();

            do {
                ContentFeedbackModel contentFeedback = ContentFeedbackModel.build();

                contentFeedback.readWithoutMoving(resultSet);

                contentFeedbackModelList.add(contentFeedback);
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

    @Override
    public void clean() {
        this.contentFeedbackModelList = null;
    }

    @Override
    public String selectionToClean() {
        return filterCondition;
    }

    @Override
    public String getTableName() {
        return ContentFeedbackEntry.TABLE_NAME;
    }


    public List<ContentFeedbackModel> getContentFeedbackModelList() {
        return contentFeedbackModelList;
    }
}
