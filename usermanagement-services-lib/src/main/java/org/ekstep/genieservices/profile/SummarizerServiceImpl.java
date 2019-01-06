package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ISummarizerService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.CorrelationData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentSummary;
import org.ekstep.genieservices.commons.bean.LearnerContentSummaryDetails;
import org.ekstep.genieservices.commons.bean.SummaryRequest;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.db.model.LearnerAssessmentDetailsModel;
import org.ekstep.genieservices.profile.db.model.LearnerAssessmentSummaryModel;
import org.ekstep.genieservices.profile.db.model.LearnerSummaryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class is the implementation of {@link ISummarizerService}
 */
public class SummarizerServiceImpl extends BaseService implements ISummarizerService {

    private static final String TAG = SummarizerServiceImpl.class.getSimpleName();

    public SummarizerServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<List<LearnerAssessmentSummary>> getSummary(SummaryRequest summaryRequest) {
        LearnerAssessmentSummaryModel learnerAssessmentSummaryModel = null;
        GenieResponse<List<LearnerAssessmentSummary>> response;
        String methodName = "getSummary@LearnerAssessmentsServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        if (summaryRequest.getUids() != null) {
            learnerAssessmentSummaryModel = LearnerAssessmentSummaryModel.findChildProgressSummary(mAppContext.getDBSession(), getStringWithQuoteList(summaryRequest.getUids()));
        } else if (summaryRequest.getContentId() != null) {
            learnerAssessmentSummaryModel = LearnerAssessmentSummaryModel.findContentProgressSummary(mAppContext.getDBSession(), summaryRequest.getContentId());
        }

