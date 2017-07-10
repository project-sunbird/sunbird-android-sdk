package org.ekstep.genieresolvers.summarizer;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
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

    public void getLearnerAssessment(String uid, String contentId, List<Map> hierarchyData, IResponseHandler responseHandler) {
        List<String> idList = new ArrayList<>();
        String hierarchyDataInString = null;
        if (hierarchyData != null && hierarchyData.size() > 0) {
            for (int i = 0 ; i < hierarchyData.size() - 1 ; i++) {
                Object identifier = hierarchyData.get(i).get("identifier");
                idList.add(identifier == null ? null : identifier.toString());
            }
        }

        hierarchyDataInString = idList.size() > 0 ? StringUtil.join("/", idList) : null;

        LearnerAssessmentTask createUserTask = new LearnerAssessmentTask(context, appQualifier, uid, contentId, hierarchyDataInString);
        createAndExecuteTask(responseHandler, createUserTask);
    }

}
