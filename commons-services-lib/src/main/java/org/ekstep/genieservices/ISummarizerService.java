package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentSummary;
import org.ekstep.genieservices.commons.bean.SummaryRequest;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;

import java.util.List;
import java.util.Map;

/**
 * This is the interface with all the required APIs for performing summarizer related
 * operations.
 */
public interface ISummarizerService {


    /**
     * This api is used to the summary details of the child/content.
     * if child summary is needed then uid has to be set and if the content summary is needed then contentId has to be set
     * <p>
     * <p>
     * On successful fetching the data, the response will return status as TRUE and with List<LearnerAssessmentSummary> in the result
     * <p>
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     * <p>PROCESSING_ERROR
     *
     * @param summaryRequest - {@link SummaryRequest}
     * @return {@link GenieResponse<List<LearnerAssessmentDetails>>}
     */
    GenieResponse<List<LearnerAssessmentSummary>> getSummary(SummaryRequest summaryRequest);

    /**
     * This api is used to get the learner assessment details
     * <p>
     * In the {@link SummaryRequest} both the uid and contentId has to be mentioned
     * <p>
     * <p>
     * On successful fetching the data, the response will return status as TRUE and with List<LearnerAssessmentDetails> in the result
     * <p>
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     * <p>PROCESSING_ERROR
     *
     * @param summaryRequest - {@link SummaryRequest}
     * @return {@link GenieResponse<List<LearnerAssessmentDetails>>}
     */
    GenieResponse<List<LearnerAssessmentDetails>> getLearnerAssessmentDetails(SummaryRequest summaryRequest);

    GenieResponse<Void> saveLearnerAssessmentDetails(Telemetry telemetry);

    GenieResponse<Void> saveLearnerContentSummaryDetails(Telemetry telemetry);


    /**
     * This api will give the list of users and the reports related to a particular content
     *
     * @param summaryRequest
     * @return
     */
    GenieResponse<List<Map<String, Object>>> getReportsByUser(SummaryRequest summaryRequest);

}
