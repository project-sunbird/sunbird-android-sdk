package org.ekstep.genieservices.content;

import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessCriteria;
import org.ekstep.genieservices.commons.bean.ContentData;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.bean.Variant;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.bean.ContentChild;
import org.ekstep.genieservices.content.bean.ContentVariant;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.db.model.ContentsModel;
import org.ekstep.genieservices.content.network.ContentDetailsAPI;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

import static java.lang.String.valueOf;

/**
 * Created on 5/23/2017.
 *
 * @author anil
 */
public class ContentHandler {

    private static final String TAG = ContentHandler.class.getSimpleName();

    private static final int DEFAULT_PACKAGE_VERSION = -1;
    private static final int INITIAL_VALUE_FOR_TRANSFER_COUNT = 0;

    public static int minCompatibilityLevel = 1;
    public static int maxCompatibilityLevel = 3;
    // TODO: 02-03-2017 : We can remove this later after few release
    public static int defaultCompatibilityLevel = 1;

    public static Map fetchContentDetailsFromServer(AppContext appContext, String contentIdentifier) {
        ContentDetailsAPI api = new ContentDetailsAPI(appContext, contentIdentifier);
        GenieResponse apiResponse = api.get();

        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();

            Map map = GsonUtil.fromJson(body, Map.class);
            Map result = (Map) map.get("result");

            return (Map) result.get("content");
        }

