package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentData;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentSummaryResponse;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;

import java.util.List;
import java.util.Map;

/**
 * Created on 4/6/17.
 * shriharsh
 */

public interface ISummarizerService {

    GenieResponse<List<LearnerAssessmentSummaryResponse>> getChildSummary(String uid);

    GenieResponse<List<LearnerAssessmentSummaryResponse>> getContentSummary(String contentId);

    GenieResponse<List<LearnerAssessmentData>> getLearnerAssessmentSummary(String uid, String contentId);

    GenieResponse<Void> saveLearnerAssessment(Telemetry telemetry);


}
