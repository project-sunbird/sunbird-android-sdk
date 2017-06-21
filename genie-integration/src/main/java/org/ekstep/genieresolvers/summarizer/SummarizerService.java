package org.ekstep.genieresolvers.summarizer;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.HierarchyInfo;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 6/6/17.
 * shriharsh
 */

public class SummarizerService extends BaseService {

    private String appQualifier;
    private Context context;
    private String hierarchyDataInString;

    public SummarizerService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    public void getLearnerAssessment(String uid, String contentId, List<HierarchyInfo> hierarchyData, IResponseHandler responseHandler) {
        List<String> idList = new ArrayList<>();
        if (hierarchyData != null) {
            for (HierarchyInfo hierarchyInfo : hierarchyData) {
                idList.add(hierarchyInfo.getIdentifier());
            }

            hierarchyDataInString = StringUtil.join("/", idList);
        }
        LearnerAssessmentTask createUserTask = new LearnerAssessmentTask(context, appQualifier, uid, contentId, hierarchyDataInString);
        createAndExecuteTask(responseHandler, createUserTask);
    }

}
