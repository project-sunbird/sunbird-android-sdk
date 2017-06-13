package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.db.contract.LearnerSummaryEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 12/6/17.
 * shriharsh
 */

public class LearnerSummaryEventsModel implements IReadable {
    private List<LearnerSummaryModel> learnerSummaryModelList;
    private IDBSession dbSession;
    private String filterCondition;


    private LearnerSummaryEventsModel(IDBSession dbSession, String filterCondition) {
        this.dbSession = dbSession;
        this.filterCondition = filterCondition;
    }

    public static LearnerSummaryEventsModel find(IDBSession dbSession, String filter) {
        LearnerSummaryEventsModel model = new LearnerSummaryEventsModel(dbSession, filter);
        dbSession.read(model);

        if (model.getAllLearnerSummaryModelList() == null) {
            return null;
        } else {
            return model;
        }
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            learnerSummaryModelList = new ArrayList<>();

            do {
                LearnerSummaryModel learnerSummaryModel = LearnerSummaryModel.build(dbSession);
                learnerSummaryModel.readWithoutMoving(cursor);
                learnerSummaryModelList.add(learnerSummaryModel);
            } while (cursor.moveToNext());
        }
        return this;
    }

    @Override
    public String getTableName() {
        return LearnerSummaryEntry.TABLE_NAME;
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return "";
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "";
    }

    public List<LearnerSummaryModel> getAllLearnerSummaryModelList() {
        return learnerSummaryModelList;
    }
}
