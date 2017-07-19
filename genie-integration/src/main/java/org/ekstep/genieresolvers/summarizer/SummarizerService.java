package org.ekstep.genieresolvers.summarizer;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;

import java.util.Map;

/**
 * Created on 6/6/17.
 * shriharsh
 */
public class SummarizerService extends BaseService {

    private String appQualifier;
    private Context context;

    public SummarizerService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    public void getLearnerAssessment(String userId, String currentContentIdentifier, Map hierarchyData, IResponseHandler responseHandler) {
        LearnerAssessmentTask createUserTask = new LearnerAssessmentTask(context, appQualifier, userId, currentContentIdentifier, hierarchyData);
        createAndExecuteTask(responseHandler, createUserTask);
    }

}
