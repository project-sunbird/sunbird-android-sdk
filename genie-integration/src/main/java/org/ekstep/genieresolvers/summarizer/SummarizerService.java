package org.ekstep.genieresolvers.summarizer;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;

import java.util.Map;

/**
 * This is the {@link SummarizerService} with all the required APIs for performing summarizer related operations.
 */
public class SummarizerService extends BaseService {

    private String appQualifier;
    private Context context;

    public SummarizerService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    /**
     *
     * This api is used to get the learner assessment details
     * <p>
     * <p>
     * On successful fetching the data, the response will return status as TRUE and with List<LearnerAssessmentDetails> in the result
     * <p>
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     * <p>PROCESSING_ERROR
     *
     * @param userId
     * @param currentContentIdentifier
     * @param hierarchyData
     * @param responseHandler
     */
    public void getLearnerAssessment(String userId, String currentContentIdentifier, Map hierarchyData, IResponseHandler responseHandler) {
        LearnerAssessmentTask createUserTask = new LearnerAssessmentTask(context, appQualifier, userId, currentContentIdentifier, hierarchyData);
        createAndExecuteTask(responseHandler, createUserTask);
    }

}
