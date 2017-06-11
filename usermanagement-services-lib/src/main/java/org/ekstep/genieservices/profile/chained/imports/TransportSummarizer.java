package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.db.contract.LearnerContentSummaryEntry;
import org.ekstep.genieservices.commons.db.model.CustomReaderModel;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.profile.db.model.LearnerAssessmentDetailsModel;

import java.util.Locale;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class TransportSummarizer implements IChainable {

    private static final String TAG = TransportSummarizer.class.getSimpleName();
    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        //check table exist
        if (isTableExist(importContext.getDBSession(), LearnerAssessmentsEntry.TABLE_NAME) &&
                isTableExist(importContext.getDBSession(), LearnerContentSummaryEntry.TABLE_NAME)) {

            // Read the learner assessment data from imported DB and insert into GS DB.
            LearnerAssessmentDetailsModel importedLearnerAssessmentDetailsModel = LearnerAssessmentDetailsModel.find(importContext.getDBSession(), "");
            if (importedLearnerAssessmentDetailsModel != null) {
                for (LearnerAssessmentDetails learnerAssessmentDetails : importedLearnerAssessmentDetailsModel.getAllAssessments()) {
                    LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = LearnerAssessmentDetailsModel.build(appContext.getDBSession(), learnerAssessmentDetails);
                    learnerAssessmentDetailsModel.save();
                }
            }

            // Read the learner content summary data from imported DB and insert into GS DB.
            // TODO: 6/9/2017
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import profile failed.", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }

    private boolean isTableExist(IDBSession dbSession, String tableName) {
        String tableQuery = String.format(Locale.US, "select name from sqlite_master where type='%s' AND name='%s'", "table", tableName);
        CustomReaderModel customReaderModel = CustomReaderModel.find(dbSession, tableQuery);
        return customReaderModel != null;
    }
}
