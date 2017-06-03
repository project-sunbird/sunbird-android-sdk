package org.ekstep.genieservices.content;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IConfigService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IContentService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentCriteria;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.bean.ContentListingResult;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.download.DownloadService;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.bean.ImportContext;
import org.ekstep.genieservices.content.chained.AddGeTransferContentImportEvent;
import org.ekstep.genieservices.content.chained.ContentImportStep;
import org.ekstep.genieservices.content.chained.DeviceMemoryCheck;
import org.ekstep.genieservices.content.chained.EcarCleanUp;
import org.ekstep.genieservices.content.chained.ExtractEcar;
import org.ekstep.genieservices.content.chained.ExtractPayloads;
import org.ekstep.genieservices.content.chained.IChainable;
import org.ekstep.genieservices.content.chained.ValidateEcar;
import org.ekstep.genieservices.content.db.model.ContentListingModel;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.network.ContentListingAPI;
import org.ekstep.genieservices.content.network.ContentSearchAPI;
import org.ekstep.genieservices.content.network.RecommendedContentAPI;
import org.ekstep.genieservices.content.network.RelatedContentAPI;

import java.io.File;
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
    public GenieResponse<Content> getContentDetails(String contentIdentifier) {
        // TODO: Telemetry logger
        String methodName = "getContentDetails@ContentServiceImpl";

        GenieResponse<Content> response;
        ContentModel contentModelInDB = ContentModel.find(mAppContext.getDBSession(), contentIdentifier);

        if (contentModelInDB == null) {     // Fetch from server if detail is not available in DB
            Map contentData = ContentHandler.fetchContentDetailsFromServer(mAppContext, contentIdentifier);
            if (contentData == null) {
                response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_DATA_FOUND, "No content found for identifier = " + contentIdentifier, TAG);
                return response;
            }

            contentModelInDB = ContentHandler.convertContentMapToModel(mAppContext.getDBSession(), contentData, null);
        } else {
            ContentHandler.refreshContentDetailsFromServer(mAppContext, contentIdentifier, contentModelInDB);
        }

        Content content = ContentHandler.convertContentModelToBean(contentModelInDB);

        if (content.isAvailableLocally()) {
            String uid = ContentHandler.getCurrentUserId(userService);
            content.setContentFeedback(ContentHandler.getContentFeedback(contentFeedbackService, content.getIdentifier(), uid));
            content.setContentAccess(ContentHandler.getContentAccess(userService, content.getIdentifier(), uid));
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
                c.setContentFeedback(ContentHandler.getContentFeedback(contentFeedbackService, c.getIdentifier(), criteria.getUid()));
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
    public GenieResponse<List<Content>> getChildContents(String contentIdentifier, int levelAndState) {
        // TODO: Telemetry logger
        String methodName = "getChildContents@ContentServiceImpl";

        GenieResponse<List<Content>> response;
        ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), contentIdentifier);
        if (contentModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_DATA_FOUND, "No content found for identifier = " + contentIdentifier, TAG);
            return response;
        }

        List<Content> childContentList = new ArrayList<>();

        switch (levelAndState) {
            case ContentConstants.ChildContents.FIRST_LEVEL_ALL:
                if (ContentHandler.hasChildren(contentModel.getLocalData())) {
                    List<ContentModel> contentModelListInDB = ContentHandler.getSortedChildrenList(mAppContext.getDBSession(), contentModel.getLocalData(), ContentConstants.ChildContents.FIRST_LEVEL_ALL);
                    for (ContentModel cm : contentModelListInDB) {
                        Content c = ContentHandler.convertContentModelToBean(cm);
                        childContentList.add(c);
                    }
                }
                break;

            case ContentConstants.ChildContents.FIRST_LEVEL_DOWNLOADED:
//  TODO:              childContentList = populateChildren(content, childContents);
                break;

            case ContentConstants.ChildContents.FIRST_LEVEL_SPINE:
//   TODO:             childContentList = populateChildren(content, childContents);
                break;

            default:
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(childContentList);
        return response;
    }

    @Override
    public GenieResponse<Void> deleteContent(String contentIdentifier, int level) {
        GenieResponse<Void> response;
        ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), contentIdentifier);

        if (contentModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_DATA_FOUND, "No content found to delete for identifier = " + contentIdentifier, TAG);
            return response;
        }

        // TODO: Removing external content code
//        if (contentModel.isExternalContent() && mAppContext.getDeviceInfo().getAndroidSdkVersion() <= mAppContext.getDeviceInfo().getKitkatVersionCode()) {
//            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE, "This content cannot be deleted.", TAG);
//            return response;
//        }

        //delete or update pre-requisites
        if (ContentHandler.hasPreRequisites(contentModel.getLocalData())) {
            ContentHandler.deleteAllPreRequisites(mAppContext, contentModel, level);
        }

        //delete or update child items
        if (ContentHandler.hasChildren(contentModel.getLocalData())) {
            ContentHandler.deleteAllChild(mAppContext, contentModel, level);
        }

        //delete or update root item
        ContentHandler.deleteOrUpdateContent(contentModel, false, level);

        // TODO: Removing external content code
