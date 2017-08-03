package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ISummarizerService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentSummary;
import org.ekstep.genieservices.commons.bean.SummaryRequest;

import java.util.List;

/**
 * This class provides all the required APIs to  perform summarizer related
 * operations
 */
public class SummarizerService {
    private ISummarizerService summarizerService;

    public SummarizerService(GenieService genieService) {
        this.summarizerService = genieService.getSummarizerService();
    }

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
     * @param responseHandler -{@link IResponseHandler<List<LearnerAssessmentSummary>>}
     */
    public void getSummary(final SummaryRequest summaryRequest, IResponseHandler<List<LearnerAssessmentSummary>> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<List<LearnerAssessmentSummary>>() {
            @Override
            public GenieResponse<List<LearnerAssessmentSummary>> perform() {
                return summarizerService.getSummary(summaryRequest);
            }
        }, responseHandler);
    }

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
     * @param responseHandler - {@link IResponseHandler<List<LearnerAssessmentDetails>>}
     */
    public void getLearnerAssessmentDetails(final SummaryRequest summaryRequest, IResponseHandler<List<LearnerAssessmentDetails>> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<List<LearnerAssessmentDetails>>() {
            @Override
            public GenieResponse<List<LearnerAssessmentDetails>> perform() {
                return summarizerService.getLearnerAssessmentDetails(summaryRequest);
            }
        }, responseHandler);
    }
}
