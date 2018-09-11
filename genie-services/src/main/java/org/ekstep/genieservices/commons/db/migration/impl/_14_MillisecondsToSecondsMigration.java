package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.LearnerContentSummaryDetails;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.profile.db.model.LearnerSummaryEventsModel;
import org.ekstep.genieservices.profile.db.model.LearnerSummaryModel;

/**
 * Created on 16/7/18.
 * shriharsh
 */
public class _14_MillisecondsToSecondsMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 14;
    private static final int TARGET_DB_VERSION = 19;

    public _14_MillisecondsToSecondsMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        updateMillisecondsToSeconds(appContext);
    }

    private void updateMillisecondsToSeconds(AppContext appContext) {
        LearnerSummaryEventsModel learnerSummaryEventsModel = LearnerSummaryEventsModel.find(appContext.getDBSession(), "");
        if (learnerSummaryEventsModel != null) {
            for (LearnerSummaryModel l : learnerSummaryEventsModel.getAllLearnerSummaryModelList()) {
                LearnerContentSummaryDetails learnerContentSummaryDetails = new LearnerContentSummaryDetails();

                learnerContentSummaryDetails.setUid(l.getUid());
                learnerContentSummaryDetails.setContentId(l.getContentId());
                learnerContentSummaryDetails.setAvgts(l.getAvgts());
                learnerContentSummaryDetails.setSessions(l.getSessions());
                learnerContentSummaryDetails.setTotalts((double) Math.round(l.getTotalts() / 1000));
                learnerContentSummaryDetails.setTimespent(l.getTimespent());
                learnerContentSummaryDetails.setTimestamp(l.getTimestamp());
                learnerContentSummaryDetails.setHierarchyData(l.getHierarchyData());
                learnerContentSummaryDetails.setLastUpdated(l.getLastUpdated());

                LearnerSummaryModel learnerSummaryModel = LearnerSummaryModel.build(appContext.getDBSession(), learnerContentSummaryDetails);
                learnerSummaryModel.update();
            }
        }
    }
}
