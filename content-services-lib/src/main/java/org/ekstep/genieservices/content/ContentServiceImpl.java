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
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.MasterDataValues;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
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
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.download.DownloadService;
import org.ekstep.genieservices.content.network.ContentSearchAPI;
import org.ekstep.genieservices.content.network.RecommendedContentAPI;
import org.ekstep.genieservices.content.network.RelatedContentAPI;

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
import java.util.TreeMap;
import java.util.UUID;

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
                response = GenieResponseBuilder.getErrorResponse(ServiceConstants.NO_DATA_FOUND, "No content found for identifier = " + contentIdentifier, TAG);
                return response;
            }

            contentModelInDB = ContentModel.build(mAppContext.getDBSession(), contentData, null);
        } else {
            ContentHandler.refreshContentDetails(mAppContext, contentIdentifier, contentModelInDB);
        }

        Content content = ContentHandler.convertContentModelToBean(contentModelInDB);

        String uid = ContentHandler.getCurrentUserId(userService);
        content.setContentFeedback(ContentHandler.getContentFeedback(contentFeedbackService, content.getIdentifier(), uid));
        content.setContentAccess(ContentHandler.getContentAccess(userService, content.getIdentifier(), uid));

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(content);
        return response;
    }

    @Override
    public GenieResponse<List<Content>> getAllLocalContent(ContentCriteria criteria) {
        // TODO: Telemetry logger
        String methodName = "getAllLocalContent@ContentServiceImpl";

        GenieResponse<List<Content>> response;
        if (criteria == null) {
            criteria = new ContentCriteria();
        }

        String uid;
        if (!StringUtil.isNullOrEmpty(criteria.getUid())) {
            uid = criteria.getUid();
        } else {
            uid = ContentHandler.getCurrentUserId(userService);
        }

        List<ContentModel> contentModelListInDB = ContentHandler.getAllLocalContentSortedByContentAccess(mAppContext.getDBSession(), uid, criteria.getContentTypes());

        List<Content> contentList = new ArrayList<>();
        for (ContentModel contentModel : contentModelListInDB) {
            Content c = ContentHandler.convertContentModelToBean(contentModel);

            if (criteria.isAttachFeedback()) {
                c.setContentFeedback(ContentHandler.getContentFeedback(contentFeedbackService, c.getIdentifier(), uid));
            }
            if (criteria.isAttachContentAccess()) {
                c.setContentAccess(ContentHandler.getContentAccess(userService, c.getIdentifier(), uid));
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
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.NO_DATA_FOUND, "No content found for identifier = " + contentIdentifier, TAG);
            return response;
        }

        List<Content> childContentList = new ArrayList<>();

        switch (levelAndState) {
            case ContentConstants.ChildContents.FIRST_LEVEL_ALL:
                if (contentModel.hasChildren()) {
                    List<ContentModel> contentModelListInDB = ContentHandler.getSortedChildrenList(mAppContext.getDBSession(), contentModel.getLocalData(), ContentConstants.ChildContents.FIRST_LEVEL_ALL);
                    for (ContentModel cm : contentModelListInDB) {
                        Content c = ContentHandler.convertContentModelToBean(cm);
                        childContentList.add(c);
                    }
                }
                break;

            case ContentConstants.ChildContents.FIRST_LEVEL_DOWNLOADED:
                childContentList = populateChildren(content, childContents);
                break;

            case ContentConstants.ChildContents.FIRST_LEVEL_SPINE:
                childContentList = populateChildren(content, childContents);
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
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.NO_DATA_FOUND, "No content found to delete for identifier = " + contentIdentifier, TAG);
            return response;
        }

        // TODO: Removing external content code
