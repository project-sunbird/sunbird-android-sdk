package org.ekstep.genieservices.tag;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ITagService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Tag;
import org.ekstep.genieservices.commons.utils.CryptoUtil;
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
 * This class is the implementation of {@link ITagService}
 */
public class TagServiceImpl extends BaseService implements ITagService {

    private static final String TAG = "TagService";

    public TagServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Void> setTag(Tag tag) {
        String methodName = "setTag@TagService";
        HashMap params = new HashMap();
        params.put("tag", GsonUtil.toJson(tag));
        params.put("logLevel", "2");

        GenieResponse<Void> genieResponse;
        if (tag != null && !StringUtil.isNullOrEmpty(tag.getName())) {
            TagModel existingTagModel = find(mAppContext.getDBSession(), tag.getName());
            String tagHash = null;

            try {
                tagHash = CryptoUtil.checksum(tag.getName().trim());
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
            genieResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            TelemetryLogger.logSuccess(mAppContext, genieResponse, TAG, methodName, params);
        } else {
            genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, ServiceConstants.ErrorMessage.TAG_NAME_SHOULD_NOT_BE_EMPTY, TAG);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, methodName, params, ServiceConstants.ErrorMessage.TAG_NAME_SHOULD_NOT_BE_EMPTY);
        }

        return genieResponse;
    }

    @Override
    public GenieResponse<List<Tag>> getTags() {
        String methodName = "getTags@TagService";
        HashMap params = new HashMap();
        params.put("logLevel", "2");

        GenieResponse<List<Tag>> genieResponse;

        List<Tag> tagList = new ArrayList<>();
        TagsModel telemetryTags = TagsModel.find(mAppContext.getDBSession());
        if (telemetryTags != null && telemetryTags.getTags() != null) {
            for (TagModel tagModel : telemetryTags.getTags()) {
                tagList.add(new Tag(tagModel.name(), tagModel.description(), tagModel.startDate(), tagModel.endDate()));
            }
        }
        genieResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        genieResponse.setResult(tagList);
        TelemetryLogger.logSuccess(mAppContext, genieResponse, TAG, methodName, params);
        return genieResponse;
    }

    @Override
    public GenieResponse<Void> deleteTag(String name) {

        String methodName = "deleteTag@TagService";
        HashMap params = new HashMap();
        params.put("tagName", name);
        params.put("logLevel", "2");

        GenieResponse<Void> genieResponse;
        TagModel existingTagModel = TagModel.find(mAppContext.getDBSession(), name);

        if (existingTagModel != null) {
            existingTagModel.clear();
            genieResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            TelemetryLogger.logSuccess(mAppContext, genieResponse, TAG, methodName, params);
        } else {
            genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_TAG, TAG);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_TAG);
        }
        return genieResponse;
    }

    @Override
    public GenieResponse<Void> updateTag(Tag tag) {
        String methodName = "updateTag@TagService";
        HashMap params = new HashMap();
        params.put("tag", GsonUtil.toJson(tag));
        params.put("logLevel", "2");

        GenieResponse<Void> genieResponse;
        if (tag != null && !StringUtil.isNullOrEmpty(tag.getName())) {
            TagModel tagModel = TagModel.find(mAppContext.getDBSession(), tag.getName().trim());
            if (tagModel == null) {
                genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_TAG, TAG);
                TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_TAG);
            } else {
                String tagHash = null;
                try {
                    tagHash = CryptoUtil.checksum(tag.getName().trim());
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    //This will never occur as the algo and encoding are hardcoded.
                }
                // TODO: 6/6/2017 - Needs to revisit update.
                tagModel = TagModel.build(mAppContext.getDBSession(), tag.getName().trim(), tagHash,
                        tag.getDescription(),
                        tag.getStartDate(),
                        tag.getEndDate());
                tagModel.update();
                TelemetryTagCache.clearCache(mAppContext);
                genieResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                TelemetryLogger.logSuccess(mAppContext, genieResponse, TAG, methodName, params);
            }
        } else {
            genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, ServiceConstants.ErrorMessage.TAG_NAME_SHOULD_NOT_BE_EMPTY, TAG);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, methodName, params, ServiceConstants.ErrorMessage.TAG_NAME_SHOULD_NOT_BE_EMPTY);
        }

        return genieResponse;
    }

}
