package org.ekstep.genieservices.content;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.ContentFeedbackFilterCriteria;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GEFeedback;
import org.ekstep.genieservices.commons.db.contract.ContentFeedbackEntry;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.bean.enums.GEFeedbackContextType;
import org.ekstep.genieservices.content.db.model.ContentFeedbackModel;
import org.ekstep.genieservices.content.db.model.ContentFeedbacksModel;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        mGameData = new GameData(mAppContext.getParams().getString(ServiceConstants.Params.GID), mAppContext.getParams().getString(ServiceConstants.Params.VERSION_NAME));
    }

    @Override
    public GenieResponse<Void> sendFeedback(ContentFeedback feedback) {
        if (StringUtil.isNullOrEmpty(feedback.getContentId())) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, ServiceConstants.ErrorMessage.MANDATORY_FIELD_CONTENT_IDENTIFIER, TAG);
        }

        String uid = ContentHandler.getCurrentUserId(userService);

        saveContentFeedbackEvent(feedback);

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

    private void saveContentFeedbackEvent(ContentFeedback feedback) {
        GEFeedback geFeedback = new GEFeedback(mGameData, "RATING",
                feedback.getContentId(), feedback.getRating(), feedback.getComments(),
                GEFeedbackContextType.CONTENT.getValue(), feedback.getStageId());

        TelemetryLogger.log(geFeedback);
    }

    @Override
    public GenieResponse<List<ContentFeedback>> getFeedback(ContentFeedbackFilterCriteria criteria) {
        GenieResponse<List<ContentFeedback>> response;

        String userFilter = null;
        String contentIdFilter = null;

        if (criteria != null) {
            if (criteria.getUid() != null) {
                userFilter = String.format(Locale.US, "%s = '%s'", ContentFeedbackEntry.COLUMN_NAME_UID, criteria.getUid());
            }
            if (criteria.getContentId() != null) {
                contentIdFilter = String.format(Locale.US, "%s = '%s'", ContentFeedbackEntry.COLUMN_NAME_CONTENT_ID, criteria.getContentId());
            }
        }

        String filter = null;
        if (!StringUtil.isNullOrEmpty(contentIdFilter) && !StringUtil.isNullOrEmpty(userFilter)) {
            filter = String.format(Locale.US, " where %s AND %s", userFilter, contentIdFilter);
        } else if (!StringUtil.isNullOrEmpty(contentIdFilter)) {
            filter = String.format(Locale.US, " where %s", contentIdFilter);
        } else if (!StringUtil.isNullOrEmpty(userFilter)) {
            filter = String.format(Locale.US, " where %s", userFilter);
        }
        ContentFeedbacksModel contentFeedbacksModel = ContentFeedbacksModel.find(mAppContext.getDBSession(), filter);

        List<ContentFeedback> contentFeedbackList = new ArrayList<>();
        if (contentFeedbacksModel != null) {
            for (ContentFeedbackModel contentFeedbackModel : contentFeedbacksModel.getContentFeedbackModelList()) {
                ContentFeedback contentFeedback = new ContentFeedback(contentFeedbackModel.getCreatedAt());
                contentFeedback.setContentId(contentFeedbackModel.getContentId());
                if (!StringUtil.isNullOrEmpty(contentFeedbackModel.getRating())) {
                    contentFeedback.setRating(Float.valueOf(contentFeedbackModel.getRating()));
                }
                contentFeedback.setComments(contentFeedbackModel.getComments());

                contentFeedbackList.add(contentFeedback);
            }
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentFeedbackList);
        return response;
    }
}