//        if (contentModel.isExternalContent()) {
//            FileUtil.refreshSDCard();
//        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        return response;
    }

    @Override
    public GenieResponse<ContentListingResult> getContentListing(ContentListingCriteria contentListingCriteria) {
        Profile profile = null;
        if (contentListingCriteria.isCurrentProfileFilter()) {
            profile = ContentHandler.getCurrentProfile(userService);
        }

        String jsonStr = null;
        ContentListingModel contentListingModelInDB = ContentListingModel.find(mAppContext.getDBSession(), contentListingCriteria.getPageIdentifier(), profile, contentListingCriteria.getSubject());
        if (contentListingModelInDB != null) {
            if (ContentHandler.dataHasExpired(contentListingModelInDB.getExpiryTime())) {
                contentListingModelInDB.delete();
            } else {
                jsonStr = contentListingModelInDB.getJson();
            }
        }

        if (jsonStr == null) {
            Map<String, Object> requestMap = ContentHandler.getContentListingRequest(configService, profile, contentListingCriteria.getSubject(), contentListingCriteria.getPartnerFilters(), mAppContext.getDeviceInfo().getDeviceID());
            ContentListingAPI api = new ContentListingAPI(mAppContext, contentListingCriteria.getPageIdentifier(), requestMap);
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

            List<Map<String, Object>> contentDataList = null;
            if (result.containsKey("content")) {
                contentDataList = (List<Map<String, Object>>) result.get("content");
            }

            ContentSearchResult searchResult = new ContentSearchResult();
            searchResult.setId(id);
            searchResult.setResponseMessageId(responseMessageId);
            searchResult.setFilter(ContentHandler.getFilters(configService, facets, (Map<String, Object>) requestMap.get("filters")));
            searchResult.setRequest(requestMap);
            searchResult.setContents(ContentHandler.convertContentMapListToBeanList(mAppContext.getDBSession(), contentDataList));

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(searchResult);
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        return response;
    }

    @Override
    public GenieResponse<ContentSearchResult> getRecommendedContent(String language) {
//        HashMap params = new HashMap();
//        params.put("mode", getNetworkMode());
        String method = "getRecommendedContents@ContentServiceImpl";

        GenieResponse<ContentSearchResult> response;
        RecommendedContentAPI recommendedContentAPI = new RecommendedContentAPI(mAppContext, ContentHandler.getRecommendedContentRequest(language, mAppContext.getDeviceInfo().getDeviceID()));
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

            ContentSearchResult searchResult = new ContentSearchResult();
            searchResult.setId(id);
            searchResult.setResponseMessageId(responseMessageId);
            searchResult.setContents(ContentHandler.convertContentMapListToBeanList(mAppContext.getDBSession(), contentDataList));

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(searchResult);
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        return response;
    }

    @Override
    public GenieResponse<RelatedContentResult> getRelatedContent(String contentIdentifier) {
        // TODO: 5/18/2017 - Telemetry
//        HashMap params = new HashMap();
//        params.put("uid", uid);
//        params.put("content_id", contentIdentifier);
//        params.put("mode", getNetworkMode());
//        String method = "getRelatedContent@ContentServiceImpl";

        GenieResponse<RelatedContentResult> response;
        RelatedContentAPI relatedContentAPI = new RelatedContentAPI(mAppContext, ContentHandler.getRelatedContentRequest(userService, contentIdentifier, mAppContext.getDeviceInfo().getDeviceID()));
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
    public GenieResponse<Void> importContent(boolean isChildContent, String ecarFilePath, File destinationFolder) {
        // TODO: 5/16/2017 - Telemetry logger
        String method = "importContent@ContentServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("importContent", ecarFilePath);
        params.put("isChildContent", isChildContent);
        params.put("logLevel", "2");

        GenieResponse<Void> response;

        if (!FileUtil.doesFileExists(ecarFilePath)) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "content import failed, file doesn't exists", TAG);
            return response;
        }

        String ext = FileUtil.getFileExtension(ecarFilePath);
        if (!ServiceConstants.FileExtension.CONTENT.equals(ext)) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "content import failed, unsupported file extension", TAG);
            return response;
        } else {
            ImportContext importContext = new ImportContext(isChildContent, ecarFilePath, destinationFolder);

            IChainable importContentSteps = ContentImportStep.initImportContent();
            importContentSteps.then(new DeviceMemoryCheck())
                    .then(new ExtractEcar())
                    .then(new ValidateEcar())
                    .then(new ExtractPayloads())
                    .then(new EcarCleanUp())
                    .then(new AddGeTransferContentImportEvent());
            GenieResponse<Void> genieResponse = importContentSteps.execute(mAppContext, importContext);
//            if (genieResponse.getStatus()) {
//                EventPublisher.postImportSuccessfull(new ImportStatus(null));
//            }
            return genieResponse;
        }
    }

    @Override
    public GenieResponse<Void> importContent(boolean isChildContent, List<String> contentIdentifiers, File destinationFolder) {
        DownloadService downloadService = new DownloadService(mAppContext);

        ContentSearchAPI contentSearchAPI = new ContentSearchAPI(mAppContext, ContentHandler.getSearchRequest(contentIdentifiers));
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
                        DownloadRequest downloadRequest = new DownloadRequest(contentIdentifier, downloadUrl, ContentConstants.MimeType.ECAR, destinationFolder, isChildContent);
                        downloadRequests[i] = downloadRequest;
                    }
                }

                downloadService.enqueue(downloadRequests);
            }
        }

        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

}