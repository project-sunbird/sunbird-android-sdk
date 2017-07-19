package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.bean.LearnerContentSummaryDetails;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.db.contract.LearnerSummaryEntry;
import org.ekstep.genieservices.commons.db.model.CustomReaderModel;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.profile.bean.ImportProfileContext;
import org.ekstep.genieservices.profile.db.model.LearnerAssessmentDetailsModel;
import org.ekstep.genieservices.profile.db.model.LearnerSummaryEventsModel;
import org.ekstep.genieservices.profile.db.model.LearnerSummaryModel;

import java.util.Locale;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class TransportSummarizer implements IChainable<ProfileImportResponse, ImportProfileContext> {

    private static final String TAG = TransportSummarizer.class.getSimpleName();
    private IChainable<ProfileImportResponse, ImportProfileContext> nextLink;

    @Override
    public GenieResponse<ProfileImportResponse> execute(AppContext appContext, ImportProfileContext importContext) {
        IDBSession externalDBSession = importContext.getDataSource().getReadOnlyDataSource(importContext.getSourceFilePath());

        //check table exist
        if (isTableExist(externalDBSession, LearnerAssessmentsEntry.TABLE_NAME) &&
                isTableExist(externalDBSession, LearnerSummaryEntry.TABLE_NAME)) {

            // Read the learner assessment data from imported DB and insert into GS DB.
            LearnerAssessmentDetailsModel importedLearnerAssessmentDetailsModel = LearnerAssessmentDetailsModel.find(externalDBSession, "");
            if (importedLearnerAssessmentDetailsModel != null) {
                for (LearnerAssessmentDetails learnerAssessmentDetails : importedLearnerAssessmentDetailsModel.getAllAssessments()) {
                    LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = LearnerAssessmentDetailsModel.build(appContext.getDBSession(), learnerAssessmentDetails);
                    learnerAssessmentDetailsModel.save();
                }
            }

            // Read the learner content summary data from imported DB and insert into GS DB.
            LearnerSummaryEventsModel learnerSummaryEventsModel = LearnerSummaryEventsModel.find(externalDBSession, "");
            if (learnerSummaryEventsModel != null) {
                for (LearnerSummaryModel l : learnerSummaryEventsModel.getAllLearnerSummaryModelList()) {
                    LearnerContentSummaryDetails learnerContentSummaryDetails = new LearnerContentSummaryDetails();
                    learnerContentSummaryDetails.setUid(l.getUid());
                    learnerContentSummaryDetails.setContentId(l.getContentId());
                    learnerContentSummaryDetails.setAvgts(l.getAvgts());
                    learnerContentSummaryDetails.setSessions(l.getSessions());
                    learnerContentSummaryDetails.setTotalts(l.getTotalts());
                    learnerContentSummaryDetails.setTimespent(l.getTimespent());
                    learnerContentSummaryDetails.setTimestamp(l.getTimestamp());
                    learnerContentSummaryDetails.setHierarchyData(l.getHierarchyData());
                    learnerContentSummaryDetails.setLastUpdated(l.getLastUpdated());

                    LearnerSummaryModel learnerSummaryModel = LearnerSummaryModel.build(appContext.getDBSession(), learnerContentSummaryDetails);
                    learnerSummaryModel.save();
                }
            }
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import profile failed.", TAG);
        }
    }

    @Override
    public IChainable<ProfileImportResponse, ImportProfileContext> then(IChainable<ProfileImportResponse, ImportProfileContext> link) {
        nextLink = link;
        return link;
    }

    private boolean isTableExist(IDBSession dbSession, String tableName) {
        String tableQuery = String.format(Locale.US, "select name from sqlite_master where type='%s' AND name='%s'", "table", tableName);
        CustomReaderModel customReaderModel = CustomReaderModel.find(dbSession, tableQuery);
        return customReaderModel != null;
    }
}
