package org.ekstep.genieservices.content;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IConfigService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IContentService;
import org.ekstep.genieservices.IDownloadService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ChildContentRequest;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentData;
import org.ekstep.genieservices.commons.bean.ContentDeleteRequest;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentExportRequest;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.ContentFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentImportRequest;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.ContentListing;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.HierarchyInfo;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RecommendedContentResult;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.bean.telemetry.GEInteract;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.chained.export.AddGeTransferContentExportEvent;
import org.ekstep.genieservices.content.chained.export.CleanTempLoc;
import org.ekstep.genieservices.content.chained.export.CompressContent;
import org.ekstep.genieservices.content.chained.export.CopyAsset;
import org.ekstep.genieservices.content.chained.export.CreateContentExportManifest;
import org.ekstep.genieservices.content.chained.export.CreateTempLoc;
import org.ekstep.genieservices.content.chained.export.DeleteTemporaryEcar;
import org.ekstep.genieservices.content.chained.export.EcarBundle;
import org.ekstep.genieservices.content.chained.export.WriteManifest;
import org.ekstep.genieservices.content.chained.imports.AddGeTransferContentImportEvent;
import org.ekstep.genieservices.content.chained.imports.ContentImportStep;
import org.ekstep.genieservices.content.chained.imports.DeviceMemoryCheck;
import org.ekstep.genieservices.content.chained.imports.EcarCleanUp;
import org.ekstep.genieservices.content.chained.imports.ExtractEcar;
import org.ekstep.genieservices.content.chained.imports.ExtractPayloads;
import org.ekstep.genieservices.content.chained.imports.ValidateEcar;
import org.ekstep.genieservices.content.db.model.ContentListingModel;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.network.ContentSearchAPI;
import org.ekstep.genieservices.content.network.RecommendedContentAPI;
import org.ekstep.genieservices.content.network.RelatedContentAPI;
import org.ekstep.genieservices.eventbus.EventPublisher;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created on 5/10/2017.
 *
 * @author anil
 */
public class ContentServiceImpl extends BaseService implements IContentService {

    private static final String TAG = ContentServiceImpl.class.getSimpleName();

    private IUserService userService;
    private IContentFeedbackService contentFeedbackService;
    private IConfigService configService;
    private IDownloadService downloadService;

    public ContentServiceImpl(AppContext appContext, IUserService userService, IContentFeedbackService contentFeedbackService, IConfigService configService, IDownloadService downloadService) {
        super(appContext);

        this.userService = userService;
        this.contentFeedbackService = contentFeedbackService;
        this.configService = configService;
        this.downloadService = downloadService;
    }

