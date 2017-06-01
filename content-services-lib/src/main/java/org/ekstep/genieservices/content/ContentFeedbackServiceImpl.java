package org.ekstep.genieservices.content;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GEFeedback;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.bean.enums.GEFeedbackContextType;
import org.ekstep.genieservices.content.db.model.ContentFeedbackModel;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

/**
 * This is the implementation of the interface {@link IContentFeedbackService}
 */
public class ContentFeedbackServiceImpl extends BaseService implements IContentFeedbackService {

    private static final String TAG = ContentFeedbackServiceImpl.class.getSimpleName();

    private IUserService userService;
    private GameData mGameData;

    public ContentFeedbackServiceImpl(AppContext appContext, IUserService userService) {
        super(appContext);

        this.userService = userService;
        mGameData = new GameData(mAppContext.getParams().getGid(), mAppContext.getParams().getVersionName());
    }

    @Override
    public GenieResponse<Void> sendFeedback(ContentFeedback feedback) {
        return sendFeedback(feedback, null);
    }

    @Override
    public GenieResponse<Void> sendFeedback(ContentFeedback feedback, String stageId) {
        if (StringUtil.isNullOrEmpty(feedback.getContentId())) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, ServiceConstants.ErrorMessage.MANDATORY_FIELD_CONTENT_IDENTIFIER, TAG);
        }

        String uid = ContentHandler.getCurrentUserId(userService);

        saveContentFeedbackEvent(feedback, stageId);

        // Save or update the content feedback in DB.
        ContentFeedbackModel contentFeedbackModel = ContentFeedbackModel.build(mAppContext.getDBSession(),
                uid, feedback.getContentId(), String.valueOf(feedback.getRating()), feedback.getComments());
        ContentFeedbackModel contentFeedbackModelInDB = ContentFeedbackModel.find(mAppContext.getDBSession(), uid, feedback.getContentId());
        if (contentFeedbackModelInDB == null) {
            contentFeedbackModel.save();
        } else {
            contentFeedbackModel.update();
        }

        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

    private void saveContentFeedbackEvent(ContentFeedback feedback, String stageId) {
        GEFeedback geFeedback = new GEFeedback(mGameData, "RATING",
                feedback.getContentId(), feedback.getRating(), feedback.getComments(),
                GEFeedbackContextType.CONTENT.getValue(), stageId);

        TelemetryLogger.log(geFeedback);
    }

    @Override
    public GenieResponse<ContentFeedback> getFeedback(String uid, String contentIdentifier) {
        GenieResponse<ContentFeedback> response;

        ContentFeedbackModel contentFeedbackModel = ContentFeedbackModel.find(mAppContext.getDBSession(), uid, contentIdentifier);
        if (contentFeedbackModel != null) {
            ContentFeedback contentFeedback = new ContentFeedback(contentFeedbackModel.getCreatedAt());
            contentFeedback.setContentId(contentFeedbackModel.getContentId());
            if (!StringUtil.isNullOrEmpty(contentFeedbackModel.getRating())) {
                contentFeedback.setRating(Float.valueOf(contentFeedbackModel.getRating()));
            }
            contentFeedback.setComments(contentFeedbackModel.getComments());

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(contentFeedback);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.DATA_NOT_FOUND_ERROR, ServiceConstants.ErrorMessage.NO_FEEDBACK, TAG);
        }

        return response;
    }
}
