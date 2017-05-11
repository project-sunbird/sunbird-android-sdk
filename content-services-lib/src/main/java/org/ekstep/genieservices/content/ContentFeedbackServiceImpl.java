package org.ekstep.genieservices.content;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.content.db.model.ContentFeedbackModel;

/**
 * Created on 5/9/2017.
 *
 * @author anil
 */
public class ContentFeedbackServiceImpl extends BaseService implements IContentFeedbackService {

    public ContentFeedbackServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<ContentFeedback> getFeedback(String uid, String contentIdentifier) {
        ContentFeedback contentFeedback = null;
        ContentFeedbackModel contentFeedbackModel = ContentFeedbackModel.find(mAppContext.getDBSession(), uid, contentIdentifier);
        if (contentFeedbackModel != null) {
            contentFeedback = new ContentFeedback();
            contentFeedback.setContentId(contentFeedbackModel.getContentId());
            contentFeedback.setUid(contentFeedbackModel.getUid());
            contentFeedback.setRating(contentFeedbackModel.getRating());
            contentFeedback.setComments(contentFeedbackModel.getComments());
            contentFeedback.setCreatedAt(contentFeedbackModel.getCreatedAt());
        }

        GenieResponse<ContentFeedback> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentFeedback);

        return response;
    }
}
