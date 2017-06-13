package org.ekstep.genieservices.content;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IConfigService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IContentService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.ChildContentRequest;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentCriteria;
import org.ekstep.genieservices.commons.bean.ContentData;
import org.ekstep.genieservices.commons.bean.ContentDeleteRequest;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentFeedbackCriteria;
import org.ekstep.genieservices.commons.bean.ContentImportRequest;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.bean.ContentListingResult;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.HierarchyInfo;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RecommendedContentResult;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.bean.telemetry.GEInteract;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.download.DownloadService;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.chained.AddGeTransferContentImportEvent;
import org.ekstep.genieservices.content.chained.ContentImportStep;
import org.ekstep.genieservices.content.chained.DeviceMemoryCheck;
import org.ekstep.genieservices.content.chained.EcarCleanUp;
import org.ekstep.genieservices.content.chained.ExtractEcar;
import org.ekstep.genieservices.content.chained.ExtractPayloads;
import org.ekstep.genieservices.content.chained.ValidateEcar;
import org.ekstep.genieservices.content.db.model.ContentListingModel;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.network.ContentListingAPI;
import org.ekstep.genieservices.content.network.ContentSearchAPI;
import org.ekstep.genieservices.content.network.RecommendedContentAPI;
import org.ekstep.genieservices.content.network.RelatedContentAPI;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

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

    public ContentServiceImpl(AppContext appContext) {
        super(appContext);
    }

    public ContentServiceImpl(AppContext appContext, IUserService userService, IContentFeedbackService contentFeedbackService, IConfigService configService) {
        super(appContext);

        this.userService = userService;
        this.contentFeedbackService = contentFeedbackService;
        this.configService = configService;
    }

    @Override
    public GenieResponse<Content> getContentDetails(ContentDetailsRequest contentDetailsRequest) {
        // TODO: Telemetry logger
        String methodName = "getContentDetails@ContentServiceImpl";

        GenieResponse<Content> response;
        ContentModel contentModelInDB = ContentModel.find(mAppContext.getDBSession(), contentDetailsRequest.getContentId());

        if (contentModelInDB == null) {     // Fetch from server if detail is not available in DB
            Map contentData = ContentHandler.fetchContentDetailsFromServer(mAppContext, contentDetailsRequest.getContentId());
            if (contentData == null) {
                response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_DATA_FOUND, "No content found for identifier = " + contentDetailsRequest.getContentId(), TAG);
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
                ContentFeedbackCriteria.Builder builder = new ContentFeedbackCriteria.Builder();
                builder.byUser(uid)
                        .forContent(content.getIdentifier());
                content.setContentFeedback(ContentHandler.getContentFeedback(contentFeedbackService, builder.build()));
            }

            if (contentDetailsRequest.isAttachContentAccess()) {
                content.setContentAccess(ContentHandler.getContentAccess(userService, content.getIdentifier(), uid));
            }
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(content);
        return response;
    }

    @Override
    public GenieResponse<List<Content>> getAllLocalContent(ContentCriteria criteria) {
        // TODO: Telemetry logger
        String methodName = "getAllLocalContent@ContentServiceImpl";

        GenieResponse<List<Content>> response;

        List<ContentModel> contentModelListInDB = ContentHandler.getAllLocalContentSortedByContentAccess(mAppContext.getDBSession(), criteria);

        List<Content> contentList = new ArrayList<>();
        for (ContentModel contentModel : contentModelListInDB) {
            Content c = ContentHandler.convertContentModelToBean(contentModel);

            if (criteria.attachFeedback()) {
                ContentFeedbackCriteria.Builder builder = new ContentFeedbackCriteria.Builder();
                builder.byUser(criteria.getUid())
                        .forContent(c.getIdentifier());
                c.setContentFeedback(ContentHandler.getContentFeedback(contentFeedbackService, builder.build()));
            }
            if (criteria.attachContentAccess()) {
                c.setContentAccess(ContentHandler.getContentAccess(userService, c.getIdentifier(), criteria.getUid()));
            }

            contentList.add(c);
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentList);
        return response;
    }

    @Override
    public GenieResponse<Content> getChildContents(ChildContentRequest childContentRequest) {
        // TODO: Telemetry logger
        String methodName = "getChildContents@ContentServiceImpl";

        List<HierarchyInfo> hierarchyInfoList = new ArrayList<>();
        GenieResponse<Content> response;
        ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), childContentRequest.getContentId());
        if (contentModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_DATA_FOUND, "No content found for identifier = " + childContentRequest.getContentId(), TAG);
            return response;
        }

        //check and fetch all childrens of this content
        List<Content> childrenList = checkAndFetchChildrenOfContent(contentModel, hierarchyInfoList);

        Content content = ContentHandler.convertContentModelToBean(contentModel);
        if (childrenList != null) {
            content.setChildren(childrenList);
        }
        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(content);
        return response;
    }

    private List<Content> checkAndFetchChildrenOfContent(ContentModel contentModel, List<HierarchyInfo> hierarchyInfoList) {
        List<Content> contentList = new ArrayList<>();

        // check if the content model has immediate children
        if (ContentHandler.hasChildren(contentModel.getLocalData())) {

            //add hierarchy info
            HierarchyInfo hierarchyInfo = new HierarchyInfo();
            hierarchyInfo.setContentType(contentModel.getContentType());
            hierarchyInfo.setIdentifier(contentModel.getIdentifier());
            hierarchyInfoList.add(hierarchyInfo);

            //get all the children
            List<ContentModel> contentModelList = ContentHandler.getSortedChildrenList(mAppContext.getDBSession(), contentModel.getLocalData(), ContentConstants.ChildContents.FIRST_LEVEL_ALL);

            //check for null and size more than 0
            if (contentModelList != null) {
                for (ContentModel perContentModel : contentModelList) {
                    Content perContent = ContentHandler.convertContentModelToBean(perContentModel);
                    perContent.setChildrenHierarchyInfo(hierarchyInfoList);
                    //add this content to the list
                    contentList.add(perContent);

                    //recurse again on this content
                    checkAndFetchChildrenOfContent(perContentModel, hierarchyInfoList);
                }
            }
        }

        return contentList;
    }

    @Override
    public GenieResponse<Void> deleteContent(ContentDeleteRequest deleteRequest) {
        GenieResponse<Void> response;
        ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), deleteRequest.getContentId());

        if (contentModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_DATA_FOUND, "No content found to delete for identifier = " + deleteRequest.getContentId(), TAG);
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
        return response;
    }

    @Override
    public GenieResponse<ContentListingResult> getContentListing(ContentListingCriteria contentListingCriteria) {
        Profile profile = contentListingCriteria.getProfile();

        String jsonStr = null;
        // TODO: 6/8/2017 - Read the channel and audience from partnerFilters in criteria and make the comma seperated string and pass in find
        ContentListingModel contentListingModelInDB = ContentListingModel.find(mAppContext.getDBSession(), contentListingCriteria.getContentListingId(),
                profile, contentListingCriteria.getSubject(), null, null);
        if (contentListingModelInDB != null) {
            if (ContentHandler.dataHasExpired(contentListingModelInDB.getExpiryTime())) {
                contentListingModelInDB.delete();
            } else {
                jsonStr = contentListingModelInDB.getJson();
            }
        }

        if (jsonStr == null) {
            Map<String, Object> requestMap = ContentHandler.getContentListingRequest(configService, contentListingCriteria, mAppContext.getDeviceInfo().getDeviceID());
            ContentListingAPI api = new ContentListingAPI(mAppContext, contentListingCriteria.getContentListingId(), requestMap);
            GenieResponse apiResponse = api.post();
            if (apiResponse.getStatus()) {
                jsonStr = apiResponse.getResult().toString();
                ContentHandler.saveContentListingDataInDB(mAppContext.getDBSession(), contentListingCriteria, profile, jsonStr);
            } else {
                return GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
            }
        }

        if (jsonStr != null) {
            ContentListingResult contentListingResult = ContentHandler.getContentListingResult(mAppContext.getDBSession(), contentListingCriteria, jsonStr);
            if (contentListingResult != null) {
                GenieResponse<ContentListingResult> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                response.setResult(contentListingResult);
                return response;
            }
        }

        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.DATA_NOT_FOUND_ERROR, ServiceConstants.ErrorMessage.NO_CONTENT_LISTING_DATA, TAG);
    }

    @Override
    public GenieResponse<ContentSearchResult> searchContent(ContentSearchCriteria contentSearchCriteria) {
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
                contentDataList = (String) result.get("content");
            }

            ContentSearchResult searchResult = new ContentSearchResult();
            searchResult.setId(id);
            searchResult.setResponseMessageId(responseMessageId);
            searchResult.setFilter(ContentHandler.getFilters(configService, facets, (Map<String, Object>) requestMap.get("filters")));
            searchResult.setRequest(requestMap);

            if (!StringUtil.isNullOrEmpty(contentDataList)) {
                Type type = new TypeToken<List<ContentData>>() {
                }.getType();
                List<ContentData> contentData = GsonUtil.getGson().fromJson(contentDataList, type);
                searchResult.setContentDataList(contentData);
            }

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(searchResult);
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        return response;
    }

    @Override
    public GenieResponse<RecommendedContentResult> getRecommendedContent(RecommendedContentRequest request) {
//        HashMap params = new HashMap();
//        params.put("mode", getNetworkMode());
        String method = "getRecommendedContents@ContentServiceImpl";

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

            List<Map<String, Object>> contentDataList = null;
            if (result.containsKey("content")) {
                contentDataList = (List<Map<String, Object>>) result.get("content");
            }

            RecommendedContentResult recommendedContentResult = new RecommendedContentResult();
            recommendedContentResult.setId(id);
            recommendedContentResult.setResponseMessageId(responseMessageId);
            recommendedContentResult.setContents(ContentHandler.convertContentMapListToBeanList(mAppContext.getDBSession(), contentDataList));

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(recommendedContentResult);
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        return response;
    }

    @Override
    public GenieResponse<RelatedContentResult> getRelatedContent(RelatedContentRequest request) {
        // TODO: 5/18/2017 - Telemetry
//        HashMap params = new HashMap();
//        params.put("uid", uid);
//        params.put("content_id", contentIdentifier);
//        params.put("mode", getNetworkMode());
//        String method = "getRelatedContent@ContentServiceImpl";

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
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        return response;
    }

    @Override
    public GenieResponse<List<Content>> nextContent(List<String> contentIdentifiers) {
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
                    List<ContentModel> childContents = ContentHandler.getSortedChildrenList(mAppContext.getDBSession(), node.getLocalData(), ContentConstants.ChildContents.FIRST_LEVEL_ALL);
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
        return response;
    }

    @Override
    public GenieResponse<Void> importContent(ContentImportRequest importRequest) {

        String method = "importContent@ContentServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("importContent", importRequest.getSourceFilePath());
        params.put("isChildContent", importRequest.isChildContent());
        params.put("logLevel", "2");

        GenieResponse<Void> response;

        if (!StringUtil.isNullOrEmpty(importRequest.getSourceFilePath())) {   // Import from file
            if (!FileUtil.doesFileExists(importRequest.getSourceFilePath())) {
                response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "content import failed, file doesn't exists", TAG);
                return response;
            }

            String ext = FileUtil.getFileExtension(importRequest.getSourceFilePath());
            if (!ServiceConstants.FileExtension.CONTENT.equals(ext)) {
                response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "content import failed, unsupported file extension", TAG);
                return response;
            } else {
                ImportContext importContext = new ImportContext(importRequest.isChildContent(), importRequest.getSourceFilePath(), importRequest.getDestinationFolder());

                // TODO: 6/8/2017 - Add identifier in the GE_INTERACT event
                buildInitiateEvent();

                IChainable importContentSteps = ContentImportStep.initImportContent();
                importContentSteps.then(new DeviceMemoryCheck())
                        .then(new ExtractEcar())
                        .then(new ValidateEcar())
                        .then(new ExtractPayloads())
                        .then(new EcarCleanUp())
                        .then(new AddGeTransferContentImportEvent());
                GenieResponse<Void> genieResponse = importContentSteps.execute(mAppContext, importContext);
                if (genieResponse.getStatus()) {
                    // TODO: 6/8/2017 - Add identifier in the GE_INTERACT event
                    buildSuccessEvent();
//                EventPublisher.postImportSuccessfull(new ImportStatus(null));

                }
                return genieResponse;
            }
        } else {    // Download and then import
            DownloadService downloadService = new DownloadService(mAppContext);

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
                            downloadRequests[i] = downloadRequest;
                        }
                    }

                    downloadService.enqueue(downloadRequests);
                }
            }

            return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        }
    }

    private void buildInitiateEvent() {
        GEInteract geInteract = new GEInteract.Builder(new GameData(mAppContext.getParams().getGid(), mAppContext.getParams().getVersionName())).
                stageId(ServiceConstants.Telemetry.CONTENT_IMPORT_STAGE_ID).
                subType(ServiceConstants.Telemetry.CONTENT_IMPORT_INITIATED_SUB_TYPE).
                interActionType(InteractionType.TOUCH).
                build();
        TelemetryLogger.log(geInteract);

    }

    private void buildSuccessEvent() {
        GEInteract geInteract = new GEInteract.Builder(new GameData(mAppContext.getParams().getGid(), mAppContext.getParams().getVersionName())).
                stageId(ServiceConstants.Telemetry.CONTENT_IMPORT_STAGE_ID).
                subType(ServiceConstants.Telemetry.CONTENT_IMPORT_SUCCESS_SUB_TYPE).
                interActionType(InteractionType.OTHER).
                build();
        TelemetryLogger.log(geInteract);
    }


}