        //if the assembleMap list size is 0 then their was some error
        if (learnerAssessmentSummaryModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROCESSING_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_SUMMARY, TAG);
            return response;
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(learnerAssessmentSummaryModel.getAssessmentMap());
        return response;
    }

    @Override
    public GenieResponse<List<LearnerAssessmentDetails>> getLearnerAssessmentDetails(SummaryRequest summaryRequest) {
        GenieResponse<List<LearnerAssessmentDetails>> response;
        String methodName = "getLearnerAssessmentDetails@LearnerAssessmentsServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        List<String> quotedUIds = getStringWithQuoteList(summaryRequest.getUids());

        String contentId = summaryRequest.getContentId();

        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = LearnerAssessmentDetailsModel.findDetailReport(mAppContext.getDBSession(), quotedUIds, contentId, LearnerAssessmentDetailsModel.FOR_SUMMARIZER);
        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        if (learnerAssessmentDetailsModel == null) {
            response.setResult(new ArrayList<LearnerAssessmentDetails>());
        } else {
            response.setResult(learnerAssessmentDetailsModel.getAllAssessments());
        }

        return response;
    }

    @Override
    public GenieResponse<List<Map<String, Object>>> getReportsByUser(SummaryRequest summaryRequest) {
        GenieResponse<List<Map<String, Object>>> response;
        String methodName = "getReportsByUser@LearnerAssessmentsServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

        if (StringUtil.isNullOrEmpty(summaryRequest.getContentId()) || CollectionUtil.isNullOrEmpty(summaryRequest.getUids())) {
            response.setResult(new ArrayList<Map<String, Object>>());
            return response;
        }

        List<String> quotedUIds = getStringWithQuoteList(summaryRequest.getUids());

        LearnerAssessmentSummaryModel learnerAssessmentSummaryModel = LearnerAssessmentSummaryModel.findReportsSummary(mAppContext.getDBSession(),
                quotedUIds, summaryRequest.getContentId(), true);

        if (learnerAssessmentSummaryModel == null) {
            response.setResult(new ArrayList<Map<String, Object>>());
        } else {
            response.setResult(learnerAssessmentSummaryModel.getReportsMap());
        }

        return response;

    }

    @Override
    public GenieResponse<List<Map<String, Object>>> getDetailsPerQuestion(SummaryRequest summaryRequest) {
        GenieResponse<List<Map<String, Object>>> response;
        String methodName = "getDetailsPerQuestion@LearnerAssessmentsServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

        if (StringUtil.isNullOrEmpty(summaryRequest.getContentId()) || CollectionUtil.isNullOrEmpty(summaryRequest.getUids())
                || StringUtil.isNullOrEmpty(summaryRequest.getQuestionId())) {
            response.setResult(new ArrayList<Map<String, Object>>());
            return response;
        }

        List<String> quotedUIds = getStringWithQuoteList(summaryRequest.getUids());

        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = LearnerAssessmentDetailsModel.findQuestionDetails(mAppContext.getDBSession(),
                quotedUIds, summaryRequest.getContentId(), summaryRequest.getQuestionId(), LearnerAssessmentDetailsModel.FOR_QUESTION_DETAILS);

        if (learnerAssessmentDetailsModel == null) {
            response.setResult(new ArrayList<Map<String, Object>>());
        } else {
            response.setResult(learnerAssessmentDetailsModel.getReportsMapList());
        }

        return response;
    }

    @Override
    public GenieResponse<List<Map<String, Object>>> getReportByQuestions(SummaryRequest summaryRequest) {
        GenieResponse<List<Map<String, Object>>> response;
        String methodName = "getReportByQuestions@LearnerAssessmentsServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

        if (StringUtil.isNullOrEmpty(summaryRequest.getContentId()) || CollectionUtil.isNullOrEmpty(summaryRequest.getUids())) {
            response.setResult(new ArrayList<Map<String, Object>>());
            return response;
        }

        List<String> quotedUIds = getStringWithQuoteList(summaryRequest.getUids());

        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = LearnerAssessmentDetailsModel.findQuestionsReportSummary(mAppContext.getDBSession(),
                summaryRequest.getContentId(), quotedUIds, LearnerAssessmentDetailsModel.FOR_QUESTIONS_REPORT);

        if (learnerAssessmentDetailsModel == null) {
            response.setResult(new ArrayList<Map<String, Object>>());
        } else {
            LearnerAssessmentDetailsModel learnerAssessmentDetailsModel1 = LearnerAssessmentDetailsModel.findForQuestionAccuracy(mAppContext.getDBSession(),
                    summaryRequest.getContentId(), quotedUIds, LearnerAssessmentDetailsModel.FOR_ACCURACY_REPORT);

            List<Map<String, Object>> questionReportMapList = learnerAssessmentDetailsModel.getReportsMapList();

            if (learnerAssessmentDetailsModel1 == null) {
                response.setResult(questionReportMapList);
            } else {
                Map<Double, Integer> accuracyReport = learnerAssessmentDetailsModel1.getAccuracyReportMap();

                if (accuracyReport == null) {
                    response.setResult(questionReportMapList);
                } else {

                    for (Map<String, Object> questionReport : questionReportMapList) {
                        double qIndex = (double) questionReport.get(LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX);

                        if (accuracyReport.containsKey(qIndex)) {
                            questionReport.put("correct_users_count", accuracyReport.get(qIndex));
                        }
                    }

                    response.setResult(questionReportMapList);
                }
            }
        }

        return response;
    }

    private List<String> getStringWithQuoteList(List<String> collection) {
        ArrayList<String> stringWithCommaList = new ArrayList<>();

        if (!CollectionUtil.isNullOrEmpty(collection)) {
            for (String s : collection) {
                stringWithCommaList.add("'" + s + "'");

            }
        }

        return stringWithCommaList;
    }

    private String getFilterForLearnerAssessmentDetails(String qid, List<String> uids, String contentId, String hierarchyData) {
        String isQid = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_QID, qid);
        String isUid = String.format(Locale.US, "%s IN (%s)", LearnerAssessmentsEntry.COLUMN_NAME_UID, StringUtil.join(",", getStringWithQuoteList(uids)));
        String isContentId = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID, contentId);
        String isHData = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA, hierarchyData == null ? "" : hierarchyData);

        String filter;
        if (StringUtil.isNullOrEmpty(qid)) {
            filter = String.format(Locale.US, "where %s AND %s AND %s", isUid, isContentId, isHData);
        } else {
            filter = String.format(Locale.US, "where %s AND %s AND %s AND %s", isUid, isContentId, isHData, isQid);
        }
        return filter;
    }

    private String getFilterForLearnerAssessmentDetails(String qid, String uid, String contentId, String hierarchyData) {
        String isQid = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_QID, qid);
        String isUid = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_UID, uid);
        String isContentId = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID, contentId);
        String isHData = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA, hierarchyData == null ? "" : hierarchyData);

        String filter;
        if (StringUtil.isNullOrEmpty(qid)) {
            filter = String.format(Locale.US, "where %s AND %s AND %s", isUid, isContentId, isHData);
        } else {
            filter = String.format(Locale.US, "where %s AND %s AND %s AND %s", isUid, isContentId, isHData, isQid);
        }
        return filter;
    }


    @Override
    public GenieResponse<Void> saveLearnerAssessmentDetails(Telemetry telemetry) {
        GenieResponse<Void> response;
        String methodName = "saveLearnerAssessmentDetails@LearnerAssessmentsServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");
        LearnerAssessmentDetails learnerAssessmentDetails = mapTelemtryToLearnerAssessmentData(telemetry);
        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = LearnerAssessmentDetailsModel.build(mAppContext.getDBSession(), learnerAssessmentDetails);

        String filter = getFilterForLearnerAssessmentDetails(learnerAssessmentDetails.getQid(), learnerAssessmentDetails.getUid(), learnerAssessmentDetails.getContentId(), learnerAssessmentDetails.getHierarchyData());

        //check if the learner assessment already exists
        if (LearnerAssessmentDetailsModel.find(mAppContext.getDBSession(), filter) == null) {
            learnerAssessmentDetailsModel.save();
        } else {
            learnerAssessmentDetailsModel.update();
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        return response;
    }

    @Override
    public GenieResponse<Void> saveLearnerContentSummaryDetails(Telemetry telemetry) {
        GenieResponse<Void> response;
        String methodName = "saveLearnerContentSummaryDetails@LearnerAssessmentsServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");
        LearnerContentSummaryDetails learnerContentSummaryDetails = mapTelemtryToLearnerContentSummaryDeatils(telemetry);
        LearnerSummaryModel learnerSummaryModel;

        LearnerSummaryModel learnerSummaryModelInDB = LearnerSummaryModel.find(mAppContext.getDBSession(), learnerContentSummaryDetails.getUid(), learnerContentSummaryDetails.getContentId(),
                learnerContentSummaryDetails.getHierarchyData() == null ? "" : learnerContentSummaryDetails.getHierarchyData());

        //Check if the learner content summary already exists
        if (learnerSummaryModelInDB == null) {
            learnerContentSummaryDetails.setAvgts(learnerContentSummaryDetails.getTimespent());
            learnerContentSummaryDetails.setSessions(1);
            learnerContentSummaryDetails.setTotalts(learnerContentSummaryDetails.getTimespent());
            learnerContentSummaryDetails.setLastUpdated(learnerContentSummaryDetails.getTimestamp());
            //save with new details
            learnerSummaryModel = LearnerSummaryModel.build(mAppContext.getDBSession(), learnerContentSummaryDetails);
            learnerSummaryModel.save();

        } else {
            if (learnerContentSummaryDetails.getTimespent() != null) {
                learnerContentSummaryDetails.setSessions(learnerSummaryModelInDB.getSessions() + 1);
                //NOTE - We do not add the total time spent from previously stored value because we need to update only the latest value
                //but not the cummulative added value
                learnerContentSummaryDetails.setTotalts(learnerContentSummaryDetails.getTimespent());
                learnerContentSummaryDetails.setAvgts(learnerSummaryModelInDB.getTotalts() / learnerSummaryModelInDB.getSessions());
                learnerContentSummaryDetails.setLastUpdated(learnerSummaryModelInDB.getTimestamp());
                //update with new details
                learnerSummaryModel = LearnerSummaryModel.build(mAppContext.getDBSession(), learnerContentSummaryDetails);
                learnerSummaryModel.update();
            }
        }

        return null;
    }

    private LearnerContentSummaryDetails mapTelemtryToLearnerContentSummaryDeatils(Telemetry telemetry) {
        LearnerContentSummaryDetails learnerContentSummaryDetails = new LearnerContentSummaryDetails();
        learnerContentSummaryDetails.setUid(telemetry.getActor().getId());
        learnerContentSummaryDetails.setContentId(telemetry.getObject().getId());
        Map<String, Object> eData = telemetry.getEdata();
        learnerContentSummaryDetails.setTimespent((Double) eData.get("duration"));

        learnerContentSummaryDetails.setTimestamp(telemetry.getEts());

        List<CorrelationData> cData = telemetry.getContext().getCdata();
        if (cData != null) {
            for (CorrelationData eachCdataValue : cData) {
                // TODO: 10/10/2017 - Relook following check, it should be mymeType check instead of specific contentType check
                if (eachCdataValue.getType().equalsIgnoreCase("Collection") || eachCdataValue.getType().equalsIgnoreCase("TextBook")) {
                    learnerContentSummaryDetails.setHierarchyData(eachCdataValue.getId());
                    break;
                }
            }
        }

        return learnerContentSummaryDetails;
    }

    private LearnerAssessmentDetails mapTelemtryToLearnerAssessmentData(Telemetry telemetry) {
        LearnerAssessmentDetails learnerAssessmentDetails = new LearnerAssessmentDetails();
        learnerAssessmentDetails.setUid(telemetry.getActor().getId());
        learnerAssessmentDetails.setContentId(telemetry.getObject().getId());
        Map<String, Object> eData = telemetry.getEdata();
        Map<String, Object> question = (Map<String, Object>) eData.get("item");
        learnerAssessmentDetails.setQid((String) question.get("id"));
        learnerAssessmentDetails.setQindex((Double) eData.get("index"));
        String pass = (String) eData.get("pass");
        learnerAssessmentDetails.setCorrect(("Yes".equalsIgnoreCase(pass) ? 1 : 0));
        learnerAssessmentDetails.setScore((Double) eData.get("score"));
        learnerAssessmentDetails.setTimespent((Double) eData.get("duration"));

        learnerAssessmentDetails.setTimestamp((Long) telemetry.getEts());
        learnerAssessmentDetails.setRes(GsonUtil.toJson(eData.get("resvalues")));
        learnerAssessmentDetails.setQdesc((String) question.get("desc"));
        learnerAssessmentDetails.setQtitle((String) question.get("title"));
        learnerAssessmentDetails.setMaxScore((Double) question.get("maxscore"));

        List<CorrelationData> cData = telemetry.getContext().getCdata();
        if (cData != null) {
            for (CorrelationData eachCdataValue : cData) {
                // TODO: 10/10/2017 - Relook following check, it should be mymeType check instead of specific contentType check
                if (eachCdataValue.getType().equalsIgnoreCase("Collection") || eachCdataValue.getType().equalsIgnoreCase("TextBook")) {
                    learnerAssessmentDetails.setHierarchyData(eachCdataValue.getId());
                    break;
                }
            }
        }
        return learnerAssessmentDetails;
    }

    /**
     * This method deletes all the previous details related to contentId and uid, whenever the new START event and new ASSESS is received
     *
     * @param uid
     * @param contentId
     */
    public void deletePreviousAssessmentDetails(String uid, String contentId) {

        LearnerSummaryModel learnerSummaryModel = LearnerSummaryModel.find(mAppContext.getDBSession(), uid, contentId, "");

        //filter for assessment details
        String filter = String.format(Locale.US, " where %s = '%s' AND %s = '%s'",
                LearnerAssessmentsEntry.COLUMN_NAME_UID, uid, LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID, contentId);

        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = LearnerAssessmentDetailsModel.find(mAppContext.getDBSession(), filter, uid, contentId);

        if (learnerSummaryModel != null) {
            learnerSummaryModel.delete();
        }

        if (learnerAssessmentDetailsModel != null) {
            learnerAssessmentDetailsModel.delete();
        }


    }
}
