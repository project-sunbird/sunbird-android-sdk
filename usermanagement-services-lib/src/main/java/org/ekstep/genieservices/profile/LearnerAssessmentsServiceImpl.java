package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ILearnerAssessmentsService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentData;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentSummaryResponse;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.profile.db.model.LearnerAssessmentSummaryModel;
import org.ekstep.genieservices.profile.db.model.LearnerAssessmentsModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 4/6/17.
 * shriharsh
 */

public class LearnerAssessmentsServiceImpl extends BaseService implements ILearnerAssessmentsService {


    private static final String TAG = LearnerAssessmentsServiceImpl.class.getSimpleName();

    public LearnerAssessmentsServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<List<LearnerAssessmentSummaryResponse>> getChildSummary(String uid) {
        GenieResponse<List<LearnerAssessmentSummaryResponse>> response;
        String methodName = "getChildSummary@LearnerAssessmentsServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "2");
        LearnerAssessmentSummaryModel learnerAssessmentSummaryModel = LearnerAssessmentSummaryModel.findChildProgressSummary(mAppContext.getDBSession(), uid);
        //if the assembleMap list size is 0 then their was some error
        if (learnerAssessmentSummaryModel.getAssessmentMap().size() == 0) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROCESSING_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_CHILD_SUMMARY, TAG);
            return response;
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(learnerAssessmentSummaryModel.getAssessmentMap());
        return response;
    }

    @Override
    public GenieResponse<List<LearnerAssessmentSummaryResponse>> getContentSummary(String contentId) {
        GenieResponse<List<LearnerAssessmentSummaryResponse>> response;
        String methodName = "getContentSummary@LearnerAssessmentsServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "2");
        LearnerAssessmentSummaryModel learnerAssessmentSummaryModel = LearnerAssessmentSummaryModel.findContentProgressSummary(mAppContext.getDBSession(), contentId);
        //if the assembleMap list size is 0 then their was some error
        if (learnerAssessmentSummaryModel.getAssessmentMap().size() == 0) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROCESSING_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_CONTENT_SUMMARY, TAG);
            return response;
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(learnerAssessmentSummaryModel.getAssessmentMap());
        return response;

    }

    @Override
    public GenieResponse<List<LearnerAssessmentData>> getLearnerAssessmentSummary(String uid, String contentId) {
        GenieResponse<List<LearnerAssessmentData>> response;
        String methodName = "getLearnerAssessmentSummary@LearnerAssessmentsServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "2");
        LearnerAssessmentsModel learnerAssessmentsModel = LearnerAssessmentsModel.findAssessmentById(mAppContext.getDBSession(), uid, contentId);
        if (learnerAssessmentsModel.getAllAssesments().size() == 0) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROCESSING_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_CONTENT_SUMMARY, TAG);
            return response;
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(learnerAssessmentsModel.getAllAssesments());
        return response;
    }

    @Override
    public GenieResponse<Void> saveLearnerAssessment(Telemetry telemetry) {
        GenieResponse<Void> response;
        String methodName = "saveLearnerAssessment@LearnerAssessmentsServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "2");
        LearnerAssessmentData learnerAssessmentData = mapTelemtryToLearnerAssessmentData(telemetry);
        LearnerAssessmentsModel learnerAssessmentsModel = LearnerAssessmentsModel.build(mAppContext.getDBSession(), learnerAssessmentData);
        learnerAssessmentsModel.save();

        if (learnerAssessmentsModel.getInsertedId() == -1) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROCESSING_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_SAVE_LEARNER_ASSESSMENT, TAG);
            return response;
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        return response;
    }

    private LearnerAssessmentData mapTelemtryToLearnerAssessmentData(Telemetry telemetry) {
        LearnerAssessmentData learnerAssessmentData = new LearnerAssessmentData();
        learnerAssessmentData.setUid(telemetry.getUid());
        learnerAssessmentData.setContentId(telemetry.getGdata().getId());
        ;
        Map<String, Object> eks = (Map<String, Object>) telemetry.getEData().get("eks");
        learnerAssessmentData.setQid((String) eks.get("qid"));
        learnerAssessmentData.setQindex((Double) eks.get("qindex"));
        String pass = (String) eks.get("pass");
        learnerAssessmentData.setCorrect(("Yes".equalsIgnoreCase(pass) ? 1 : 0));
        learnerAssessmentData.setScore((Double) eks.get("score"));
        learnerAssessmentData.setTimespent((Double) eks.get("length"));
        if ("2.0".equalsIgnoreCase(telemetry.getVer())) {
            learnerAssessmentData.setTimestamp((Long) telemetry.getEts());
            learnerAssessmentData.setRes(GsonUtil.toJson(eks.get("resvalues")));
        } else {
            learnerAssessmentData.setTimestamp(DateUtil.dateToEpoch(telemetry.getTs()));
            learnerAssessmentData.setRes(GsonUtil.toJson(eks.get("res")));
        }
        learnerAssessmentData.setQdesc((String) eks.get("qdesc"));
        learnerAssessmentData.setQtitle((String) eks.get("qtitle"));
        return learnerAssessmentData;
    }


}
