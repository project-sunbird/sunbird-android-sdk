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
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.importexport.bean.ImportProfileContext;
import org.ekstep.genieservices.profile.db.model.LearnerAssessmentDetailsModel;
import org.ekstep.genieservices.profile.db.model.LearnerSummaryEventsModel;
import org.ekstep.genieservices.profile.db.model.LearnerSummaryModel;
import org.ekstep.genieservices.profile.db.model.UserModel;
import org.ekstep.genieservices.profile.db.model.UsersModel;

import java.util.ArrayList;
import java.util.List;
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
        IDBSession externalDBSession = appContext.getExternalDBSession(importContext.getSourceDBFilePath());

        //check table exist
        if (isTableExist(externalDBSession, LearnerAssessmentsEntry.TABLE_NAME) &&
                isTableExist(externalDBSession, LearnerSummaryEntry.TABLE_NAME)) {

            // TODO: 27/12/17 Todo Remove this segment after subsequent release
            ///////////////////////////////////////////////////////////////////
            // Read from imported DB
            UsersModel usersModel = UsersModel.findAll(externalDBSession);

            if (usersModel != null) {
                List<String> userIds = new ArrayList<>();
                for (UserModel userModel : usersModel.getUserModelList()) {
                    userIds.add(userModel.getUid());
                }

                if (!CollectionUtil.isNullOrEmpty(userIds)) {
                    deleteUnwantedProfileSummary(externalDBSession, userIds);
                }
            }
            /////////////////////////////////////////////////////////////////


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

    private void deleteUnwantedProfileSummary(IDBSession dbSession, final List<String> userIds) {
        dbSession.executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                String commaSeparatedUids = "'" + StringUtil.join("','", userIds) + "'";
                String delLearnerAssesmentQuery = "DELETE FROM " + LearnerAssessmentsEntry.TABLE_NAME + " WHERE " + LearnerAssessmentsEntry.COLUMN_NAME_UID + " NOT IN(" + commaSeparatedUids + ")";
                String delLearnerSummaryQuery = "DELETE FROM " + LearnerSummaryEntry.TABLE_NAME + " WHERE " + LearnerAssessmentsEntry.COLUMN_NAME_UID + " NOT IN(" + commaSeparatedUids + ")";

                dbSession.execute(delLearnerAssesmentQuery);
                dbSession.execute(delLearnerSummaryQuery);
                return null;
            }
        });
    }
}