//        if (contentModel.isExternalContent() && mAppContext.getDeviceInfo().getAndroidSdkVersion() <= mAppContext.getDeviceInfo().getKitkatVersionCode()) {
//            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE, "This content cannot be deleted.", TAG);
//            return response;
//        }

        //delete or update pre-requisites
        if (contentModel.hasPreRequisites()) {
            ContentHandler.deleteAllPreRequisites(mAppContext, contentModel, level);
        }

        //delete or update child items
        if (contentModel.hasChildren()) {
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
    public GenieResponse<ContentSearchResult> searchContent(ContentSearchCriteria contentSearchCriteria) {
        GenieResponse<ContentSearchResult> response;

        Map<String, String[]> filter = contentSearchCriteria.getFilter();
        // Apply profile specific filter
        if (userService != null) {
            GenieResponse<Profile> profileResponse = userService.getCurrentUser();
            if (profileResponse.getStatus()) {
                Profile currentProfile = profileResponse.getResult();

                // Add age filter
                applyFilter(MasterDataType.AGEGROUP, String.valueOf(currentProfile.getAge()), filter);

                // Add board filter
                if (currentProfile.getBoard() != null) {
                    applyFilter(MasterDataType.BOARD, currentProfile.getBoard(), filter);
                }

                // Add medium filter
                if (currentProfile.getMedium() != null) {
                    applyFilter(MasterDataType.MEDIUM, currentProfile.getMedium(), filter);
                }

                // Add standard filter
                applyFilter(MasterDataType.GRADELEVEL, String.valueOf(currentProfile.getStandard()), filter);
            }
        }

        // Apply partner specific filer
        // TODO: 5/15/2017 - Uncomment after partner getting the API for getPartnerInfo in PartnerService.
//        HashMap<String, String> partnerInfo = GsonUtil.fromJson(getSharedPreferenceWrapper().getString(Constants.KEY_PARTNER_INFO, null), HashMap.class);
//        if (partnerInfo != null) {
//            //Apply Channel filter
//            String channel = partnerInfo.get(Constant.BUNDLE_KEY_PARTNER_CHANNEL);
//            if (channel != null) {
//                applyFilter(MasterDataType.CHANNEL, channel, filter);
//            }
//
//            //Apply Purpose filter
//            String audience = partnerInfo.get(Constant.BUNDLE_KEY_PARTNER_PURPOSE);
//            if (purpose != null) {
//                applyFilter(MasterDataType.AUDIENCE, audience, filter);
//            }
//        }

        contentSearchCriteria.setFilter(filter);

        // Populating implicit search criteria.
        List<String> facets = contentSearchCriteria.getFacets();
        facets.addAll(Arrays.asList("contentType", "domain", "ageGroup", "language", "gradeLevel"));
        contentSearchCriteria.setFacets(facets);

        addFiltersIfNotAvailable(contentSearchCriteria, "objectType", Arrays.asList("Content"));
        addFiltersIfNotAvailable(contentSearchCriteria, "contentType", Arrays.asList("Story", "Worksheet", "Collection", "Game", "TextBook"));
        addFiltersIfNotAvailable(contentSearchCriteria, "status", Arrays.asList("Live"));

        ContentSearchAPI contentSearchAPI = new ContentSearchAPI(mAppContext, getRequest(contentSearchCriteria));
        GenieResponse apiResponse = contentSearchAPI.post();
        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();

            LinkedTreeMap map = GsonUtil.fromJson(body, LinkedTreeMap.class);
            String id = (String) map.get("id");
            LinkedTreeMap responseParams = (LinkedTreeMap) map.get("params");
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");
            String responseFacetsString = GsonUtil.toJson(result.get("facets"));
            String contentDataListString = GsonUtil.toJson(result.get("content"));

            Type type = new TypeToken<List<HashMap<String, Object>>>() {
            }.getType();
            List<Map<String, Object>> responseFacets = GsonUtil.getGson().fromJson(responseFacetsString, type);
            List<Map<String, Object>> contentDataList = GsonUtil.getGson().fromJson(contentDataListString, type);

            List<Content> contents = new ArrayList<>();
            for (Map contentDataMap : contentDataList) {
                // TODO: 5/15/2017 - Can fetch content from DB and return in response.
                ContentModel contentModel = ContentModel.build(mAppContext.getDBSession(), contentDataMap, null);
                Content content = ContentHandler.convertContentModelToBean(contentModel);
                contents.add(content);
            }

            ContentSearchResult searchResult = new ContentSearchResult();
            searchResult.setId(id);
            searchResult.setParams(responseParams);
            searchResult.setFacets(getSortedFacets(responseFacets));
            searchResult.setRequest(getRequest(contentSearchCriteria));
            searchResult.setContents(contents);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(searchResult);
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        return response;
    }

    // TODO: TO be done by Swayangjit
    private void applyFilter(MasterDataType masterDataType, String propertyValue, Map<String, String[]> filter) {
        try {

            if (masterDataType == MasterDataType.AGEGROUP) {
                masterDataType = MasterDataType.AGE;
            }

            MasterDataValues masterDataValues = null;
            if (configService != null) {
                GenieResponse<MasterData> masterDataResponse = configService.getMasterData(masterDataType);

                MasterData masterData = null;
                if (masterDataResponse.getStatus()) {
                    masterData = masterDataResponse.getResult();
                }

                for (MasterDataValues values : masterData.getValues()) {
                    if (values.getValue().equals(propertyValue)) {
                        masterDataValues = values;
                        break;
                    }
                }
            }
            Map termMap = (Map) map.get(propertyValue);

            String masterDataTypeValue = masterDataType.getValue();

            Set termSet = new HashSet((List) termMap.get(masterDataTypeValue));
            if (filter.containsKey(masterDataTypeValue)) {
                if (filter.get(masterDataTypeValue) != null) {
                    Set set = new HashSet(Arrays.asList(filter.get(masterDataTypeValue)));
                    if (set != null && termSet != null) {
                        termSet.addAll(set);
                    }
                }
            }

            String[] strArr = new String[termSet.size()];
            termSet.toArray(strArr);
            filter.put(masterDataTypeValue, strArr);
        } catch (Exception e) {
            Logger.e(TAG, "Failed to apply filter");
        }
    }

    private void addFiltersIfNotAvailable(ContentSearchCriteria contentSearchCriteria, String key, List<String> values) {
        Map<String, String[]> filter = contentSearchCriteria.getFilter();
        if (filter == null) {
            filter = new HashMap<>();
        }

        if (filter.isEmpty() || filter.get(key) == null) {
            String[] newValues = values.toArray(new String[values.size()]);
            filter.put(key, newValues);
        }

        contentSearchCriteria.setFilter(filter);
    }

    private HashMap<String, Object> getRequest(ContentSearchCriteria criteria) {
        HashMap<String, Object> request = new HashMap<>();
        request.put("query", criteria.getQuery());
        request.put("limit", criteria.getLimit());
        request.put("mode", "soft");

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("compatibilityLevel", getCompatibilityLevel());
        if (criteria.getFilter() != null) {
            filterMap.putAll(criteria.getFilter());
        }
        request.put("filters", filterMap);

        if (criteria.getSort() != null) {
            request.put("sort_by", criteria.getSort());
        }

        if (criteria.getFacets() != null) {
            request.put("facets", criteria.getFacets());
        }

        return request;
    }

    private HashMap<String, Integer> getCompatibilityLevel() {
        HashMap<String, Integer> compatLevelMap = new HashMap<>();
        compatLevelMap.put("max", ContentHandler.maxCompatibilityLevel);
        compatLevelMap.put("min", ContentHandler.minCompatibilityLevel);
        return compatLevelMap;
    }

    private List<Map<String, Object>> getSortedFacets(List<Map<String, Object>> facets) {
        if (configService == null) {
            return facets;
        }

        GenieResponse<Map<String, Object>> ordinalsResponse = configService.getOrdinals();
        if (ordinalsResponse.getStatus()) {

            Map<String, Object> ordinalsMap = ordinalsResponse.getResult();

            if (ordinalsMap != null) {
                List<Map<String, Object>> sortedFacetList = new ArrayList<>();
                for (Map<String, Object> facetMap : facets) {
                    for (String nameKey : facetMap.keySet()) {
                        if (nameKey.equals("name")) {
                            String facetName = (String) facetMap.get(nameKey);

                            String facetValuesString = GsonUtil.toJson(facetMap.get("values"));
                            Type facetType = new TypeToken<List<Map<String, Object>>>() {
                            }.getType();
                            List<Map<String, Object>> facetValues = GsonUtil.getGson().fromJson(facetValuesString, facetType);

                            if (ordinalsMap.containsKey(facetName)) {
                                String dataString = GsonUtil.toJson(ordinalsMap.get(facetName));
                                Type type = new TypeToken<List<String>>() {
                                }.getType();
                                List<String> facetsOrder = GsonUtil.getGson().fromJson(dataString, type);

                                List<Map<String, Object>> valuesList = sortOrder(facetValues, facetsOrder);

                                HashMap<String, Object> map = new HashMap<>();
                                map.put("name", facetName);
                                map.put("values", valuesList);

                                sortedFacetList.add(map);
                            }
                            break;
                        }
                    }
                }

                return sortedFacetList;
            }
        }

        return facets;
    }

    private List<Map<String, Object>> sortOrder(List<Map<String, Object>> facetValues, List<String> facetsOrder) {
        Map<Integer, Map<String, Object>> map = new TreeMap<>();

        for (Map<String, Object> value : facetValues) {
            String name = (String) value.get("name");
            int index = indexOf(facetsOrder, name);
            map.put(index, value);
        }

        List<Map<String, Object>> valuesList = new ArrayList<>(map.values());
        return valuesList;
    }

    private int indexOf(List<String> responseFacets, String key) {
        if (!StringUtil.isNullOrEmpty(key)) {
            for (int i = 0; i < responseFacets.size(); i++) {
                if (key.equalsIgnoreCase(responseFacets.get(i))) {
                    return i;
                }
            }
        }

        return -1;
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

            List<Map<String, Object>> contentDataList = null;
            if (result.containsKey("content")) {
                contentDataList = (List<Map<String, Object>>) result.get("content");
            }

            List<Content> contents = new ArrayList<>();
            if (contentDataList != null) {
                for (Map contentDataMap : contentDataList) {
                    ContentModel contentModel = ContentModel.build(mAppContext.getDBSession(), contentDataMap, null);
                    Content content = ContentHandler.convertContentModelToBean(contentModel);
                    contents.add(content);
                }
            }

            ContentSearchResult searchResult = new ContentSearchResult();
            searchResult.setId(id);
            searchResult.setParams(responseParams);
            searchResult.setContents(contents);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(searchResult);
            return response;
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        return response;
    }

    @Override
    public GenieResponse<ContentSearchResult> getRelatedContent(String contentIdentifier) {
        // TODO: 5/18/2017 - Telemetry
//        HashMap params = new HashMap();
//        params.put("uid", uid);
//        params.put("content_id", contentIdentifier);
//        params.put("mode", getNetworkMode());
//        String method = "getRelatedContent@ContentServiceImpl";

        GenieResponse<ContentSearchResult> response;
        RelatedContentAPI relatedContentAPI = new RelatedContentAPI(mAppContext, ContentHandler.getRelatedContentRequest(userService, contentIdentifier, mAppContext.getDeviceInfo().getDeviceID()));
        GenieResponse apiResponse = relatedContentAPI.post();
        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();

            LinkedTreeMap map = GsonUtil.fromJson(body, LinkedTreeMap.class);
            String id = (String) map.get("id");
            LinkedTreeMap responseParams = (LinkedTreeMap) map.get("params");
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");

            List<Map<String, Object>> contentDataList = null;
            if (result.containsKey("content")) {
                contentDataList = (List<Map<String, Object>>) result.get("content");
            }

            List<ContentModel> allLocalContentModel = ContentHandler.getAllLocalContentModel(mAppContext.getDBSession(), null);

            List<Content> contents = new ArrayList<>();
            if (contentDataList != null) {
                for (Map contentDataMap : contentDataList) {
                    ContentModel contentModel = ContentModel.build(mAppContext.getDBSession(), contentDataMap, null);
                    Content content = ContentHandler.convertContentModelToBean(contentModel);

                    if (allLocalContentModel.contains(contentModel)) {
                        content.setAvailableLocally(true);
                    }

                    contents.add(content);
                }
            }

            ContentSearchResult searchResult = new ContentSearchResult();
            searchResult.setId(id);
            searchResult.setParams(responseParams);
            searchResult.setContents(contents);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(searchResult);
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
                if (node.hasChildren()) {
                    List<ContentModel> childContents = ContentHandler.getSortedChildrenList(mAppContext.getDBSession(), node.getLocalData(), ContentConstants.ChildContents.FIRST_LEVEL_ALL);
                    // TODO: 5/19/2017 -      List<ContentModel> childContents = node.getSortedChildrenList(dbOperator, CHILD_CONTENTS_FIRST_LEVEL_ALL);
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
    public GenieResponse<Void> importContent(boolean isChildContent, String ecarFilePath) {
        // TODO: 5/16/2017 - Telemetry logger
        String method = "importContent@ContentServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("importContent", ecarFilePath);
        params.put("isChildContent", isChildContent);
        params.put("logLevel", "2");

        GenieResponse<Void> response;

        if (!FileUtil.doesFileExists(ecarFilePath)) {
            response = GenieResponseBuilder.getErrorResponse(ContentConstants.INVALID_FILE, "content import failed, file doesn't exists", TAG);
            return response;
        }

        String ext = FileUtil.getFileExtension(ecarFilePath);
        if (!ServiceConstants.FileExtension.CONTENT.equals(ext)) {
            response = GenieResponseBuilder.getErrorResponse(ContentConstants.INVALID_FILE, "content import failed, unsupported file extension", TAG);
            return response;
        } else {
            File ecarFile = new File(ecarFilePath);
            File tmpLocation = new File(FileUtil.getTmpDir(mAppContext.getPrimaryFilesDir()), UUID.randomUUID().toString());
            ImportContext importContext = new ImportContext(ecarFile, tmpLocation);

            IChainable importContentSteps = ContentImportStep.initImportContent();
            importContentSteps.then(new DeviceMemoryCheck())
                    .then(new ExtractEcar())
                    .then(new ValidateEcar())
                    .then(new ExtractPayloads())
                    .then(new EcarCleanUp())
                    .then(new AddGeTransferContentImportEvent());

            return importContentSteps.execute(mAppContext, importContext);
        }
    }

    @Override
    public GenieResponse<Void> importContent(boolean isChildContent, List<String> contentIdentifiers) {
        DownloadService downloadService = new DownloadService(mAppContext);

        for (String contentIdentifier : contentIdentifiers) {
            Map dataMap = ContentHandler.fetchContentDetailsFromServer(mAppContext, contentIdentifier);
            String downloadUrl = ContentHandler.getDownloadUrl(dataMap);
            if (downloadUrl != null) {
                downloadUrl = downloadUrl.trim();
            }

            if (!StringUtil.isNullOrEmpty(downloadUrl) && ServiceConstants.FileExtension.CONTENT.equalsIgnoreCase(FileUtil.getFileExtension(downloadUrl))) {
                DownloadRequest downloadRequest = new DownloadRequest(contentIdentifier, downloadUrl, ContentConstants.MimeType.ECAR, isChildContent);
                downloadService.enQueue(downloadRequest);
            }
        }

        return null;
    }

}