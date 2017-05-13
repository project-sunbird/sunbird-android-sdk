package org.ekstep.genieservices.tag;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ITagService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.CommonConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Tag;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.SecurityUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.tag.cache.TelemetryTagCache;
import org.ekstep.genieservices.tag.model.TagModel;
import org.ekstep.genieservices.tag.model.TagsModel;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by swayangjit on 12/5/17.
 */

public class TagServiceImpl extends BaseService implements ITagService {

    private static final String TAG = "TagService";

    public TagServiceImpl(AppContext appContext) {
        super(appContext);
    }


    @Override
    public GenieResponse<Void> setTag(Tag tag) {
        HashMap params = new HashMap();
        params.put("tag", GsonUtil.toJson(tag));
        params.put("logLevel", CommonConstants.LOG_LEVEL);

        String errorMessage = "Unable to set tag";

        GenieResponse<Void> genieResponse;
        try {
            if (tag != null && !StringUtil.isNullOrEmpty(tag.getName())) {
                TagModel existingTagModel = TagModel.find(mAppContext.getDBSession(), tag.getName());
                TagModel tagModel = TagModel.build(mAppContext.getDBSession(), tag.getName().trim(), SecurityUtil.toSha1(tag.getName().trim()),
                        tag.getDescription(),
                        tag.getStartDate(),
                        tag.getEndDate());
                if (existingTagModel == null) {
                    tagModel.save();
                } else {
                    tagModel.update();
                }
                TelemetryTagCache.clearCache(mAppContext);
                genieResponse = GenieResponseBuilder.getSuccessResponse("Tag set successfully");
                TelemetryLogger.logSuccess(mAppContext, genieResponse, new HashMap(), TAG, "setTag@TagService", params);
            } else {
                genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, "Tag name can't be null or empty", errorMessage);
                TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "setTag@TagService", params, errorMessage);
            }
        } catch (Exception e) {
            genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROCESSING_ERROR, errorMessage + e.toString(), "Tag setting failed", Void.class);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "setTag@TagService", params, errorMessage);
        }

        return genieResponse;
    }

    @Override
    public GenieResponse<List<Tag>> getTags() {

        HashMap params = new HashMap();
        params.put("logLevel", CommonConstants.LOG_LEVEL);

        String errorMessage = "Unable to get tags";

        GenieResponse<List<Tag>> genieResponse;

        List<Tag> tagList = new ArrayList<>();
        try {
            TagsModel telemetryTags = TagsModel.find(mAppContext.getDBSession());
            if (telemetryTags != null && telemetryTags.getTags() != null) {
                for (TagModel tagModel : telemetryTags.getTags()) {
                    tagList.add(new Tag(tagModel.name(), tagModel.description(), tagModel.startDate(), tagModel.endDate()));
                }

            }
            genieResponse = GenieResponseBuilder.getSuccessResponse("Tag retrieved successfully");
            genieResponse.setResult(tagList);
            TelemetryLogger.logSuccess(mAppContext, genieResponse, new HashMap(), TAG, "getTags@TagService", params);
            return genieResponse;
        } catch (Exception e) {
            genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.DB_ERROR, errorMessage);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "getTags@TagService", params, errorMessage);
            return genieResponse;
        }
    }

    @Override
    public GenieResponse<Void> deleteTag(String name) {

        HashMap params = new HashMap();
        params.put("tagName", name);
        params.put("logLevel", CommonConstants.LOG_LEVEL);

        String errorMessage = "Unable to delete the tag";
        GenieResponse<Void> genieResponse;

        try {
            TagModel existingTagModel = TagModel.build(mAppContext.getDBSession(), name);
            existingTagModel.clean();
            genieResponse = GenieResponseBuilder.getSuccessResponse("Tag deleted successfully");
            TelemetryLogger.logSuccess(mAppContext, genieResponse, new HashMap(), TAG, "deleteTag@TagService", params);
            return genieResponse;
        } catch (Exception e) {
            genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.DB_ERROR, errorMessage);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "deleteTag@TagService", params, errorMessage);
            return genieResponse;
        }

    }

    @Override
    public GenieResponse<Void> updateTag(Tag tag) {

        HashMap params = new HashMap();
        params.put("tag", GsonUtil.toJson(tag));
        params.put("logLevel", CommonConstants.LOG_LEVEL);

        String errorMessage = "Unable to update tag";

        GenieResponse<Void> genieResponse;
        try {
            if (tag != null && !StringUtil.isNullOrEmpty(tag.getName())) {
                TagModel tagModel = TagModel.build(mAppContext.getDBSession(), tag.getName().trim(), SecurityUtil.toSha1(tag.getName().trim()),
                        tag.getDescription(),
                        tag.getStartDate(),
                        tag.getEndDate());

                tagModel.update();
                TelemetryTagCache.clearCache(mAppContext);
                genieResponse = GenieResponseBuilder.getSuccessResponse("Tag updated successfully");
                TelemetryLogger.logSuccess(mAppContext, genieResponse, new HashMap(), TAG, "updateTag@TagService", params);

            } else {
                genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, "Tag name can't be null or empty", errorMessage);
                TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "updateTag@TagService", params, errorMessage);
            }
        } catch (Exception e) {
            genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.DB_ERROR, errorMessage + e.toString(), "Tag updation failed", Void.class);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "updateTag@TagService", params, errorMessage);
        }


        return null;
    }
}
