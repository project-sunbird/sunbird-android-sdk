package org.ekstep.genieservices.content;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.db.model.ContentFeedbackModel;

/**
 * This is the implementation of the interface {@link IContentFeedbackService}
 */
public class ContentFeedbackServiceImpl extends BaseService implements IContentFeedbackService {

    private static final String TAG = ContentFeedbackServiceImpl.class.getSimpleName();

    public ContentFeedbackServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Void> saveFeedback(String uid, String contentIdentifier, String rating, String comments) {
        if (StringUtil.isNullOrEmpty(contentIdentifier)) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, ServiceConstants.ErrorMessage.MANDATORY_FIELD_CONTENT_IDENTIFIER, TAG);
        }

        if (StringUtil.isNullOrEmpty(uid)) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, ServiceConstants.ErrorMessage.MANDATORY_FIELD_UID, TAG);
        }

        // TODO: 5/29/2017 - Save feedback telemetry event here.

        ContentFeedbackModel contentFeedbackModel = ContentFeedbackModel.build(mAppContext.getDBSession(), uid, contentIdentifier, rating, comments);
        ContentFeedbackModel contentFeedbackModelInDB = ContentFeedbackModel.find(mAppContext.getDBSession(), uid, contentIdentifier);
        if (contentFeedbackModelInDB == null) {
            contentFeedbackModel.save();
        } else {
            contentFeedbackModel.update();
        }

        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

    @Override
    public GenieResponse<ContentFeedback> getFeedback(String uid, String contentIdentifier) {
        GenieResponse<ContentFeedback> response;

        ContentFeedbackModel contentFeedbackModel = ContentFeedbackModel.find(mAppContext.getDBSession(), uid, contentIdentifier);
        if (contentFeedbackModel != null) {
            ContentFeedback contentFeedback = new ContentFeedback();
            contentFeedback.setContentId(contentFeedbackModel.getContentId());
            contentFeedback.setUid(contentFeedbackModel.getUid());
            contentFeedback.setRating(contentFeedbackModel.getRating());
            contentFeedback.setComments(contentFeedbackModel.getComments());
            contentFeedback.setCreatedAt(contentFeedbackModel.getCreatedAt());

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(contentFeedback);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.DATA_NOT_FOUND_ERROR, ServiceConstants.ErrorMessage.NO_FEEDBACK, TAG);
        }

        return response;
    }
}