    @Override
    public GenieResponse<Content> getContentDetails(ContentDetailsRequest contentDetailsRequest) {
        String methodName = "getContentDetails@ContentServiceImpl";
        HashMap params = new HashMap();
        params.put("request", GsonUtil.toJson(contentDetailsRequest));
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));

        GenieResponse<Content> response;
        ContentModel contentModelInDB = ContentModel.find(mAppContext.getDBSession(), contentDetailsRequest.getContentId());

        if (contentModelInDB == null) {     // Fetch from server if detail is not available in DB
            Map contentData = ContentHandler.fetchContentDetailsFromServer(mAppContext, contentDetailsRequest.getContentId());
            if (contentData == null) {
                response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_DATA_FOUND, ServiceConstants.ErrorMessage.CONTENT_NOT_FOUND + contentDetailsRequest.getContentId(), TAG);
                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.CONTENT_NOT_FOUND + contentDetailsRequest.getContentId());
                return response;
            }

            contentModelInDB = ContentHandler.convertContentMapToModel(mAppContext.getDBSession(), contentData, null);
        } else {
            ContentHandler.refreshContentDetailsFromServer(mAppContext, contentDetailsRequest.getContentId(), contentModelInDB);
        }

        Content content = ContentHandler.convertContentModelToBean(contentModelInDB);

        if (content.isAvailableLocally()) {
            String uid = ContentHandler.getCurrentUserId(userService);
            if (contentDetailsRequest.isAttachFeedback()) {
                content.setContentFeedback(ContentHandler.getContentFeedback(contentFeedbackService, content.getIdentifier(), uid));
            }

            if (contentDetailsRequest.isAttachContentAccess()) {
                content.setContentAccess(ContentHandler.getContentAccess(userService, content.getIdentifier(), uid));
            }
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(content);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<List<Content>> getAllLocalContent(ContentFilterCriteria criteria) {
        String methodName = "getAllLocalContent@ContentServiceImpl";
        HashMap params = new HashMap();
        params.put("criteria", GsonUtil.toJson(criteria));

        GenieResponse<List<Content>> response;

        List<ContentModel> contentModelListInDB = ContentHandler.getAllLocalContent(mAppContext.getDBSession(), criteria);

        List<Content> contentList = new ArrayList<>();
        for (ContentModel contentModel : contentModelListInDB) {
            Content c = ContentHandler.convertContentModelToBean(contentModel);

            if (criteria.attachFeedback()) {
                c.setContentFeedback(ContentHandler.getContentFeedback(contentFeedbackService, c.getIdentifier(), criteria.getUid()));
            }
            if (criteria.attachContentAccess()) {
                c.setContentAccess(ContentHandler.getContentAccess(userService, c.getIdentifier(), criteria.getUid()));
            }
            contentList.add(c);
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentList);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<Content> getChildContents(ChildContentRequest childContentRequest) {
        String methodName = "getChildContents@ContentServiceImpl";
        HashMap params = new HashMap();
        params.put("request", GsonUtil.toJson(childContentRequest));

        GenieResponse<Content> response;
        ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), childContentRequest.getContentId());
        if (contentModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_DATA_FOUND, ServiceConstants.ErrorMessage.CONTENT_NOT_FOUND + childContentRequest.getContentId(), TAG);
            return response;
        }

        List<HierarchyInfo> hierarchyInfoList = new ArrayList<>();
        hierarchyInfoList.add(new HierarchyInfo(contentModel.getIdentifier(), contentModel.getContentType()));

        //check and fetch all children of this content
        Content content = checkAndFetchChildrenOfContent(contentModel, hierarchyInfoList, 0, childContentRequest.getLevel());

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(content);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    private Content checkAndFetchChildrenOfContent(ContentModel contentModel, List<HierarchyInfo> sourceInfoList, int currentLevel, int level) {
        Content content = ContentHandler.convertContentModelToBean(contentModel);
        content.setHierarchyInfo(sourceInfoList);

        // check if the content model has immediate children
        List<ContentModel> contentModelList = ContentHandler.getSortedChildrenList(mAppContext.getDBSession(), contentModel.getLocalData(), ContentConstants.ChildContents.ALL);
        if (contentModelList.size() > 0
                && (level == -1 || currentLevel < level)) {
            currentLevel = currentLevel + 1;
            List<Content> childContents = new ArrayList<>();

            for (ContentModel perContentModel : contentModelList) {
                Content perContent = ContentHandler.convertContentModelToBean(perContentModel);
                List<HierarchyInfo> hierarchyInfoList = new ArrayList<>(sourceInfoList);
                hierarchyInfoList.add(new HierarchyInfo(perContent.getIdentifier(), perContent.getContentType()));
                perContent.setHierarchyInfo(hierarchyInfoList);
                Content iteratedContent = checkAndFetchChildrenOfContent(perContentModel, hierarchyInfoList, currentLevel, level);
                perContent.setChildren(iteratedContent.getChildren());

                childContents.add(perContent);
            }

            //add children to the main content
            content.setChildren(childContents);
        }

        return content;
    }

    @Override
    public GenieResponse<Void> deleteContent(ContentDeleteRequest deleteRequest) {

        HashMap params = new HashMap();
        params.put("request", GsonUtil.toJson(deleteRequest));
        String methodName = "deleteContent@ContentServiceImpl";

        GenieResponse<Void> response;
        ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), deleteRequest.getContentId());

        if (contentModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_DATA_FOUND, ServiceConstants.ErrorMessage.CONTENT_NOT_FOUND_TO_DELETE + deleteRequest.getContentId(), TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.NO_CONTENT_LISTING_DATA);
            return response;
        }

        //delete or update pre-requisites
        if (ContentHandler.hasPreRequisites(contentModel.getLocalData())) {
            ContentHandler.deleteAllPreRequisites(mAppContext, contentModel, deleteRequest.isChildContent());
        }

        //delete or update child items
        if (ContentHandler.hasChildren(contentModel.getLocalData())) {
            ContentHandler.deleteAllChild(mAppContext, contentModel, deleteRequest.isChildContent());
        }

        //delete or update root item
        ContentHandler.deleteOrUpdateContent(contentModel, false, deleteRequest.isChildContent());

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<ContentListing> getContentListing(ContentListingCriteria contentListingCriteria) {
        HashMap params = new HashMap();
        params.put("criteria", GsonUtil.toJson(contentListingCriteria));
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));
        String methodName = "getContentListing@ContentServiceImpl";

        String jsonStr = null;

        ContentListingModel contentListingModelInDB = ContentListingModel.find(mAppContext.getDBSession(), contentListingCriteria);
        if (contentListingModelInDB != null) {
            jsonStr = contentListingModelInDB.getJson();
            if (DateUtil.getEpochDiff(contentListingModelInDB.getExpiryTime()) > 0) {
                ContentHandler.refreshContentListingFromServer(mAppContext, configService, contentListingCriteria, ContentHandler.getCurrentProfile(userService), mAppContext.getDeviceInfo().getDeviceID());
            }
        }

        if (jsonStr == null) {
            jsonStr = ContentHandler.fetchContentListingFromServer(mAppContext, configService, contentListingCriteria, ContentHandler.getCurrentProfile(userService), mAppContext.getDeviceInfo().getDeviceID());
        }

        if (jsonStr != null) {
            ContentListing contentListing = ContentHandler.getContentListingResult(mAppContext.getDBSession(), contentListingCriteria, jsonStr);
            if (contentListing != null) {
                GenieResponse<ContentListing> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                response.setResult(contentListing);
                TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
                return response;
            }
        }
        GenieResponse response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.DATA_NOT_FOUND_ERROR, ServiceConstants.ErrorMessage.NO_CONTENT_LISTING_DATA, TAG);
        TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.NO_CONTENT_LISTING_DATA);
        return response;
    }

    @Override
    public GenieResponse<ContentSearchResult> searchContent(ContentSearchCriteria contentSearchCriteria) {
        HashMap params = new HashMap();
        params.put("request", GsonUtil.toJson(contentSearchCriteria));
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));
        String methodName = "searchContent@ContentServiceImpl";

        GenieResponse<ContentSearchResult> response;

        Map<String, Object> requestMap = ContentHandler.getSearchRequest(userService, configService, contentSearchCriteria);

        ContentSearchAPI contentSearchAPI = new ContentSearchAPI(mAppContext, requestMap);
        GenieResponse apiResponse = contentSearchAPI.post();
        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();

            LinkedTreeMap map = GsonUtil.fromJson(body, LinkedTreeMap.class);
            String id = (String) map.get("id");
            LinkedTreeMap responseParams = (LinkedTreeMap) map.get("params");
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");

            String responseMessageId = null;
            if (responseParams.containsKey("resmsgid")) {
                responseMessageId = (String) responseParams.get("resmsgid");
            }

            List<Map<String, Object>> facets = null;
            if (result.containsKey("facets")) {
                facets = (List<Map<String, Object>>) result.get("facets");
            }

            String contentDataList = null;
            if (result.containsKey("content")) {
                contentDataList = GsonUtil.toJson(result.get("content"));
            }

            ContentSearchResult searchResult = new ContentSearchResult();
            searchResult.setId(id);
            searchResult.setResponseMessageId(responseMessageId);
            searchResult.setRequest(requestMap);

            if (!StringUtil.isNullOrEmpty(contentDataList)) {
                Type type = new TypeToken<List<ContentData>>() {
                }.getType();
                List<ContentData> contentData = GsonUtil.getGson().fromJson(contentDataList, type);
                searchResult.setContentDataList(contentData);
                searchResult.setFilterCriteria(ContentHandler.createFilterCriteria(configService, contentSearchCriteria, facets, (Map<String, Object>) requestMap.get("filters")));
            } else {
                searchResult.setContentDataList(new ArrayList<ContentData>());
                searchResult.setFilterCriteria(null);
            }


            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(searchResult);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, (String) apiResponse.getErrorMessages().get(0));
        return response;
    }

    @Override
    public GenieResponse<RecommendedContentResult> getRecommendedContent(RecommendedContentRequest request) {
        HashMap params = new HashMap();
        params.put("request", GsonUtil.toJson(request));
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));
        String methodName = "getRecommendedContents@ContentServiceImpl";

        GenieResponse<RecommendedContentResult> response;
        RecommendedContentAPI recommendedContentAPI = new RecommendedContentAPI(mAppContext, ContentHandler.getRecommendedContentRequest(request, mAppContext.getDeviceInfo().getDeviceID()));
        GenieResponse apiResponse = recommendedContentAPI.post();
        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();

            LinkedTreeMap map = GsonUtil.fromJson(body, LinkedTreeMap.class);
            String id = (String) map.get("id");
            LinkedTreeMap responseParams = (LinkedTreeMap) map.get("params");
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");

            String responseMessageId = null;
            if (responseParams.containsKey("resmsgid")) {
                responseMessageId = (String) responseParams.get("resmsgid");
            }

            String contentDataList = null;
            if (result.containsKey("content")) {
                contentDataList = GsonUtil.toJson(result.get("content"));
            }

            RecommendedContentResult recommendedContentResult = new RecommendedContentResult();
            recommendedContentResult.setId(id);
            recommendedContentResult.setResponseMessageId(responseMessageId);
            if (!StringUtil.isNullOrEmpty(contentDataList)) {
                Type type = new TypeToken<List<ContentData>>() {
                }.getType();
                List<ContentData> contentData = GsonUtil.getGson().fromJson(contentDataList, type);
                recommendedContentResult.setContents(contentData);
            }
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(recommendedContentResult);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, (String) apiResponse.getErrorMessages().get(0));
        return response;
    }

    @Override
    public GenieResponse<RelatedContentResult> getRelatedContent(RelatedContentRequest request) {
        HashMap params = new HashMap();
        params.put("request", GsonUtil.toJson(request));
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));
        String methodName = "getRelatedContent@ContentServiceImpl";

        GenieResponse<RelatedContentResult> response;
        RelatedContentAPI relatedContentAPI = new RelatedContentAPI(mAppContext, ContentHandler.getRelatedContentRequest(userService, request, mAppContext.getDeviceInfo().getDeviceID()));
        GenieResponse apiResponse = relatedContentAPI.post();
        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();

            LinkedTreeMap map = GsonUtil.fromJson(body, LinkedTreeMap.class);
            String id = (String) map.get("id");
            LinkedTreeMap responseParams = (LinkedTreeMap) map.get("params");
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");

            String responseMessageId = null;
            if (responseParams.containsKey("resmsgid")) {
                responseMessageId = (String) responseParams.get("resmsgid");
            }

            List<Map<String, Object>> contentDataList = null;
            if (result.containsKey("content")) {
                contentDataList = (List<Map<String, Object>>) result.get("content");
            }

            List<ContentModel> allLocalContentModel = ContentHandler.getAllLocalContentModel(mAppContext.getDBSession(), null);

            List<Content> contents = new ArrayList<>();
            if (contentDataList != null) {
                for (Map contentDataMap : contentDataList) {
                    ContentModel contentModel = ContentHandler.convertContentMapToModel(mAppContext.getDBSession(), contentDataMap, null);
                    Content content = ContentHandler.convertContentModelToBean(contentModel);

                    if (allLocalContentModel.contains(contentModel)) {
                        content.setAvailableLocally(true);
                    }

                    contents.add(content);
                }
            }

            RelatedContentResult relatedContentResult = new RelatedContentResult();
            relatedContentResult.setId(id);
            relatedContentResult.setResponseMessageId(responseMessageId);
            relatedContentResult.setRelatedContents(contents);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(relatedContentResult);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, (String) apiResponse.getErrorMessages().get(0));
        return response;
    }

    @Override
    public GenieResponse<List<Content>> nextContent(List<String> contentIdentifiers) {

        String methodName = "nextContent@ContentServiceImpl";
        HashMap<String, Object> params = new HashMap<>();
        params.put("contentIdentifiers", GsonUtil.toJson(contentIdentifiers));
        params.put("logLevel", "2");

        List<Content> contentList = new ArrayList<>();

        try {
            List<String> contentsKeyList = new ArrayList<>();
            List<String> parentChildRelation = new ArrayList<>();
            String key = null;

            ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), contentIdentifiers.get(0));

            Stack<ContentModel> stack = new Stack<>();
            stack.push(contentModel);

            ContentModel node;
            while (!stack.isEmpty()) {
                node = stack.pop();
                if (ContentHandler.hasChildren(node.getLocalData())) {
                    List<ContentModel> childContents = ContentHandler.getSortedChildrenList(mAppContext.getDBSession(), node.getLocalData(), ContentConstants.ChildContents.ALL);
                    stack.addAll(childContents);

                    for (ContentModel c : childContents) {
                        parentChildRelation.add(node.getIdentifier() + "/" + c.getIdentifier());
                    }
                }

                if (StringUtil.isNullOrEmpty(key)) {
                    key = node.getIdentifier();

                    // First content
//                contents.put(key, node);

                } else {
                    String tempKey;

                    for (int i = key.split("/").length - 1; i >= 0; i--) {
                        String immediateParent = key.split("/")[i];

                        if (parentChildRelation.contains(immediateParent + "/" + node.getIdentifier())) {
                            break;
                        } else {
                            key = key.substring(0, key.lastIndexOf("/"));
                        }
                    }

                    if (ContentType.COLLECTION.getValue().equalsIgnoreCase(node.getContentType())
                            || ContentType.TEXTBOOK.getValue().equalsIgnoreCase(node.getContentType())
                            || ContentType.TEXTBOOK_UNIT.getValue().equalsIgnoreCase(node.getContentType())) {
                        key = key + "/" + node.getIdentifier();

                    } else {
                        tempKey = key + "/" + node.getIdentifier();

                        contentsKeyList.add(tempKey);
                    }
                }
            }

            String currentIdentifiers = null;
            for (String identifier : contentIdentifiers) {
                if (StringUtil.isNullOrEmpty(currentIdentifiers)) {
                    currentIdentifiers = identifier;
                } else {
                    currentIdentifiers = currentIdentifiers + "/" + identifier;
                }
            }

            int indexOfCurrentContentIdentifier = contentsKeyList.indexOf(currentIdentifiers);
            String nextContentIdentifier = null;
            if (indexOfCurrentContentIdentifier > 0) {
                nextContentIdentifier = contentsKeyList.get(indexOfCurrentContentIdentifier - 1);
            }

            if (!StringUtil.isNullOrEmpty(nextContentIdentifier)) {

                String nextContentIdentifierList[] = nextContentIdentifier.split("/");
                for (String identifier : nextContentIdentifierList) {
                    ContentModel nextContentModel = ContentModel.find(mAppContext.getDBSession(), identifier);

                    Content content = ContentHandler.convertContentModelToBean(nextContentModel);
                    contentList.add(content);
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, "" + e.getMessage());
        }

        GenieResponse<List<Content>> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentList);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<Void> importEcar(EcarImportRequest importRequest) {

        String methodName = "importEcar@ContentServiceImpl";
        HashMap<String, Object> params = new HashMap<>();
        params.put("importContent", importRequest.getSourceFilePath());
        params.put("isChildContent", importRequest.isChildContent());
        params.put("logLevel", "2");

        GenieResponse<Void> response;

        EventPublisher.postContentImportStatus(new ContentImportResponse(null, 1, importRequest.getSourceFilePath()));

        if (!FileUtil.doesFileExists(importRequest.getSourceFilePath())) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.ECAR_NOT_FOUND, ServiceConstants.ErrorMessage.FILE_DOESNT_EXIST, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.FILE_DOESNT_EXIST);
            return response;
        }

        String ext = FileUtil.getFileExtension(importRequest.getSourceFilePath());
        if (!ServiceConstants.FileExtension.CONTENT.equals(ext)) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, ServiceConstants.ErrorMessage.UNSUPPORTED_FILE, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNSUPPORTED_FILE);
            return response;
        } else {
            ImportContext importContext = new ImportContext(importRequest.isChildContent(), importRequest.getSourceFilePath(), new File(importRequest.getDestinationFolder()));

            buildInitiateEvent();

            IChainable importContentSteps = ContentImportStep.initImportContent();
            importContentSteps.then(new DeviceMemoryCheck())
                    .then(new ExtractEcar())
                    .then(new ValidateEcar())
                    .then(new ExtractPayloads())
                    .then(new EcarCleanUp())
                    .then(new AddGeTransferContentImportEvent());

            response = importContentSteps.execute(mAppContext, importContext);
            if (response.getStatus()) {
                String identifier = importContext.getIdentifiers() != null ? importContext.getIdentifiers().get(0) : "";
                buildSuccessEvent(identifier);
                TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);

                EventPublisher.postContentImportStatus(new ContentImportResponse(identifier, 2, importRequest.getSourceFilePath()));
            }

            return response;
        }
    }

    @Override
    public GenieResponse<ContentImportResponse> getImportStatus(String identifier) {
        String methodName = "getImportStatus@ContentServiceImpl";
        HashMap params = new HashMap<>();
        params.put("identifier", identifier);
        params.put("logLevel", "2");
        DownloadRequest request = downloadService.getDownloadRequest(identifier);
        int status = -1;
        if (request != null) {
            status = request.getDownloadId() == -1 ? 0 : 1;
        }
        ContentImportResponse contentImportResponse = new ContentImportResponse(identifier, status, null);
        GenieResponse<ContentImportResponse> response = GenieResponseBuilder.getSuccessResponse("", ContentImportResponse.class);
        response.setResult(contentImportResponse);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<Void> importContent(ContentImportRequest importRequest) {

        String methodName = "importContent@ContentServiceImpl";
        HashMap params = new HashMap<>();
        params.put("request", GsonUtil.toJson(importRequest));
        params.put("logLevel", "2");

        GenieResponse<Void> response;

        ContentSearchAPI contentSearchAPI = new ContentSearchAPI(mAppContext, ContentHandler.getSearchRequest(importRequest.getContentIds()));
        GenieResponse apiResponse = contentSearchAPI.post();
        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();

            LinkedTreeMap map = GsonUtil.fromJson(body, LinkedTreeMap.class);
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");

            List<Map<String, Object>> contentDataList = null;
            if (result.containsKey("content")) {
                contentDataList = (List<Map<String, Object>>) result.get("content");
            }

            if (contentDataList != null) {
                DownloadRequest[] downloadRequests = new DownloadRequest[contentDataList.size()];
                for (int i = 0; i < contentDataList.size(); i++) {
                    Map dataMap = contentDataList.get(i);
                    String downloadUrl = ContentHandler.getDownloadUrl(dataMap);
                    if (downloadUrl != null) {
                        downloadUrl = downloadUrl.trim();
                    }

                    if (!StringUtil.isNullOrEmpty(downloadUrl) && ServiceConstants.FileExtension.CONTENT.equalsIgnoreCase(FileUtil.getFileExtension(downloadUrl))) {
                        String contentIdentifier = ContentHandler.readIdentifier(dataMap);
                        DownloadRequest downloadRequest = new DownloadRequest(contentIdentifier, downloadUrl,
                                ContentConstants.MimeType.ECAR, importRequest.getDestinationFolder(), importRequest.isChildContent());
                        downloadRequest.setCorrelationData(importRequest.getCorrelationData());
                        downloadRequest.setProcessorClass("org.ekstep.genieservices.commons.download.ContentImportService");
                        downloadRequests[i] = downloadRequest;
                    }
                }

                downloadService.enqueue(downloadRequests);
            }
        }
        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);

        return response;
    }

    @Override
    public GenieResponse<Void> cancelDownload(String identifier) {
        String methodName = "cancelDownload@ContentServiceImpl";
        HashMap params = new HashMap<>();
        params.put("identifier", identifier);
        params.put("logLevel", "2");
        downloadService.cancel(identifier);
        GenieResponse response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    private void buildInitiateEvent() {
        GEInteract geInteract = new GEInteract.Builder(new GameData(mAppContext.getParams().getGid(), mAppContext.getParams().getVersionName())).
                stageId(ServiceConstants.Telemetry.CONTENT_IMPORT_STAGE_ID).
                subType(ServiceConstants.Telemetry.CONTENT_IMPORT_INITIATED_SUB_TYPE).
                interActionType(InteractionType.TOUCH).
                build();
        TelemetryLogger.log(geInteract);
    }

    private void buildSuccessEvent(String identifier) {
        GEInteract geInteract = new GEInteract.Builder(new GameData(mAppContext.getParams().getGid(), mAppContext.getParams().getVersionName())).
                stageId(ServiceConstants.Telemetry.CONTENT_IMPORT_STAGE_ID).
                subType(ServiceConstants.Telemetry.CONTENT_IMPORT_SUCCESS_SUB_TYPE).
                interActionType(InteractionType.OTHER).
                id(identifier).
                build();
        TelemetryLogger.log(geInteract);
    }

    @Override
    public GenieResponse<ContentExportResponse> exportContent(ContentExportRequest contentExportRequest) {
        if (contentExportRequest.getContentIds() == null && contentExportRequest.getContentIds().isEmpty()) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, ServiceConstants.ErrorMessage.NO_CONTENT_TO_EXPORT, TAG);
        }

        File destinationFolder = new File(contentExportRequest.getDestinationFolder());
        List<ContentModel> contentModelsToExport = ContentHandler.getContentModelToExport(mAppContext.getDBSession(), contentExportRequest.getContentIds());
        contentModelsToExport = ContentHandler.deDupe(contentModelsToExport);

        String fileName = ContentHandler.getExportedFileName(contentModelsToExport);
        File ecarFile = FileUtil.getTempLocation(destinationFolder, fileName);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(GETransferEventKnowStructure.CONTENT_ITEMS_KEY, contentModelsToExport);

        ImportContext importContext = new ImportContext(metadata, destinationFolder, ecarFile);

        CleanTempLoc cleanTempLoc = new CleanTempLoc();
        cleanTempLoc.then(new CreateTempLoc())
                .then(new CreateContentExportManifest(contentModelsToExport))
                .then(new WriteManifest())
                .then(new CompressContent(contentModelsToExport))
                .then(new org.ekstep.genieservices.content.chained.export.DeviceMemoryCheck())
                .then(new CopyAsset(contentModelsToExport))
                .then(new EcarBundle())
                .then(new DeleteTemporaryEcar())
                .then(new AddGeTransferContentExportEvent());

        return cleanTempLoc.execute(mAppContext, importContext);
    }

}