        return null;
    }

    public static void refreshContentDetails(final AppContext appContext, final String contentIdentifier, final ContentModel existingContentModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map contentData = ContentHandler.fetchContentDetailsFromServer(appContext, contentIdentifier);

                if (contentData != null) {
                    ContentModel contentModel = ContentModel.build(appContext.getDBSession(), contentData, null);
                    contentModel.setVisibility(existingContentModel.getVisibility());
                    contentModel.addOrUpdateRefCount(existingContentModel.getRefCount());
                    contentModel.addOrUpdateContentState(existingContentModel.getContentState());

                    contentModel.update();
                }
            }
        }).start();
    }

    public static Content convertContentModelToBean(ContentModel contentModel) {
        Content content = new Content();
        content.setIdentifier(contentModel.getIdentifier());

        ContentData localData = null;
        if (contentModel.getLocalData() != null) {
            localData = GsonUtil.fromJson(contentModel.getLocalData(), ContentData.class);
        }
        content.setContentData(localData);

        ContentData serverData = null;
        if (contentModel.getServerData() != null) {
            serverData = GsonUtil.fromJson(contentModel.getServerData(), ContentData.class);
        }

        if (serverData != null) {
            addContentVariants(serverData, contentModel.getServerData());
        } else if (localData != null) {
            addContentVariants(localData, contentModel.getLocalData());
        }

        content.setMimeType(contentModel.getMimeType());
        content.setBasePath(contentModel.getPath());
        content.setContentType(contentModel.getContentType());
        content.setAvailableLocally(isAvailableLocally(contentModel.getContentState()));
        content.setReferenceCount(contentModel.getRefCount());
        content.setUpdateAvailable(isUpdateAvailable(serverData, localData));

        long contentCreationTime = 0;
        String localLastUpdatedTime = contentModel.getLocalLastUpdatedTime();
        if (!StringUtil.isNullOrEmpty(localLastUpdatedTime)) {
            contentCreationTime = DateUtil.getTime(localLastUpdatedTime.substring(0, localLastUpdatedTime.lastIndexOf(".")));
        }
        content.setLastUpdatedTime(contentCreationTime);

        return content;
    }

    private static void addContentVariants(ContentData contentData, String dataJson) {
        List<Variant> variantList = new ArrayList<>();

        Type type = new TypeToken<List<HashMap<String, Object>>>() {
        }.getType();
        Map<String, Object> dataMap = GsonUtil.getGson().fromJson(dataJson, type);

        Object variants = dataMap.get(ContentModel.KEY_VARIANTS);
        if (variants != null) {
            String variantsString;
            if (variants instanceof Map) {
                variantsString = GsonUtil.getGson().toJson(variants);
            } else {
                variantsString = (String) variants;
            }

            variantsString = variantsString.replace("\\", "");
            ContentVariant contentVariant = GsonUtil.fromJson(variantsString, ContentVariant.class);

            if (contentVariant.getSpine() != null) {
                Variant variant = new Variant("spine", contentVariant.getSpine().getEcarUrl(), contentVariant.getSpine().getSize());
                variantList.add(variant);
            }
        }

        contentData.setVariants(variantList);
    }

    public static String getCurrentUserId(IUserService userService) {
        if (userService != null) {
            UserSession userSession = userService.getCurrentUserSession().getResult();
            if (userSession != null) {
                return userSession.getUid();
            }
        }
        return null;
    }

    public static ContentFeedback getContentFeedback(IContentFeedbackService contentFeedbackService, String contentIdentifier, String uid) {
        if (contentFeedbackService != null) {
            return contentFeedbackService.getFeedback(uid, contentIdentifier).getResult();
        }

        return null;
    }

    public static ContentAccess getContentAccess(IUserService userService, String contentIdentifier, String uid) {
        if (userService != null) {
            List<ContentAccess> contentAccessList = getAllContentAccess(userService, uid, contentIdentifier);
            if (contentAccessList.size() > 0) {
                return contentAccessList.get(0);
            }
        }

        return null;
    }

    private static boolean isUpdateAvailable(ContentData serverData, ContentData localData) {
        float lVersion = DEFAULT_PACKAGE_VERSION;
        float sVersion = DEFAULT_PACKAGE_VERSION;

        if (serverData != null && !StringUtil.isNullOrEmpty(serverData.getPkgVersion())) {
            sVersion = Float.valueOf(serverData.getPkgVersion());
        }

        if (localData != null && !StringUtil.isNullOrEmpty(localData.getPkgVersion())) {
            lVersion = Float.valueOf(localData.getPkgVersion());
        }

        return sVersion > 0 && lVersion > 0 && sVersion > lVersion;
    }

    private static boolean isAvailableLocally(int contentState) {
        return contentState == ContentConstants.State.ARTIFACT_AVAILABLE;
    }

    public static List<ContentModel> getAllLocalContentSortedByContentAccess(IDBSession dbSession, String uid, ContentType[] contentTypes) {
        String contentTypesStr = getCommaSeparatedContentTypes(contentTypes);

        String isContentType = String.format(Locale.US, "c.%s in ('%s')", ContentEntry.COLUMN_NAME_CONTENT_TYPE, contentTypesStr);
        String isVisible = String.format(Locale.US, "c.%s = '%s'", ContentEntry.COLUMN_NAME_VISIBILITY, ContentConstants.Visibility.DEFAULT);
        String isArtifactAvailable = String.format(Locale.US, "c.%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);
        String filter = String.format(Locale.US, "WHERE (%s AND %s AND %s)", isVisible, isArtifactAvailable, isContentType);

        String orderBy = String.format(Locale.US, "ORDER BY ca.%s desc, c.%s desc, c.%s desc", ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP, ContentEntry.COLUMN_NAME_LOCAL_LAST_UPDATED_ON, ContentEntry.COLUMN_NAME_SERVER_LAST_UPDATED_ON);

        String query = String.format(Locale.US, "SELECT * FROM  %s c LEFT JOIN %s ca ON c.%s = ca.%s AND ca.%s = '%s' %s %s;",
                ContentEntry.TABLE_NAME, ContentAccessEntry.TABLE_NAME, ContentEntry.COLUMN_NAME_IDENTIFIER, ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER, ContentAccessEntry.COLUMN_NAME_UID, uid,
                filter, orderBy);


        List<ContentModel> contentModelListInDB;
        ContentsModel contentsModel = ContentsModel.findWithCustomQuery(dbSession, query);
        if (contentsModel != null) {
            contentModelListInDB = contentsModel.getContentModelList();
        } else {
            contentModelListInDB = new ArrayList<>();
        }

        return contentModelListInDB;
    }

    public static List<ContentModel> getAllLocalContentModel(IDBSession dbSession, ContentType[] contentTypes) {
        String contentTypesStr = getCommaSeparatedContentTypes(contentTypes);

        String isContentType = String.format(Locale.US, "%s in ('%s')", ContentEntry.COLUMN_NAME_CONTENT_TYPE, contentTypesStr);
        String isVisible = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_VISIBILITY, ContentConstants.Visibility.DEFAULT);
        // For hiding the non compatible imported content, which visibility is DEFAULT.
        String isArtifactAvailable = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);

        String filter = String.format(Locale.US, " where (%s AND %s AND %s)", isVisible, isArtifactAvailable, isContentType);

        List<ContentModel> contentModelListInDB;
        ContentsModel contentsModel = ContentsModel.find(dbSession, filter);
        if (contentsModel != null) {
            contentModelListInDB = contentsModel.getContentModelList();
        } else {
            contentModelListInDB = new ArrayList<>();
        }

        return contentModelListInDB;
    }

    private static String getCommaSeparatedContentTypes(ContentType[] contentTypes) {
        String contentTypesStr;

        if (contentTypes != null) {
            List<String> contentTypeList = new ArrayList<>();
            for (ContentType contentType : contentTypes) {
                contentTypeList.add(contentType.getValue());
            }
            contentTypesStr = StringUtil.join("','", contentTypeList);
        } else {
            contentTypesStr = StringUtil.join("','", ContentType.values());
        }

        return contentTypesStr;
    }

    public static List<ContentAccess> getAllContentAccessByUid(IUserService userService, String uid) {
        List<ContentAccess> contentAccessList;
        if (userService != null) {
            contentAccessList = getAllContentAccess(userService, uid, null);
        } else {
            contentAccessList = new ArrayList<>();
        }

        return contentAccessList;
    }

    private static List<ContentAccess> getAllContentAccess(IUserService userService, String uid, String contentIdentifier) {
        ContentAccessCriteria contentAccessCriteria = new ContentAccessCriteria();
        contentAccessCriteria.setUid(uid);
        contentAccessCriteria.setContentIdentifier(contentIdentifier);
        return userService.getAllContentAccess(contentAccessCriteria).getResult();
    }

    public static void deleteOrUpdateContent(ContentModel contentModel, boolean isChildItems, int level) {
        int refCount = contentModel.getRefCount();

        if (level == ContentConstants.Delete.NESTED) {
            // If visibility is Default it means this content was visible in my downloads.
            // After deleting artifact for this content it should not visible as well so reduce the refCount also for this.
            if (refCount > 1 && ContentConstants.Visibility.DEFAULT.equalsIgnoreCase(contentModel.getVisibility())) {
                refCount = refCount - 1;

                // Update visibility
                contentModel.setVisibility(ContentConstants.Visibility.PARENT);
            }

            // Update the contentState
            // Do not update the content state if contentType is Collection / TextBook / TextBookUnit
            if (ContentType.COLLECTION.getValue().equalsIgnoreCase(contentModel.getContentType())
                    || ContentType.TEXTBOOK.getValue().equalsIgnoreCase(contentModel.getContentType())
                    || ContentType.TEXTBOOK_UNIT.getValue().equalsIgnoreCase(contentModel.getContentType())) {
                contentModel.addOrUpdateContentState(ContentConstants.State.ARTIFACT_AVAILABLE);
            } else {
                contentModel.addOrUpdateContentState(ContentConstants.State.ONLY_SPINE);

                // if there are no entry in DB for any content then on this case contentModel.getPath() will be null
                if (contentModel.getPath() != null) {
                    FileUtil.rm(new File(contentModel.getPath()), contentModel.getIdentifier());
                }
            }

        } else {
            // TODO: This check should be before updating the existing refCount.
            // Do not update the content state if contentType is Collection / TextBook / TextBookUnit and refCount is more than 1.
            if ((ContentType.COLLECTION.getValue().equalsIgnoreCase(contentModel.getContentType())
                    || ContentType.TEXTBOOK.getValue().equalsIgnoreCase(contentModel.getContentType())
                    || ContentType.TEXTBOOK_UNIT.getValue().equalsIgnoreCase(contentModel.getContentType()))
                    && refCount > 1) {
                contentModel.addOrUpdateContentState(ContentConstants.State.ARTIFACT_AVAILABLE);
            } else if (refCount > 1 && isChildItems) {  //contentModel.isVisibilityDefault() &&
                // Visibility will remain Default only.

                contentModel.addOrUpdateContentState(ContentConstants.State.ARTIFACT_AVAILABLE);
            } else {

                // Set the visibility to Parent so that this content will not visible in My contents / Downloads section.
                // Update visibility
                if (ContentConstants.Visibility.DEFAULT.equalsIgnoreCase(contentModel.getVisibility())) {
                    contentModel.setVisibility(ContentConstants.Visibility.PARENT);
                }

                contentModel.addOrUpdateContentState(ContentConstants.State.ONLY_SPINE);

                // if there are no entry in DB for any content then on this case contentModel.getPath() will be null
                if (contentModel.getPath() != null) {
                    FileUtil.rm(new File(contentModel.getPath()), contentModel.getIdentifier());
                }
            }

            refCount = refCount - 1;
        }

        // Update the refCount
        contentModel.addOrUpdateRefCount(refCount);

        // if there are no entry in DB for any content then on this case contentModel.getPath() will be null
        if (contentModel.getPath() != null) {
            contentModel.update();
        }
    }

    public static void deleteAllPreRequisites(AppContext appContext, ContentModel contentModel, int level) {
        List<String> preRequisitesIdentifier = contentModel.getPreRequisitesIdentifiers();
        ContentsModel contentsModel = ContentsModel.findAllContentsWithIdentifiers(appContext.getDBSession(), preRequisitesIdentifier);

        if (contentsModel != null) {
            for (ContentModel c : contentsModel.getContentModelList()) {
                deleteOrUpdateContent(c, true, level);
            }
        }
    }

    /**
     * Is compatible to current version of Genie.
     *
     * @return true if compatible else false.
     */
    public static boolean isCompatible(Double compatibilityLevel) {
        return (compatibilityLevel >= minCompatibilityLevel) && (compatibilityLevel <= maxCompatibilityLevel);
    }

    public static boolean isImportFileExist(ContentModel oldContentModel, ContentModel newContentModel) {
        if (oldContentModel == null || newContentModel == null) {
            return false;
        }

        boolean isExist = false;
        try {
            String oldIdentifier = oldContentModel.getIdentifier();
            String newIdentifier = newContentModel.getIdentifier();
            String oldVisibility = oldContentModel.getVisibility();
            String newVisibility = newContentModel.getVisibility();

            if (oldIdentifier.equalsIgnoreCase(newIdentifier) && oldVisibility.equalsIgnoreCase(newVisibility)) {
                isExist = oldContentModel.pkgVersion() >= newContentModel.pkgVersion();
            }
        } catch (Exception e) {
            Logger.e(TAG, "isImportFileExist", e);
        }

        return isExist;
    }

    /**
     * To Check whether the content is exist or not.
     *
     * @param oldContent Old ContentModel
     * @param newContent New ContentModel
     * @return True - if file exists, False- does not exists
     */
    public static boolean isContentExist(ContentModel oldContent, ContentModel newContent) {
        if (oldContent == null || newContent == null) {
            return false;
        }

        boolean isExist = false;
        try {
            String oldIdentifier = oldContent.getIdentifier();
            String newIdentifier = newContent.getIdentifier();
            if (oldIdentifier.equalsIgnoreCase(newIdentifier)) {
                isExist = (oldContent.pkgVersion() >= newContent.pkgVersion()) // If old content's pkgVersion is less than the new content then return false.
                        || oldContent.getContentState() == ContentConstants.State.ARTIFACT_AVAILABLE;  // If content_state is other than artifact available then also return  false.
            }
        } catch (Exception e) {
            Logger.e(TAG, "isContentExist", e);
        }

        return isExist;
    }

    private static boolean contentMetadataAbsent(Map<String, Object> localDataMap) {
        return localDataMap.get(ContentModel.KEY_CONTENT_METADATA) == null;
    }

    private static boolean contentMetadataPresentWithoutViralityMetadata(Map<String, Object> localDataMap) {
        return localDataMap.get(ContentModel.KEY_CONTENT_METADATA) != null
                && ((Map<String, Object>) localDataMap.get(ContentModel.KEY_CONTENT_METADATA)).get(ContentModel.KEY_VIRALITY_METADATA) == null;
    }

    private static boolean contentAndViralityMetadataPresent(Map<String, Object> localDataMap) {
        return localDataMap.get(ContentModel.KEY_CONTENT_METADATA) != null
                && ((Map<String, Object>) localDataMap.get(ContentModel.KEY_CONTENT_METADATA)).get(ContentModel.KEY_VIRALITY_METADATA) != null;
    }

    public static int transferCount(Map<String, Object> localDataMap) {
        try {
            return Double.valueOf(valueOf(viralityMetadata(localDataMap).get(ContentModel.KEY_TRANSFER_COUNT))).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    private static Map<String, Object> viralityMetadata(Map<String, Object> localDataMap) {
        return contentAndViralityMetadataPresent(localDataMap)
                ? (Map<String, Object>) ((Map<String, Object>) localDataMap.get(ContentModel.KEY_CONTENT_METADATA)).get(ContentModel.KEY_VIRALITY_METADATA)
                : new HashMap<String, Object>();
    }

    public static void addOrUpdateViralityMetadata(String localDataJson, String origin) {
        Map<String, Object> localDataMap = GsonUtil.fromJson(localDataJson, Map.class);

        if (contentMetadataAbsent(localDataMap)) {
            Map<String, Object> contentMetadata = new HashMap<>();
            localDataMap.put(ContentModel.KEY_CONTENT_METADATA, contentMetadata);
            Map<String, Object> viralityMetadata = new HashMap<>();
            contentMetadata.put(ContentModel.KEY_VIRALITY_METADATA, viralityMetadata);
            viralityMetadata.put(ContentModel.KEY_ORIGIN, origin);
            viralityMetadata.put(ContentModel.KEY_TRANSFER_COUNT, INITIAL_VALUE_FOR_TRANSFER_COUNT);
        } else if (contentMetadataPresentWithoutViralityMetadata(localDataMap)) {
            Map<String, Object> virality = new HashMap<>();
            ((Map<String, Object>) localDataMap.get(ContentModel.KEY_CONTENT_METADATA)).put(ContentModel.KEY_VIRALITY_METADATA, virality);
            virality.put(ContentModel.KEY_ORIGIN, origin);
            virality.put(ContentModel.KEY_TRANSFER_COUNT, INITIAL_VALUE_FOR_TRANSFER_COUNT);
        } else if (contentAndViralityMetadataPresent(localDataMap)) {
            Map<String, Object> viralityMetadata =
                    (Map<String, Object>) ((Map<String, Object>) localDataMap.get(ContentModel.KEY_CONTENT_METADATA)).get(ContentModel.KEY_VIRALITY_METADATA);
            viralityMetadata.put(ContentModel.KEY_TRANSFER_COUNT, transferCount(localDataMap) + 1);
        }
    }

    public static List<ContentModel> getSortedChildrenList(IDBSession dbSession, String localData, int childContents) {
        Map<String, Object> localDataMap = GsonUtil.fromJson(localData, Map.class);
        String childrenString = GsonUtil.toJson(localDataMap.get("children"));

        // json string to list of child
        Type type = new TypeToken<List<ContentChild>>() {
        }.getType();
        List<ContentChild> contentChildList = GsonUtil.getGson().fromJson(childrenString, type);

        // Sort by index of child content
        Comparator<ContentChild> comparator = new Comparator<ContentChild>() {
            @Override
            public int compare(ContentChild left, ContentChild right) {
                return (int) (left.getIndex() - right.getIndex()); // use your logic
            }
        };
        Collections.sort(contentChildList, comparator);

        List<String> childIdentifiers = new ArrayList<>();
        StringBuilder whenAndThen = new StringBuilder();
        int i = 0;

        for (ContentChild contentChild : contentChildList) {
            childIdentifiers.add(contentChild.getIdentifier());
            whenAndThen.append(String.format(Locale.US, " WHEN '%s' THEN %s ", contentChild.getIdentifier(), i));
            i++;
        }

        String orderBy = "";
        if (i > 0) {
            orderBy = String.format(Locale.US, " ORDER BY CASE %s %s END", ContentEntry.COLUMN_NAME_IDENTIFIER, whenAndThen.toString());
        }

        String filter;
        switch (childContents) {
            case ContentConstants.ChildContents.FIRST_LEVEL_DOWNLOADED:
                filter = String.format(Locale.US, " AND %s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);
                break;

            case ContentConstants.ChildContents.FIRST_LEVEL_SPINE:
                filter = String.format(Locale.US, " AND %s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ONLY_SPINE);
                break;

//            case CHILD_CONTENTS_FIRST_LEVEL_TEXTBOOK_UNIT:
//                filter = String.format(Locale.US, " AND %s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_TYPE, ContentType.TEXTBOOK_UNIT.getValue());
//                break;

            case ContentConstants.ChildContents.FIRST_LEVEL_ALL:
            default:
                filter = "";
                break;
        }

        String query = String.format(Locale.US, "Select * from %s where %s in ('%s') %s %s",
                ContentEntry.TABLE_NAME, ContentEntry.COLUMN_NAME_IDENTIFIER, StringUtil.join("','", childIdentifiers), filter, orderBy);
        List<ContentModel> contentModelListInDB;
        ContentsModel contentsModel = ContentsModel.findWithCustomQuery(dbSession, query);
        if (contentsModel != null) {
            contentModelListInDB = contentsModel.getContentModelList();
        } else {
            contentModelListInDB = new ArrayList<>();
        }

        return contentModelListInDB;
    }

    public static void deleteAllChild(AppContext appContext, ContentModel contentModel, int level) {
        Queue<ContentModel> queue = new LinkedList<>();

        queue.add(contentModel);

        ContentModel node;
        while (!queue.isEmpty()) {
            node = queue.remove();

            if (node.hasChildren()) {
                List<String> childContentsIdentifiers = node.getChildContentsIdentifiers();
                ContentsModel contentsModel = ContentsModel.findAllContentsWithIdentifiers(appContext.getDBSession(), childContentsIdentifiers);
                if (contentsModel != null) {
                    queue.addAll(contentsModel.getContentModelList());
                }
            }

            // Deleting only child content
            if (!contentModel.getIdentifier().equalsIgnoreCase(node.getIdentifier())) {
                deleteOrUpdateContent(node, true, level);
            }
        }
    }

    public static HashMap<String, Object> getRecommendedContentRequest(String language, String did) {
        HashMap<String, Object> contextMap = new HashMap<>();
        contextMap.put("did", did);
        contextMap.put("dlang", language);

        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("context", contextMap);
        requestMap.put("limit", 10);

        return requestMap;
    }

    public static HashMap<String, Object> getRelatedContentRequest(IUserService userService, String contentIdentifier, String did) {
        String dlang = "";
        String uid = "";
        if (userService != null) {
            GenieResponse<Profile> profileGenieResponse = userService.getCurrentUser();
            if (profileGenieResponse.getStatus()) {
                Profile profile = profileGenieResponse.getResult();
                uid = profile.getUid();
                dlang = profile.getLanguage();
            }
        }

        HashMap<String, Object> contextMap = new HashMap<>();
        contextMap.put("did", did);
        contextMap.put("dlang", dlang);
        contextMap.put("contentid", contentIdentifier);
        contextMap.put("uid", uid);

        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("context", contextMap);
        requestMap.put("limit", 10);

        return requestMap;
    }

    public static HashMap<String, Object> getPageAssembleRequest(IUserService userService, String did) {
        String dlang = "";
        String uid = "";
        if (userService != null) {
            GenieResponse<Profile> profileGenieResponse = userService.getCurrentUser();
            if (profileGenieResponse.getStatus()) {
                Profile profile = profileGenieResponse.getResult();
                uid = profile.getUid();
                dlang = profile.getLanguage();
            }
        }

        HashMap<String, Object> contextMap = new HashMap<>();
        contextMap.put("did", did);
        contextMap.put("dlang", dlang);
        contextMap.put("contentid", "");
        contextMap.put("uid", uid);

        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("context", contextMap);
        // TODO: 5/25/2017 - Uncomment this and add filter request. Reffer PageAssembleAPI in Genie repo.
//        requestMap.put("filters", getFilterRequest());

        return requestMap;
    }

    public static String getDownloadUrl(Map<String, Object> dataMap) {
        String downloadUrl = null;

        Object variants = dataMap.get(ContentModel.KEY_VARIANTS);
        if (variants != null) {
            String variantsString;
            if (variants instanceof Map) {
                variantsString = GsonUtil.getGson().toJson(variants);
            } else {
                variantsString = (String) variants;
            }

            variantsString = variantsString.replace("\\", "");
            ContentVariant contentVariant = GsonUtil.fromJson(variantsString, ContentVariant.class);

            String contentType = (String) dataMap.get(ContentModel.KEY_CONTENT_TYPE);
            if ((contentVariant.getSpine() != null && !StringUtil.isNullOrEmpty(contentVariant.getSpine().getEcarUrl()))
                    && (ContentType.TEXTBOOK.getValue().equalsIgnoreCase(contentType)
                    || ContentType.COLLECTION.getValue().equalsIgnoreCase(contentType))) {

                downloadUrl = contentVariant.getSpine().getEcarUrl();
            }
        } else {
            downloadUrl = (String) dataMap.get(ContentModel.KEY_DOWNLOAD_URL);
        }

        return downloadUrl;
    }

}
