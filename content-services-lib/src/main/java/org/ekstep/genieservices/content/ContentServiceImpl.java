package org.ekstep.genieservices.content;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAuthSession;
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
import org.ekstep.genieservices.commons.bean.ContentDelete;
import org.ekstep.genieservices.commons.bean.ContentDeleteRequest;
import org.ekstep.genieservices.commons.bean.ContentDeleteResponse;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentExportRequest;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.ContentFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentImport;
import org.ekstep.genieservices.commons.bean.ContentImportRequest;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.ContentListing;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.bean.ContentMoveRequest;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.ContentSpaceUsageSummaryRequest;
import org.ekstep.genieservices.commons.bean.ContentSpaceUsageSummaryResponse;
import org.ekstep.genieservices.commons.bean.ContentSwitchRequest;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.FlagContentRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.HierarchyInfo;
import org.ekstep.genieservices.commons.bean.MoveContentResponse;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RecommendedContentResult;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;
import org.ekstep.genieservices.commons.bean.ScanStorageRequest;
import org.ekstep.genieservices.commons.bean.ScanStorageResponse;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.SunbirdContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.SunbirdContentSearchResult;
import org.ekstep.genieservices.commons.bean.SwitchContentResponse;
import org.ekstep.genieservices.commons.bean.enums.ContentDeleteStatus;
import org.ekstep.genieservices.commons.bean.enums.ContentImportStatus;
import org.ekstep.genieservices.commons.bean.enums.DownloadAction;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.bean.enums.ScanStorageStatus;
import org.ekstep.genieservices.commons.bean.telemetry.Interact;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.bean.ExportContentContext;
import org.ekstep.genieservices.content.bean.ImportContentContext;
import org.ekstep.genieservices.content.bean.MoveContentContext;
import org.ekstep.genieservices.content.bean.SwitchContentContext;
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
import org.ekstep.genieservices.content.chained.imports.CreateContentImportManifest;
import org.ekstep.genieservices.content.chained.imports.DeviceMemoryCheck;
import org.ekstep.genieservices.content.chained.imports.EcarCleanUp;
import org.ekstep.genieservices.content.chained.imports.ExtractEcar;
import org.ekstep.genieservices.content.chained.imports.ExtractPayloads;
import org.ekstep.genieservices.content.chained.imports.UpdateSizeOnDevice;
import org.ekstep.genieservices.content.chained.imports.ValidateEcar;
import org.ekstep.genieservices.content.chained.move.CopyContentFromSourceToDestination;
import org.ekstep.genieservices.content.chained.move.DeleteDestinationFolder;
import org.ekstep.genieservices.content.chained.move.DeleteSourceFolder;
import org.ekstep.genieservices.content.chained.move.DuplicateContentCheck;
import org.ekstep.genieservices.content.chained.move.StoreDestinationContentInDB;
import org.ekstep.genieservices.content.chained.move.UpdateSourceContentPathInDB;
import org.ekstep.genieservices.content.chained.move.ValidateDestinationContent;
import org.ekstep.genieservices.content.chained.move.ValidateDestinationFolder;
import org.ekstep.genieservices.content.db.model.ContentListingModel;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.network.ContentSearchAPI;
import org.ekstep.genieservices.content.network.FlagContentAPI;
import org.ekstep.genieservices.content.network.RecommendedContentAPI;
import org.ekstep.genieservices.content.network.RelatedContentAPI;
import org.ekstep.genieservices.eventbus.EventBus;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private IAuthSession<Session> authSession;

    public ContentServiceImpl(AppContext appContext, IUserService userService, IContentFeedbackService contentFeedbackService,
                              IConfigService configService, IDownloadService downloadService, IAuthSession<Session> authSession) {
        super(appContext);

        this.userService = userService;
        this.contentFeedbackService = contentFeedbackService;
        this.configService = configService;
        this.downloadService = downloadService;
        this.authSession = authSession;
    }

    private Map<String, String> getCustomHeaders(Session authSession) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Authenticated-User-Token", authSession.getAccessToken());
        return headers;
    }

    private <T> GenieResponse<T> isValidAuthSession(String methodName, Map<String, Object> params) {
        if (authSession == null || authSession.getSessionData() == null) {
            GenieResponse<T> response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.AUTH_SESSION,
                    ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN, TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN);
            return response;
        }

        return null;
    }

    @Override
    public GenieResponse<Content> getContentDetails(ContentDetailsRequest contentDetailsRequest) {
        String methodName = "getContentDetails@ContentServiceImpl";
        Map<String, Object> params = new HashMap<>();
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
        } else if (contentDetailsRequest.isRefreshContentDetails()) {
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
        Map<String, Object> params = new HashMap<>();
        params.put("criteria", GsonUtil.toJson(criteria));

        GenieResponse<List<Content>> response;

        List<ContentModel> contentModelListInDB = ContentHandler.getAllLocalContent(mAppContext.getDBSession(), criteria);

        List<Content> contentList = new ArrayList<>();
        for (ContentModel contentModel : contentModelListInDB) {
            Content c = ContentHandler.convertContentModelToBean(contentModel);

            if (criteria != null && criteria.attachFeedback()) {
                c.setContentFeedback(ContentHandler.getContentFeedback(contentFeedbackService, c.getIdentifier(), criteria.getUid()));
            }
            if (criteria != null && criteria.attachContentAccess()) {
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
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(childContentRequest));

        GenieResponse<Content> response;
        ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), childContentRequest.getContentId());
        if (contentModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_DATA_FOUND, ServiceConstants.ErrorMessage.CONTENT_NOT_FOUND + childContentRequest.getContentId(), TAG);
            return response;
        }

        List<HierarchyInfo> hierarchyInfoList = childContentRequest.getHierarchyInfo();
        if (hierarchyInfoList == null) {
            hierarchyInfoList = new ArrayList<>();
        } else if (!hierarchyInfoList.isEmpty()) {
            /* If the nested collection is C/C1/C11 and somebody is asking for C1's children they would be sending the hierarchy of C1 which is C/C1.
             * We are removing the last element here so that the further processing can continue to add it back
             * In other words, the checkAndFetchChildrenOfContent method assumes that the sourceInfoList is the parent's hierarchy info
             */
            if (hierarchyInfoList.get(hierarchyInfoList.size() - 1).getIdentifier().equalsIgnoreCase(childContentRequest.getContentId())) {
                hierarchyInfoList.remove(hierarchyInfoList.size() - 1);
            }
        }

        //check and fetch all children of this content
        Content content = checkAndFetchChildrenOfContent(contentModel, hierarchyInfoList, 0, childContentRequest.getLevel());

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(content);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    private Content checkAndFetchChildrenOfContent(ContentModel contentModel, List<HierarchyInfo> sourceInfoList, int currentLevel, int level) {
        Content content = ContentHandler.convertContentModelToBean(contentModel);

        // check if the content model has immediate children
        List<ContentModel> contentModelList = ContentHandler.getSortedChildrenList(mAppContext.getDBSession(), contentModel.getLocalData(), ContentConstants.ChildContents.ALL);
        if (contentModelList.size() > 0) {
            List<HierarchyInfo> hierarchyInfoList = new ArrayList<>(sourceInfoList);
            hierarchyInfoList.add(new HierarchyInfo(contentModel.getIdentifier(), contentModel.getContentType()));
            content.setHierarchyInfo(hierarchyInfoList);
            if (level == -1 || currentLevel < level) {
                List<Content> childContents = new ArrayList<>();
                for (ContentModel perContentModel : contentModelList) {
                    Content iteratedContent = checkAndFetchChildrenOfContent(perContentModel, hierarchyInfoList, currentLevel + 1, level);
                    childContents.add(iteratedContent);
                }
                //add children to the main content
                content.setChildren(childContents);
            }
        } else {
            content.setHierarchyInfo(sourceInfoList);
        }
        return content;
    }

    @Override
    public GenieResponse<List<ContentDeleteResponse>> deleteContent(ContentDeleteRequest deleteRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(deleteRequest));
        String methodName = "deleteContent@ContentServiceImpl";

        List<ContentDeleteResponse> contentDeleteResponseList = new ArrayList<>();
        for (ContentDelete contentDelete : deleteRequest.getContentDeleteList()) {
            ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), contentDelete.getContentId());

            if (contentModel == null) {
                contentDeleteResponseList.add(new ContentDeleteResponse(contentDelete.getContentId(), ContentDeleteStatus.NOT_FOUND));
            } else {
                contentDeleteResponseList.add(new ContentDeleteResponse(contentDelete.getContentId(), ContentDeleteStatus.DELETED_SUCCESSFULLY));

                //delete or update pre-requisites
                if (ContentHandler.hasPreRequisites(contentModel.getLocalData())) {
                    ContentHandler.deleteAllPreRequisites(mAppContext, contentModel, contentDelete.isChildContent());
                }

                //delete or update child items
                if (ContentHandler.hasChildren(contentModel.getLocalData())) {
                    ContentHandler.deleteAllChild(mAppContext, contentModel, contentDelete.isChildContent());
                }

                //delete or update root item
                ContentHandler.deleteOrUpdateContent(mAppContext, contentModel, false, contentDelete.isChildContent());
            }
        }

        // Update size on device value in DB after content deletion.
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentHandler.updateSizeOnDevice(mAppContext);
            }
        }).start();

        GenieResponse<List<ContentDeleteResponse>> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentDeleteResponseList);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<ContentListing> getContentListing(ContentListingCriteria contentListingCriteria) {
        Map<String, Object> params = new HashMap<>();
        params.put("criteria", GsonUtil.toJson(contentListingCriteria));
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));
        String methodName = "getContentListing@ContentServiceImpl";

        GenieResponse<ContentListing> response;
        String jsonStr = null;

        ContentListingModel contentListingModelInDB = ContentListingModel.find(mAppContext.getDBSession(), contentListingCriteria);
        if (contentListingModelInDB != null) {
            jsonStr = contentListingModelInDB.getJson();
            if (DateUtil.getEpochDiff(contentListingModelInDB.getExpiryTime()) > 0) {
                ContentHandler.refreshContentListingFromServer(mAppContext, configService, contentListingCriteria, mAppContext.getDeviceInfo().getDeviceID());
            }
        }

        if (jsonStr == null) {
            jsonStr = ContentHandler.fetchContentListingFromServer(mAppContext, configService, contentListingCriteria, mAppContext.getDeviceInfo().getDeviceID());
        }

        if (jsonStr != null) {
            ContentListing contentListing = ContentHandler.getContentListingResult(contentListingCriteria, jsonStr);
            if (contentListing != null) {
                response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                response.setResult(contentListing);
                TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
                return response;
            }
        }

        response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.DATA_NOT_FOUND_ERROR, ServiceConstants.ErrorMessage.NO_CONTENT_LISTING_DATA, TAG);
        TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.NO_CONTENT_LISTING_DATA);
        return response;
    }

    @Override
    public GenieResponse<ContentSearchResult> searchContent(ContentSearchCriteria contentSearchCriteria) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(contentSearchCriteria));
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));
        String methodName = "searchContent@ContentServiceImpl";

        GenieResponse<ContentSearchResult> response;

        Map<String, Object> requestMap = ContentHandler.getSearchContentRequest(mAppContext, configService, contentSearchCriteria);

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
        Map<String, Object> params = new HashMap<>();
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
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(request));
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));
        String methodName = "getRelatedContent@ContentServiceImpl";

        GenieResponse<RelatedContentResult> response;
        RelatedContentAPI relatedContentAPI = new RelatedContentAPI(mAppContext, ContentHandler.getRelatedContentRequest(request, mAppContext.getDeviceInfo().getDeviceID()));
        GenieResponse apiResponse = relatedContentAPI.post();
        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();

            LinkedTreeMap map = GsonUtil.fromJson(body, LinkedTreeMap.class);
            String id = (String) map.get("id");
            LinkedTreeMap responseParams = (LinkedTreeMap) map.get("params");
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");

            String responseMessageId = null;
            if (responseParams != null && responseParams.containsKey("resmsgid")) {
                responseMessageId = (String) responseParams.get("resmsgid");
            }

            String contentDataList = null;
            if (result != null && result.containsKey("content")) {
                contentDataList = GsonUtil.toJson(result.get("content"));
            }

            RelatedContentResult relatedContentResult = new RelatedContentResult();
            relatedContentResult.setId(id);
            relatedContentResult.setResponseMessageId(responseMessageId);
            if (!StringUtil.isNullOrEmpty(contentDataList)) {
                Type type = new TypeToken<List<ContentData>>() {
                }.getType();
                List<ContentData> contentData = GsonUtil.getGson().fromJson(contentDataList, type);
                relatedContentResult.setContents(contentData);
            }
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
    public GenieResponse<Content> nextContent(List<HierarchyInfo> hierarchyInfo, String currentContentIdentifier) {
        String methodName = "nextContent@ContentServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("contentIdentifiers", GsonUtil.toJson(hierarchyInfo));
        params.put("logLevel", "2");

        List<HierarchyInfo> nextContentHierarchyList = new ArrayList<>();
        Content nextContent = null;
        try {
            List<String> contentsKeyList = new ArrayList<>();
            List<String> parentChildRelation = new ArrayList<>();
            String key = null;

            ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), hierarchyInfo.get(0).getIdentifier());

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

                    if (ContentConstants.MimeType.COLLECTION.equals(node.getMimeType())) {
                        key = key + "/" + node.getIdentifier();

                    } else {
                        tempKey = key + "/" + node.getIdentifier();

                        contentsKeyList.add(tempKey);
                    }
                }
            }

            String currentIdentifiers = null;
            for (HierarchyInfo hierarchyItem : hierarchyInfo) {
                if (StringUtil.isNullOrEmpty(currentIdentifiers)) {
                    currentIdentifiers = hierarchyItem.getIdentifier();
                } else {
                    currentIdentifiers = currentIdentifiers + "/" + hierarchyItem.getIdentifier();
                }
            }
            currentIdentifiers += "/" + currentContentIdentifier;

            int indexOfCurrentContentIdentifier = contentsKeyList.indexOf(currentIdentifiers);
            String nextContentIdentifier = null;
            if (indexOfCurrentContentIdentifier > 0) {
                nextContentIdentifier = contentsKeyList.get(indexOfCurrentContentIdentifier - 1);
            }

            if (!StringUtil.isNullOrEmpty(nextContentIdentifier)) {

                String nextContentIdentifierList[] = nextContentIdentifier.split("/");
                int idCount = nextContentIdentifierList.length;
                for (int i = 0; i < (idCount - 1); i++) {
                    ContentModel contentModelObj = ContentModel.find(mAppContext.getDBSession(), nextContentIdentifierList[i]);
                    nextContentHierarchyList.add(new HierarchyInfo(contentModelObj.getIdentifier(), contentModelObj.getContentType()));
                }
                ContentModel nextContentModel = ContentModel.find(mAppContext.getDBSession(), nextContentIdentifierList[idCount - 1]);
                nextContent = ContentHandler.convertContentModelToBean(nextContentModel);
                nextContent.setHierarchyInfo(nextContentHierarchyList);
            }
        } catch (Exception e) {
            Logger.e(TAG, "" + e.getMessage());
        }

        GenieResponse<Content> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(nextContent);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<List<ContentImportResponse>> importEcar(EcarImportRequest importRequest) {
        String methodName = "importEcar@ContentServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("importContent", importRequest.getSourceFilePath());
        params.put("isChildContent", importRequest.isChildContent());
        params.put("logLevel", "2");

        GenieResponse<List<ContentImportResponse>> response;

        // TODO: 7/21/2017 - Needs to move in ValidateEcar task.
        EventBus.postEvent(new ContentImportResponse(null, ContentImportStatus.IMPORT_STARTED));

        if (!FileUtil.doesFileExists(importRequest.getSourceFilePath())) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.ECAR_NOT_FOUND, ServiceConstants.ErrorMessage.FILE_DOES_NOT_EXIST, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.FILE_DOES_NOT_EXIST);
            return response;
        }

        String ext = FileUtil.getFileExtension(importRequest.getSourceFilePath());
        if (!ServiceConstants.FileExtension.CONTENT.equals(ext)) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, ServiceConstants.ErrorMessage.UNSUPPORTED_FILE, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNSUPPORTED_FILE);
            return response;
        } else {
            buildInitiateEvent();

            File tmpLocation = FileUtil.getTempLocation(new File(importRequest.getDestinationFolder()));
            ImportContentContext importContentContext = new ImportContentContext(importRequest.isChildContent(), importRequest.getSourceFilePath(), importRequest.getDestinationFolder());
            IChainable<List<ContentImportResponse>, ImportContentContext> deviceMemoryCheck = new DeviceMemoryCheck();
            deviceMemoryCheck.then(new ExtractEcar(tmpLocation))
                    .then(new ValidateEcar(tmpLocation))
                    .then(new ExtractPayloads(tmpLocation))
                    .then(new CreateContentImportManifest())
                    .then(new EcarCleanUp(tmpLocation))
                    .then(new UpdateSizeOnDevice())
                    .then(new AddGeTransferContentImportEvent());
            response = deviceMemoryCheck.execute(mAppContext, importContentContext);

            if (response.getStatus()) {
                String identifier = importContentContext.getIdentifiers() != null && !importContentContext.getIdentifiers().isEmpty() ? importContentContext.getIdentifiers().get(0) : "";
                buildSuccessEvent(identifier);
                TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);

                EventBus.postEvent(new ContentImportResponse(identifier, ContentImportStatus.IMPORT_COMPLETED));

                //store the folder's last modified time
                File contentRootFolder = FileUtil.getContentRootDir(new File(importContentContext.getDestinationFolder()));

                if (FileUtil.doesFileExists(contentRootFolder.getPath())) {
                    mAppContext.getKeyValueStore().putLong(ServiceConstants.PreferenceKey.KEY_LAST_MODIFIED, contentRootFolder.lastModified());
                }
            }

            return response;
        }
    }

    @Override
    public GenieResponse<ContentImportResponse> getImportStatus(String contentId) {
        String methodName = "getImportStatus@ContentServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("identifier", contentId);
        params.put("logLevel", "2");

        DownloadRequest request = downloadService.getDownloadRequest(contentId);
        ContentImportStatus status = ContentImportStatus.NOT_FOUND;
        if (request != null) {
            status = request.getDownloadId() == -1 ? ContentImportStatus.ENQUEUED_FOR_DOWNLOAD : ContentImportStatus.DOWNLOAD_STARTED;
        } else {
            ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), contentId);
            if (contentModel != null) {
                status = ContentImportStatus.IMPORT_COMPLETED;
            }
        }

        ContentImportResponse contentImportResponse = new ContentImportResponse(contentId, status);
        GenieResponse<ContentImportResponse> response = GenieResponseBuilder.getSuccessResponse("", ContentImportResponse.class);
        response.setResult(contentImportResponse);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<List<ContentImportResponse>> getImportStatus(List<String> contentIdList) {
        String methodName = "getImportStatus@ContentServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("identifierList", GsonUtil.toJson(contentIdList));
        params.put("logLevel", "2");

        List<ContentImportResponse> contentImportResponseList = new ArrayList<>();
        for (String contentId : contentIdList) {
            DownloadRequest request = downloadService.getDownloadRequest(contentId);
            ContentImportStatus status = ContentImportStatus.NOT_FOUND;
            if (request != null) {
                status = request.getDownloadId() == -1 ? ContentImportStatus.ENQUEUED_FOR_DOWNLOAD : ContentImportStatus.DOWNLOAD_STARTED;
            } else {
                ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), contentId);
                if (contentModel != null) {
                    status = ContentImportStatus.IMPORT_COMPLETED;
                }
            }

            ContentImportResponse contentImportResponse = new ContentImportResponse(contentId, status);
            contentImportResponseList.add(contentImportResponse);
        }


        GenieResponse<List<ContentImportResponse>> response = GenieResponseBuilder.getSuccessResponse("");
        response.setResult(contentImportResponseList);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<List<ContentImportResponse>> importContent(ContentImportRequest importRequest) {
        String methodName = "importContent@ContentServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(importRequest));
        params.put("logLevel", "2");

        GenieResponse<List<ContentImportResponse>> response;
        List<ContentImportResponse> contentImportResponseList = new ArrayList<>();
        Map<String, Object> contentImportMap = importRequest.getContentImportMap();
        Set<String> contentIds = contentImportMap.keySet();

        ContentSearchAPI contentSearchAPI = new ContentSearchAPI(mAppContext, ContentHandler.getSearchRequest(mAppContext, contentIds, importRequest.getContentStatusArray()));
        GenieResponse apiResponse = contentSearchAPI.post();
        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();

            LinkedTreeMap map = GsonUtil.fromJson(body, LinkedTreeMap.class);
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");

            String contents = null;
            if (result.containsKey("content")) {
                contents = GsonUtil.toJson(result.get("content"));
            }

            List<ContentData> contentDataList = null;
            if (!StringUtil.isNullOrEmpty(contents)) {
                Type type = new TypeToken<List<ContentData>>() {
                }.getType();
                contentDataList = GsonUtil.getGson().fromJson(contents, type);
            }

            if (!CollectionUtil.isNullOrEmpty(contentDataList)) {
                List<DownloadRequest> downloadRequestList = new ArrayList<>();
                List<String> requestedContentIdList = new ArrayList<>(contentIds);

                for (String requestedContentId : requestedContentIdList) {
                    int indexOfContentData = contentDataList.indexOf(new ContentData(requestedContentId));
                    if (indexOfContentData != -1) {
                        ContentData contentData = contentDataList.get(indexOfContentData);
                        String contentId = contentData.getIdentifier();
                        String downloadUrl = ContentHandler.getDownloadUrl(contentData);
                        ContentImportStatus status = ContentImportStatus.NOT_FOUND;

                        if (!StringUtil.isNullOrEmpty(downloadUrl) && ServiceConstants.FileExtension.CONTENT.equalsIgnoreCase(FileUtil.getFileExtension(downloadUrl))) {
                            status = ContentImportStatus.ENQUEUED_FOR_DOWNLOAD;
                            ContentImport contentImport = (ContentImport) contentImportMap.get(contentId);
                            DownloadRequest downloadRequest = new DownloadRequest(contentId, downloadUrl, ContentConstants.MimeType.ECAR, contentImport.getDestinationFolder(), contentImport.isChildContent());
                            downloadRequest.setFilename(contentId + "." + ServiceConstants.FileExtension.CONTENT);
                            downloadRequest.setCorrelationData(contentImport.getCorrelationData());
                            downloadRequest.setProcessorClass("org.ekstep.genieservices.commons.download.ContentImportService");

                            downloadRequestList.add(downloadRequest);
                        }

                        contentIds.remove(contentId);
                        contentImportResponseList.add(new ContentImportResponse(contentId, status));
                    }
                }

                if (downloadRequestList.size() > 0) {
                    DownloadRequest[] downloadRequestArray = downloadRequestList.toArray(new DownloadRequest[downloadRequestList.size()]);
                    downloadService.enqueue(downloadRequestArray);
                }
            }
        }

        for (String contentId : contentIds) {
            contentImportResponseList.add(new ContentImportResponse(contentId, ContentImportStatus.NOT_FOUND));
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentImportResponseList);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);

        return response;
    }

    @Override
    public GenieResponse<Void> cancelDownload(String contentId) {
        String methodName = "cancelDownload@ContentServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("identifier", contentId);
        params.put("logLevel", "2");
        downloadService.cancel(contentId);
        GenieResponse<Void> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    private void buildInitiateEvent() {
        Interact interact = new Interact.Builder()
                .environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT)
                .pageId(ServiceConstants.Telemetry.CONTENT_IMPORT_STAGE_ID)
                .subType(ServiceConstants.Telemetry.CONTENT_IMPORT_INITIATED_SUB_TYPE)
                .interactionType(InteractionType.TOUCH)
                .objectType(ServiceConstants.Telemetry.OBJECT_TYPE_CONTENT)
                .resourceId(ServiceConstants.Telemetry.CONTENT_IMPORT_STAGE_ID)
                .build();
        TelemetryLogger.log(interact);
    }

    private void buildSuccessEvent(String identifier) {
        Interact interact = new Interact.Builder()
                .environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT)
                .pageId(ServiceConstants.Telemetry.CONTENT_IMPORT_STAGE_ID)
                .subType(ServiceConstants.Telemetry.CONTENT_IMPORT_SUCCESS_SUB_TYPE)
                .interactionType(InteractionType.OTHER)
                .objectId(identifier)
                .objectType(ServiceConstants.Telemetry.OBJECT_TYPE_CONTENT)
                .resourceId(ServiceConstants.Telemetry.CONTENT_IMPORT_STAGE_ID)
                .build();
        TelemetryLogger.log(interact);
    }

    @Override
    public GenieResponse<ContentExportResponse> exportContent(ContentExportRequest contentExportRequest) {
        if (CollectionUtil.isNullOrEmpty(contentExportRequest.getContentIds())) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, ServiceConstants.ErrorMessage.NO_CONTENT_TO_EXPORT, TAG);
        }

        File destinationFolder = new File(contentExportRequest.getDestinationFolder());
        List<ContentModel> contentModelsToExport = ContentHandler.getContentModelToExport(mAppContext.getDBSession(), contentExportRequest.getContentIds());
        contentModelsToExport = ContentHandler.deDupe(contentModelsToExport);

        String fileName = ContentHandler.getExportedFileName(contentModelsToExport);
        File ecarFile = FileUtil.getTempLocation(destinationFolder, fileName);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ServiceConstants.CONTENT_ITEMS_COUNT_KEY, contentModelsToExport.size());

        ExportContentContext exportContentContext = new ExportContentContext(metadata, destinationFolder, ecarFile, contentModelsToExport);

        CleanTempLoc cleanTempLoc = new CleanTempLoc();
        cleanTempLoc.then(new CreateTempLoc())
                .then(new CreateContentExportManifest())
                .then(new WriteManifest())
                .then(new CompressContent())
                .then(new org.ekstep.genieservices.content.chained.export.DeviceMemoryCheck())
                .then(new CopyAsset())
                .then(new EcarBundle())
                .then(new DeleteTemporaryEcar())
                .then(new AddGeTransferContentExportEvent());

        return cleanTempLoc.execute(mAppContext, exportContentContext);
    }

    @Override
    public GenieResponse<Void> setDownloadAction(DownloadAction action) {
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");
        String methodName = "setDownloadAction@ContentServiceImpl";
        mAppContext.getKeyValueStore().putInt(ServiceConstants.PreferenceKey.KEY_DOWNLOAD_STATUS, action.getValue());
        if (action.getValue() == 0) {
            downloadService.resumeDownloads();
        }
        GenieResponse<Void> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;

    }

    @Override
    public GenieResponse<DownloadAction> getDownloadState() {
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");
        String methodName = "getDownloadState@ContentServiceImpl";
        GenieResponse<DownloadAction> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(mAppContext.getKeyValueStore().getInt(ServiceConstants.PreferenceKey.KEY_DOWNLOAD_STATUS, 0) == 0 ? DownloadAction.RESUME : DownloadAction.PAUSE);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<List<MoveContentResponse>> moveContent(ContentMoveRequest contentMoveRequest) {
        File destinationFolder = new File(contentMoveRequest.getDestinationFolder());

        MoveContentContext moveContentContext = new MoveContentContext(contentMoveRequest.getContentIds(), destinationFolder,
                contentMoveRequest.getExistingContentAction(), contentMoveRequest.deleteDestination());

        ValidateDestinationFolder validateDestinationFolder = new ValidateDestinationFolder();

        validateDestinationFolder.then(new DeleteDestinationFolder())
                .then(new org.ekstep.genieservices.content.chained.move.DeviceMemoryCheck())
                .then(new ValidateDestinationContent())
                .then(new DuplicateContentCheck())
                .then(new CopyContentFromSourceToDestination())
                .then(new DeleteSourceFolder()) //TODO : Check if the source has to be deleted or not, if needed, then add a builder method to check
                .then(new UpdateSourceContentPathInDB())
                .then(new StoreDestinationContentInDB());

        return validateDestinationFolder.execute(mAppContext, moveContentContext);
    }

    @Override
    public GenieResponse<List<ScanStorageResponse>> scanStorage(ScanStorageRequest scanStorageRequest) {
        GenieResponse<List<ScanStorageResponse>> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SuccessMessage.SCAN_SUCCESS_NO_CHANGES);

        //get the last modified time from preference
        long storedLastModifiedTime = mAppContext.getKeyValueStore().getLong(ServiceConstants.PreferenceKey.KEY_LAST_MODIFIED, 0);

        //check if folder exists
        if (FileUtil.doesFileExists(scanStorageRequest.getStorageFilePath())) {
            List<String> deletedContentIdentifiers = null;
            List<String> addedContentIdentifiers = null;
            List<ScanStorageResponse> addedOrDeletedIdentifiersList = new ArrayList<>();
            File storageFolder = FileUtil.getContentRootDir(new File(scanStorageRequest.getStorageFilePath()));

            //get last modified time of the folder
            long folderLastModifiedTime = storageFolder.lastModified();

            if (storedLastModifiedTime != folderLastModifiedTime) {
                List<String> foldersList = new ArrayList<>();

                //get all the identifiers from the folder
                String[] folders = storageFolder.list();

                if (folders != null && folders.length > 0) {
                    foldersList = Arrays.asList(folders);
                }

                //get all the identifiers from the db
                List<ContentModel> dbContentModelList = ContentHandler.findAllContent(mAppContext.getDBSession());

                //fetch all identifiers from the content model list
                List<String> dbContentIdentifiers = new ArrayList<>();
                if (!CollectionUtil.isNullOrEmpty(dbContentModelList)) {
                    for (ContentModel contentModel : dbContentModelList) {
                        dbContentIdentifiers.add(contentModel.getIdentifier());
                    }
                }

                if (foldersList.size() > 0 && dbContentIdentifiers.size() > 0) {
                    //deleted contents - if the identifier is present in db-list and not in folder-list
                    deletedContentIdentifiers = getDeletedContents(foldersList, dbContentIdentifiers);

                    //newly added contents - if the identifier is present in folder-list and not in the db-list
                    addedContentIdentifiers = getNewlyAddedContents(foldersList, dbContentIdentifiers);
                }

                if (!CollectionUtil.isNullOrEmpty(deletedContentIdentifiers)) {
                    for (String deletedContent : deletedContentIdentifiers) {
                        addedOrDeletedIdentifiersList.add(new ScanStorageResponse(deletedContent, ScanStorageStatus.DELETED));
                    }
                }

                if (!CollectionUtil.isNullOrEmpty(addedContentIdentifiers)) {
                    // Validate manifest identifiers
                    List<String> validContentIdentifiers = ContentHandler.getValidIdentifiersFromPath(mAppContext, storageFolder, addedContentIdentifiers);

                    if (!CollectionUtil.isNullOrEmpty(validContentIdentifiers)) {
                        for (String addedContent : validContentIdentifiers) {
                            addedOrDeletedIdentifiersList.add(new ScanStorageResponse(addedContent, ScanStorageStatus.ADDED));
                        }
                    }
                }

                if (addedOrDeletedIdentifiersList.size() > 0) {
                    performActionOnContents(addedOrDeletedIdentifiersList, storageFolder);

                    if (storageFolder != null) {
                        mAppContext.getKeyValueStore().putLong(ServiceConstants.PreferenceKey.KEY_LAST_MODIFIED, storageFolder.lastModified());
                    }
                }

                response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SuccessMessage.SCAN_SUCCESS_WITH_CHANGES);
                response.setResult(addedOrDeletedIdentifiersList);
            }
        }

        return response;
    }

    @Override
    public GenieResponse<List<SwitchContentResponse>> switchContent(ContentSwitchRequest contentSwitchRequest) {
        File destinationFolder = new File(contentSwitchRequest.getDestinationFolder());

        SwitchContentContext switchContentContext = new SwitchContentContext(destinationFolder);

        org.ekstep.genieservices.content.chained.switchLocation.ValidateDestinationFolder validateDestinationFolder =
                new org.ekstep.genieservices.content.chained.switchLocation.ValidateDestinationFolder();
        validateDestinationFolder.then(new org.ekstep.genieservices.content.chained.switchLocation.ValidateDestinationFolder())
                .then(new org.ekstep.genieservices.content.chained.switchLocation.ValidateDestinationContent())
                .then(new org.ekstep.genieservices.content.chained.switchLocation.DeleteSourceFolder())
                .then(new org.ekstep.genieservices.content.chained.switchLocation.StoreDestinationContentInDB());

        return validateDestinationFolder.execute(mAppContext, switchContentContext);
    }

    @Override
    public GenieResponse<List<ContentSpaceUsageSummaryResponse>> getContentSpaceUsageSummary(ContentSpaceUsageSummaryRequest contentSpaceUsageSummaryRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");
        String methodName = "getContentSpaceUsageSummary@ContentServiceImpl";

        List<ContentSpaceUsageSummaryResponse> contentSpaceUsageSummaryList = new ArrayList<>();
        for (String path : contentSpaceUsageSummaryRequest.getPaths()) {
            long size = ContentHandler.getUsageSpace(path, mAppContext);
            contentSpaceUsageSummaryList.add(new ContentSpaceUsageSummaryResponse(path, size));
        }

        GenieResponse<List<ContentSpaceUsageSummaryResponse>> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentSpaceUsageSummaryList);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);

        return response;
    }

    /**
     * This method performs necessary action on that particular content based on the scan status of the content.
     *
     * @param scannedIdentifiersList
     * @param storageFolder
     */
    private void performActionOnContents(List<ScanStorageResponse> scannedIdentifiersList, File storageFolder) {
        if (!CollectionUtil.isNullOrEmpty(scannedIdentifiersList)) {
            for (ScanStorageResponse scannedContent : scannedIdentifiersList) {
                if (scannedContent.getStatus().equals(ScanStorageStatus.DELETED)) {
                    ContentHandler.deleteContentsFromDb(mAppContext.getDBSession(), scannedContent.getIdentifier());
                } else if (scannedContent.getStatus().equals(ScanStorageStatus.ADDED)) {
                    ContentHandler.addContentToDb(mAppContext, scannedContent.getIdentifier(), storageFolder, false);
                } else if (scannedContent.getStatus().equals(ScanStorageStatus.UPDATED)) {
                    // TODO: 12/10/17 Yet to be done - In progress
//                    updateIdsList.add(scannedContent.getIdentifier());
                }
            }
        }
    }

    /**
     * This method gives all the identifiers of the contents that are newly added manually, by looking at the identifiers from the db
     *
     * @param foldersList
     * @param contentIdentifiers
     * @return
     */
    private List<String> getNewlyAddedContents(List<String> foldersList, List<String> contentIdentifiers) {
        Set<String> dbContents = new HashSet<>(contentIdentifiers);
        Set<String> folderContents = new HashSet<>(foldersList);
        folderContents.removeAll(dbContents);

        return new ArrayList<>(folderContents);
    }

    /**
     * This method gives all the identifiers of the contents that are deleted manually, by looking at the identifiers from the db
     *
     * @param foldersList
     * @param contentIdentifiers
     * @return
     */
    private List<String> getDeletedContents(List<String> foldersList, List<String> contentIdentifiers) {
        Set<String> dbContents = new HashSet<>(contentIdentifiers);
        Set<String> folderContents = new HashSet<>(foldersList);
        dbContents.removeAll(folderContents);

        return new ArrayList<>(dbContents);
    }

    @Override
    public GenieResponse<SunbirdContentSearchResult> searchSunbirdContent(SunbirdContentSearchCriteria contentSearchCriteria) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(contentSearchCriteria));
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));
        String methodName = "searchSunbirdContent@ContentServiceImpl";

        GenieResponse<SunbirdContentSearchResult> response;

        if (contentSearchCriteria.isOfflineSearch()) {
            Map<String, ContentData> contentDataMap = new HashMap<>();
            Map<String, ContentData> collectionDataMap = new HashMap<>();

            if (!CollectionUtil.isEmpty(contentSearchCriteria.getDialCodes())) {
                for (String dialcode : contentSearchCriteria.getDialCodes()) {

                    String key = ContentHandler.KEY_DB_DIAL_CODES + dialcode;

                    NoSqlModel dialcodeInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
                    if (dialcodeInDB != null && !StringUtil.isNullOrEmpty(dialcodeInDB.getValue())) {
                        Map<String, Object> dialcodeMapping = GsonUtil.fromJson(dialcodeInDB.getValue(), Map.class);

                        String childNode = null;
                        String identifier = (String) dialcodeMapping.get("identifier");
                        if (!contentDataMap.containsKey(identifier)) {
                            ContentModel contentModelInDB = ContentModel.find(mAppContext.getDBSession(), identifier);
                            if (contentModelInDB != null) {
                                childNode = contentModelInDB.getIdentifier();

                                contentDataMap.put(contentModelInDB.getIdentifier(),
                                        GsonUtil.fromJson(contentModelInDB.getLocalData(), ContentData.class));
                            }
                        }

                        List<String> rootNodes = (List) dialcodeMapping.get("rootNodes");
                        for (String rootNodeIdentifier : rootNodes) {
                            ContentData rootContent = null;
                            if (!collectionDataMap.containsKey(rootNodeIdentifier)) {
                                ContentModel rootContentModelInDB = ContentModel.find(mAppContext.getDBSession(), rootNodeIdentifier);
                                if (rootContentModelInDB != null) {
                                    rootContent = GsonUtil.fromJson(rootContentModelInDB.getLocalData(), ContentData.class);
                                }
                            } else {
                                rootContent = GsonUtil.fromJson(String.valueOf(collectionDataMap.get(rootNodeIdentifier)), ContentData.class);
                            }

                            if (rootContent != null) {
                                if (!StringUtil.isNullOrEmpty(childNode)) {
                                    List<String> childNodes = rootContent.getChildNodes();
                                    if (CollectionUtil.isNullOrEmpty(childNodes)) {
                                        childNodes = new ArrayList<>();
                                    }

                                    if (!childNodes.contains(childNode)) {
                                        childNodes.add(childNode);
                                        rootContent.setChildNodes(childNodes);
                                    }
                                }
                                collectionDataMap.put(rootNodeIdentifier, rootContent);
                            }
                        }
                    }
                }
            }

            if (contentDataMap.isEmpty() && collectionDataMap.isEmpty()) {
                response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.DATA_NOT_FOUND_ERROR,
                        ServiceConstants.ErrorMessage.NO_CONTENT_LISTING_DATA, TAG);
                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
            } else {
                SunbirdContentSearchResult searchResult = new SunbirdContentSearchResult();
                if (!contentDataMap.isEmpty()) {
                    List<ContentData> contentDataList = new ArrayList<>(contentDataMap.values());
                    searchResult.setContentDataList(contentDataList);
                }
                if (!collectionDataMap.isEmpty()) {
                    List<ContentData> collectionDataList = new ArrayList<>(collectionDataMap.values());
                    searchResult.setCollectionDataList(collectionDataList);
                }

                response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                response.setResult(searchResult);
                TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
                return response;
            }

        } else {
            Map<String, Object> requestMap = ContentHandler.getSearchContentRequest(mAppContext, configService, contentSearchCriteria);

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

                String collectionDataList = null;
                if (result.containsKey("collections")) {
                    collectionDataList = GsonUtil.toJson(result.get("collections"));
                }

                SunbirdContentSearchResult searchResult = new SunbirdContentSearchResult();
                searchResult.setId(id);
                searchResult.setResponseMessageId(responseMessageId);
                searchResult.setRequest(requestMap);

                if (!StringUtil.isNullOrEmpty(contentDataList)
                        || !StringUtil.isNullOrEmpty(collectionDataList)) {

                    Type type = new TypeToken<List<ContentData>>() {
                    }.getType();

                    if (!StringUtil.isNullOrEmpty(contentDataList)) {
                        List<ContentData> contentData = GsonUtil.getGson().fromJson(contentDataList, type);
                        searchResult.setContentDataList(contentData);
                    }

                    if (!StringUtil.isNullOrEmpty(collectionDataList)) {
                        List<ContentData> contentData = GsonUtil.getGson().fromJson(collectionDataList, type);
                        searchResult.setCollectionDataList(contentData);
                    }

                    searchResult.setFilterCriteria(ContentHandler.createFilterCriteria(configService,
                            contentSearchCriteria, facets, (Map<String, Object>) requestMap.get("filters")));
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
        }

        return response;
    }

    @Override
    public GenieResponse<Void> flagContent(FlagContentRequest flagContentRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(flagContentRequest));
        String methodName = "flagContent@ContentServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        FlagContentAPI flagContentAPI = new FlagContentAPI(mAppContext, getCustomHeaders(authSession.getSessionData()),
                flagContentRequest.getContentId(), ContentHandler.getFlagContentRequestMap(flagContentRequest));
        GenieResponse genieResponse = flagContentAPI.post();

        if (genieResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(genieResponse.getError(), genieResponse.getMessage(), TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
        }

        return response;
    }

}