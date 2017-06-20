package org.ekstep.genieresolvers.summarizer;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;

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

    public void getLearnerAssessment(String uid, String contentId, String hierarchyData, IResponseHandler responseHandler) {
        LearnerAssessmentTask createUserTask = new LearnerAssessmentTask(context, appQualifier, uid, contentId, hierarchyData);
        createAndExecuteTask(responseHandler, createUserTask);
    }

}
