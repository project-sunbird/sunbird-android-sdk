package org.ekstep.genieservices.tag;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ITagService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.CommonConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Tag;
import org.ekstep.genieservices.commons.utils.Crypto;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.tag.cache.TelemetryTagCache;
import org.ekstep.genieservices.tag.model.TagModel;
import org.ekstep.genieservices.tag.model.TagsModel;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.ekstep.genieservices.tag.model.TagModel.find;


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

        GenieResponse<Void> genieResponse;
        if (tag != null && !StringUtil.isNullOrEmpty(tag.getName())) {
            TagModel existingTagModel = find(mAppContext.getDBSession(), tag.getName());
            String tagHash = null;
            try {
                tagHash = Crypto.checksum(tag.getName().trim());
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                //This will never occur as the algo and encoding are hardcoded.
            }
            TagModel tagModel = TagModel.build(mAppContext.getDBSession(), tag.getName().trim(), tagHash,
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
            String errorMessage = "Unable to set tag";
            genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, "Tag name can't be null or empty", errorMessage);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "setTag@TagService", params, errorMessage);
        }

        return genieResponse;
    }

    @Override
    public GenieResponse<List<Tag>> getTags() {

        HashMap params = new HashMap();
        params.put("logLevel", CommonConstants.LOG_LEVEL);

        GenieResponse<List<Tag>> genieResponse;

        List<Tag> tagList = new ArrayList<>();
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
    }

    @Override
    public GenieResponse<Void> deleteTag(String name) {

        HashMap params = new HashMap();
        params.put("tagName", name);
        params.put("logLevel", CommonConstants.LOG_LEVEL);

        GenieResponse<Void> genieResponse = null;
        TagModel existingTagModel = TagModel.find(mAppContext.getDBSession(), name);

        if (existingTagModel != null) {
            existingTagModel.clean();
            genieResponse = GenieResponseBuilder.getSuccessResponse("Tag deleted successfully");
            TelemetryLogger.logSuccess(mAppContext, genieResponse, new HashMap(), TAG, "deleteTag@TagService", params);
        } else {
            genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, "Tag name not found", "Tag name not found");
        }
        return genieResponse;
    }

    @Override
    public GenieResponse<Void> updateTag(Tag tag) {

        HashMap params = new HashMap();
        params.put("tag", GsonUtil.toJson(tag));
        params.put("logLevel", CommonConstants.LOG_LEVEL);

        String errorMessage = "Unable to update tag";

        GenieResponse<Void> genieResponse = null;
        if (tag != null && !StringUtil.isNullOrEmpty(tag.getName())) {
            TagModel tagModel = TagModel.find(mAppContext.getDBSession(), tag.getName().trim());
            if (tagModel == null) {
                genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, "Tag name not found", "Tag name not found");
                TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "updateTag@TagService", params, errorMessage);
            } else {
                String tagHash = null;
                try {
                    tagHash = Crypto.checksum(tag.getName().trim());
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    //This will never occur as the algo and encoding are hardcoded.
                }
                tagModel = TagModel.build(mAppContext.getDBSession(), tag.getName().trim(), tagHash,
                        tag.getDescription(),
                        tag.getStartDate(),
                        tag.getEndDate());
                tagModel.update();
                TelemetryTagCache.clearCache(mAppContext);
                genieResponse = GenieResponseBuilder.getSuccessResponse("Tag updated successfully");
                TelemetryLogger.logSuccess(mAppContext, genieResponse, new HashMap(), TAG, "updateTag@TagService", params);
            }
        } else {
            genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, "Tag name can't be null or empty", errorMessage);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "updateTag@TagService", params, errorMessage);
        }
        return genieResponse;
    }
}
