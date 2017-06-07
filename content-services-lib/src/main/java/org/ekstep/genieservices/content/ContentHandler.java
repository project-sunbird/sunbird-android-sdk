package org.ekstep.genieservices.content;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.IConfigService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentCriteria;
import org.ekstep.genieservices.commons.bean.ContentData;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.ContentFeedbackCriteria;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.bean.ContentListingResult;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchFilter;
import org.ekstep.genieservices.commons.bean.Display;
import org.ekstep.genieservices.commons.bean.FilterValue;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.MasterDataValues;
import org.ekstep.genieservices.commons.bean.PartnerFilter;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.Section;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.bean.Variant;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.commons.bean.enums.SortBy;
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
import org.ekstep.genieservices.content.db.model.ContentListingModel;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.db.model.ContentsModel;
import org.ekstep.genieservices.content.network.ContentDetailsAPI;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created on 5/23/2017.
 *
 * @author anil
 */
public class ContentHandler {

    private static final String TAG = ContentHandler.class.getSimpleName();

    private static final String KEY_IDENTIFIER = "identifier";
    private static final String KEY_PKG_VERSION = "pkgVersion";
    private static final String KEY_ARTIFACT_URL = "artifactUrl";
    private static final String KEY_APP_ICON = "appIcon";
    private static final String KEY_POSTER_IMAGE = "posterImage";
    private static final String KEY_GRAY_SCALE_APP_ICON = "grayScaleAppIcon";
    private static final String KEY_STATUS = "status";
    private static final String KEY_OBJECT_TYPE = "objectType";
    private static final String KEY_VARIANTS = "variants";
    private static final String KEY_DOWNLOAD_URL = "downloadUrl";
    private static final String KEY_COMPATIBILITY_LEVEL = "compatibilityLevel";
    private static final String KEY_MIME_TYPE = "mimeType";
    private static final String KEY_VISIBILITY = "visibility";
    private static final String KEY_LAST_UPDATED_ON = "lastUpdatedOn";
    private static final String KEY_PRE_REQUISITES = "pre_requisites";
    private static final String KEY_CHILDREN = "children";
    private static final String KEY_CONTENT_TYPE = "contentType";

    private static final String KEY_CONTENT_METADATA = "contentMetadata";
    private static final String KEY_VIRALITY_METADATA = "virality";
    private static final String KEY_TRANSFER_COUNT = "transferCount";
    private static final String KEY_ORIGIN = "origin";

    private static final int DEFAULT_PACKAGE_VERSION = -1;
    private static final int INITIAL_VALUE_FOR_TRANSFER_COUNT = 0;

    public static int minCompatibilityLevel = 1;
    public static int maxCompatibilityLevel = 3;
    // TODO: 02-03-2017 : We can remove this later after few release
    public static int defaultCompatibilityLevel = 1;

    public static ContentModel convertContentMapToModel(IDBSession dbSession, Map contentData, String manifestVersion) {
        String identifier = readIdentifier(contentData);

        String localData = null;
        String serverData = null;
        String serverLastUpdatedOn = null;
        if (StringUtil.isNullOrEmpty(manifestVersion)) {
            serverData = GsonUtil.toJson(contentData);
            serverLastUpdatedOn = serverLastUpdatedOn(contentData);
        } else {
            localData = GsonUtil.toJson(contentData);
        }

        String mimeType = readMimeType(contentData);
        String contentType = readContentType(contentData);
        String visibility = readVisibility(contentData);

        ContentModel contentModel = ContentModel.build(dbSession, identifier, serverData, serverLastUpdatedOn,
                manifestVersion, localData, mimeType, contentType, visibility);

        return contentModel;
    }

    public static String readIdentifier(Map contentData) {
        if (contentData.containsKey(KEY_IDENTIFIER)) {
            return (String) contentData.get(KEY_IDENTIFIER);
        }
        return null;
    }

    public static String readMimeType(Map contentData) {
        if (contentData.containsKey(KEY_MIME_TYPE)) {
            return (String) contentData.get(KEY_MIME_TYPE);
        }
        return null;
    }

    public static String readContentType(Map contentData) {
        if (contentData.containsKey(KEY_CONTENT_TYPE)) {
            String contentType = (String) contentData.get(KEY_CONTENT_TYPE);
            if (!StringUtil.isNullOrEmpty(contentType)) {
                contentType = contentType.toLowerCase();
            }
            return contentType;
        }
        return null;
    }

    public static String readVisibility(Map contentData) {
        if (contentData.containsKey(KEY_VISIBILITY)) {
            return (String) contentData.get(KEY_VISIBILITY);
        }
        return ContentConstants.Visibility.DEFAULT;
    }

    public static String readArtifactUrl(Map contentData) {
        if (contentData.containsKey(KEY_ARTIFACT_URL)) {
            return (String) contentData.get(KEY_ARTIFACT_URL);
        }
        return null;
    }

    public static String readAppIcon(Map contentData) {
        if (contentData.containsKey(KEY_APP_ICON)) {
            return (String) contentData.get(KEY_APP_ICON);
        }
        return null;
    }

    public static String readPosterImage(Map contentData) {
        if (contentData.containsKey(KEY_POSTER_IMAGE)) {
            return (String) contentData.get(KEY_POSTER_IMAGE);
        }
        return null;
    }

    public static String readGrayScaleAppIcon(Map contentData) {
        if (contentData.containsKey(KEY_GRAY_SCALE_APP_ICON)) {
            return (String) contentData.get(KEY_GRAY_SCALE_APP_ICON);
        }
        return null;
    }

    public static Double readCompatibilityLevel(Map contentData) {
        return (contentData.get(KEY_COMPATIBILITY_LEVEL) != null) ? (Double) contentData.get(KEY_COMPATIBILITY_LEVEL) : defaultCompatibilityLevel;
    }

    public static String readStatus(Map contentData) {
        if (contentData.containsKey(KEY_STATUS)) {
            return (String) contentData.get(KEY_STATUS);
        }
        return null;
    }

    public static String readObjectType(Map contentData) {
        if (contentData.containsKey(KEY_OBJECT_TYPE)) {
            return (String) contentData.get(KEY_OBJECT_TYPE);
        }
        return null;
    }

    private static String serverLastUpdatedOn(Map serverData) {
        return (String) serverData.get(KEY_LAST_UPDATED_ON);
    }

    private static Double pkgVersion(String localData) {
        return readPkgVersion(GsonUtil.fromJson(localData, Map.class));
    }

    public static Double readPkgVersion(Map contentData) {
        return (Double) contentData.get(KEY_PKG_VERSION);
    }

    public static boolean hasPreRequisites(String localData) {
        return GsonUtil.fromJson(localData, Map.class).get(KEY_PRE_REQUISITES) != null;
    }

    private static List<String> getPreRequisitesIdentifiers(String localData) {
        List<Map> children = (List) GsonUtil.fromJson(localData, Map.class).get(KEY_PRE_REQUISITES);

        List<String> childIdentifiers = new ArrayList<>();
        for (Map child : children) {
            String childIdentifier = readIdentifier(child);
            childIdentifiers.add(childIdentifier);
        }

        // Return the pre_requisites in DB
        return childIdentifiers;
    }

    public static boolean hasChildren(String localData) {
        return GsonUtil.fromJson(localData, Map.class).get(KEY_CHILDREN) != null;
    }

    private static List<String> getChildContentsIdentifiers(String localData) {
        List<Map> children = (List) GsonUtil.fromJson(localData, Map.class).get(KEY_CHILDREN);

        List<String> childIdentifiers = new ArrayList<>();
        for (Map child : children) {
            String childIdentifier = readIdentifier(child);
            childIdentifiers.add(childIdentifier);
        }

        // Return the childrenInDB
        return childIdentifiers;
    }

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

    public static void refreshContentDetailsFromServer(final AppContext appContext, final String contentIdentifier, final ContentModel existingContentModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map contentData = fetchContentDetailsFromServer(appContext, contentIdentifier);

                if (contentData != null) {
                    ContentModel contentModel = convertContentMapToModel(appContext.getDBSession(), contentData, null);
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

        ContentData serverData = null;
        if (contentModel.getServerData() != null) {
            serverData = GsonUtil.fromJson(contentModel.getServerData(), ContentData.class);
        }

        if (serverData != null) {
            content.setContentData(serverData);
            addContentVariants(serverData, contentModel.getServerData());
        } else if (localData != null) {
            content.setContentData(localData);
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

        Map<String, Object> dataMap = GsonUtil.fromJson(dataJson, Map.class);

        Object variants = dataMap.get(KEY_VARIANTS);
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

    public static Profile getCurrentProfile(IUserService userService) {
        Profile profile = null;
        if (userService != null) {
            GenieResponse<Profile> profileGenieResponse = userService.getCurrentUser();
            if (profileGenieResponse.getStatus()) {
                profile = profileGenieResponse.getResult();
            }
        }
        return profile;
    }

    public static ContentFeedback getContentFeedback(IContentFeedbackService contentFeedbackService, ContentFeedbackCriteria contentFeedbackCriteria) {
        if (contentFeedbackService != null) {
            return contentFeedbackService.getFeedback(contentFeedbackCriteria).getResult();
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

        return new ContentAccess();
    }

    private static List<ContentAccess> getAllContentAccess(IUserService userService, String uid, String contentIdentifier) {
        ContentAccessFilterCriteria.Builder builder = new ContentAccessFilterCriteria.Builder();
        builder.uid(uid);
        builder.contentId(contentIdentifier);
        return userService.getAllContentAccess(builder.build()).getResult();
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

    public static List<ContentModel> getAllLocalContentSortedByContentAccess(IDBSession dbSession, ContentCriteria criteria) {
        String uid = null;
        ContentType[] contentTypes = null;
        if (criteria != null) {
            uid = criteria.getUid();
            contentTypes = criteria.getContentTypes();
        }
        String contentTypesStr = getCommaSeparatedContentTypes(contentTypes);

        String isContentType = String.format(Locale.US, "c.%s in ('%s')", ContentEntry.COLUMN_NAME_CONTENT_TYPE, contentTypesStr);
        String isVisible = String.format(Locale.US, "c.%s = '%s'", ContentEntry.COLUMN_NAME_VISIBILITY, ContentConstants.Visibility.DEFAULT);
        String isArtifactAvailable = String.format(Locale.US, "c.%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);
        String filter = String.format(Locale.US, "WHERE (%s AND %s AND %s)", isVisible, isArtifactAvailable, isContentType);

        String orderBy = String.format(Locale.US, "ORDER BY ca.%s desc, c.%s desc, c.%s desc", ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP, ContentEntry.COLUMN_NAME_LOCAL_LAST_UPDATED_ON, ContentEntry.COLUMN_NAME_SERVER_LAST_UPDATED_ON);

        String query = String.format(Locale.US, "SELECT c.* FROM  %s c LEFT JOIN %s ca ON c.%s = ca.%s AND ca.%s = '%s' %s %s;",
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

    public static void deleteOrUpdateContent(ContentModel contentModel, boolean isChildItems, boolean isChildContent) {
        int refCount = contentModel.getRefCount();
        int contentState;
        String visibility = contentModel.getVisibility();

        if (isChildContent) {
            // If visibility is Default it means this content was visible in my downloads.
            // After deleting artifact for this content it should not visible as well so reduce the refCount also for this.
            if (refCount > 1 && ContentConstants.Visibility.DEFAULT.equalsIgnoreCase(contentModel.getVisibility())) {
                refCount = refCount - 1;

                // Update visibility
                visibility = ContentConstants.Visibility.PARENT;
            }

            // Update the contentState
            // Do not update the content state if contentType is Collection / TextBook / TextBookUnit
            if (ContentType.COLLECTION.getValue().equalsIgnoreCase(contentModel.getContentType())
                    || ContentType.TEXTBOOK.getValue().equalsIgnoreCase(contentModel.getContentType())
                    || ContentType.TEXTBOOK_UNIT.getValue().equalsIgnoreCase(contentModel.getContentType())) {
                contentState = ContentConstants.State.ARTIFACT_AVAILABLE;
            } else {
                contentState = ContentConstants.State.ONLY_SPINE;
            }

        } else {
            // TODO: This check should be before updating the existing refCount.
            // Do not update the content state if contentType is Collection / TextBook / TextBookUnit and refCount is more than 1.
            if ((ContentType.COLLECTION.getValue().equalsIgnoreCase(contentModel.getContentType())
                    || ContentType.TEXTBOOK.getValue().equalsIgnoreCase(contentModel.getContentType())
                    || ContentType.TEXTBOOK_UNIT.getValue().equalsIgnoreCase(contentModel.getContentType()))
                    && refCount > 1) {
                contentState = ContentConstants.State.ARTIFACT_AVAILABLE;
            } else if (refCount > 1 && isChildItems) {  //contentModel.isVisibilityDefault() &&
                // Visibility will remain Default only.

                contentState = ContentConstants.State.ARTIFACT_AVAILABLE;
            } else {

                // Set the visibility to Parent so that this content will not visible in My contents / Downloads section.
                // Update visibility
                if (ContentConstants.Visibility.DEFAULT.equalsIgnoreCase(contentModel.getVisibility())) {
                    visibility = ContentConstants.Visibility.PARENT;
                }

                contentState = ContentConstants.State.ONLY_SPINE;
            }

            refCount = refCount - 1;
        }

        // if there are no entry in DB for any content then on this case contentModel.getPath() will be null
        if (contentModel.getPath() != null) {
            if (contentState == ContentConstants.State.ONLY_SPINE) {
                FileUtil.rm(new File(contentModel.getPath()), contentModel.getIdentifier());
            }

            contentModel.setVisibility(visibility);
            // Update the refCount
            contentModel.addOrUpdateRefCount(refCount);
            contentModel.addOrUpdateContentState(contentState);

            contentModel.update();
        }
    }

    public static void deleteAllPreRequisites(AppContext appContext, ContentModel contentModel, boolean isChildContent) {
        List<String> preRequisitesIdentifier = getPreRequisitesIdentifiers(contentModel.getLocalData());
        List<ContentModel> contentModelListInDB = findAllContentsWithIdentifiers(appContext.getDBSession(), preRequisitesIdentifier);

        if (contentModelListInDB != null) {
            for (ContentModel c : contentModelListInDB) {
                deleteOrUpdateContent(c, true, isChildContent);
            }
        }
    }

    public static void deleteAllChild(AppContext appContext, ContentModel contentModel, boolean isChildContent) {
        Queue<ContentModel> queue = new LinkedList<>();

        queue.add(contentModel);

        ContentModel node;
        while (!queue.isEmpty()) {
            node = queue.remove();

            if (hasChildren(node.getLocalData())) {
                List<String> childContentsIdentifiers = getChildContentsIdentifiers(node.getLocalData());
                List<ContentModel> contentModelListInDB = findAllContentsWithIdentifiers(appContext.getDBSession(), childContentsIdentifiers);
                if (contentModelListInDB != null) {
                    queue.addAll(contentModelListInDB);
                }
            }

            // Deleting only child content
            if (!contentModel.getIdentifier().equalsIgnoreCase(node.getIdentifier())) {
                deleteOrUpdateContent(node, true, isChildContent);
            }
        }
    }

    private static List<ContentModel> findAllContentsWithIdentifiers(IDBSession dbSession, List<String> identifiers) {
        String filter = String.format(Locale.US, " where %s in ('%s') ", ContentEntry.COLUMN_NAME_IDENTIFIER, StringUtil.join("','", identifiers));

        List<ContentModel> contentModelListInDB = null;
        ContentsModel contentsModel = ContentsModel.find(dbSession, filter);
        if (contentsModel != null) {
            contentModelListInDB = contentsModel.getContentModelList();
        }

        return contentModelListInDB;
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
                isExist = pkgVersion(oldContentModel.getLocalData()) >= pkgVersion(newContentModel.getLocalData());
            }
        } catch (Exception e) {
            Logger.e(TAG, "isImportFileExist", e);
        }

        return isExist;
    }

    /**
     * To Check whether the content is exist or not.
     *
     * @param oldContent    Old ContentModel
     * @param newIdentifier New content identifier
     * @return True - if file exists, False- does not exists
     */
    public static boolean isContentExist(ContentModel oldContent, String newIdentifier, Double newPkgVersion) {
        if (oldContent == null) {
            return false;
        }

        boolean isExist = false;
        try {
            String oldIdentifier = oldContent.getIdentifier();
            if (oldIdentifier.equalsIgnoreCase(newIdentifier)) {
                isExist = (pkgVersion(oldContent.getLocalData()) >= newPkgVersion) // If old content's pkgVersion is less than the new content then return false.
                        || oldContent.getContentState() == ContentConstants.State.ARTIFACT_AVAILABLE;  // If content_state is other than artifact available then also return  false.
            }
        } catch (Exception e) {
            Logger.e(TAG, "isContentExist", e);
        }

        return isExist;
    }

    public static String readOriginFromContentMap(Map contentData) {
        try {
            Map contentMetadataMap = (Map) contentData.get(KEY_CONTENT_METADATA);
            if (contentMetadataMap != null) {
                Map viralityMetadataMap = (Map) contentMetadataMap.get(KEY_VIRALITY_METADATA);
                return (String) viralityMetadataMap.get(KEY_ORIGIN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int readTransferCountFromContentMap(Map contentData) {
        try {
            Map contentMetadataMap = (Map) contentData.get(KEY_CONTENT_METADATA);
            if (contentMetadataMap != null) {
                Map viralityMetadataMap = (Map) contentMetadataMap.get(KEY_VIRALITY_METADATA);
                Double count = (Double) viralityMetadataMap.get(KEY_TRANSFER_COUNT);
                return count.intValue();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void addOrUpdateViralityMetadata(Map<String, Object> localDataMap, String origin) {
        if (isContentMetadataAbsent(localDataMap)) {
            Map<String, Object> viralityMetadata = new HashMap<>();
            viralityMetadata.put(KEY_ORIGIN, origin);
            viralityMetadata.put(KEY_TRANSFER_COUNT, INITIAL_VALUE_FOR_TRANSFER_COUNT);

            Map<String, Object> contentMetadata = new HashMap<>();
            contentMetadata.put(KEY_VIRALITY_METADATA, viralityMetadata);

            localDataMap.put(KEY_CONTENT_METADATA, contentMetadata);
        } else if (isContentMetadataPresentWithoutViralityMetadata(localDataMap)) {
            Map<String, Object> viralityMetadata = new HashMap<>();
            viralityMetadata.put(KEY_ORIGIN, origin);
            viralityMetadata.put(KEY_TRANSFER_COUNT, INITIAL_VALUE_FOR_TRANSFER_COUNT);

            ((Map<String, Object>) localDataMap.get(KEY_CONTENT_METADATA)).put(KEY_VIRALITY_METADATA, viralityMetadata);
        } else {
            Map<String, Object> viralityMetadata = (Map<String, Object>) ((Map<String, Object>) localDataMap.get(KEY_CONTENT_METADATA)).get(KEY_VIRALITY_METADATA);
            viralityMetadata.put(KEY_TRANSFER_COUNT, transferCount(localDataMap) + 1);
        }
    }

    private static boolean isContentMetadataAbsent(Map<String, Object> localDataMap) {
        return localDataMap.get(KEY_CONTENT_METADATA) == null;
    }

    private static boolean isContentMetadataPresentWithoutViralityMetadata(Map<String, Object> localDataMap) {
        return localDataMap.get(KEY_CONTENT_METADATA) != null
                && ((Map<String, Object>) localDataMap.get(KEY_CONTENT_METADATA)).get(KEY_VIRALITY_METADATA) == null;
    }

    private static int transferCount(Map<String, Object> viralityMetadata) {
        try {
            String transferCount = (String) viralityMetadata.get(KEY_TRANSFER_COUNT);
            return Double.valueOf(transferCount).intValue();
        } catch (Exception e) {
            return 0;
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

    public static Map<String, Object> getSearchRequest(List<String> contentIdentifiers) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("compatibilityLevel", getCompatibilityLevelFilter());
        filterMap.put("identifier", contentIdentifiers);
        addFiltersIfNotAvailable(filterMap, "objectType", Collections.singletonList("Content"));
        addFiltersIfNotAvailable(filterMap, "contentType", Arrays.asList("Story", "Worksheet", "Collection", "Game", "TextBook"));
        addFiltersIfNotAvailable(filterMap, "status", Collections.singletonList("Live"));

        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("filters", filterMap);
        requestMap.put("fields", Arrays.asList("downloadUrl", "variants"));

        return requestMap;
    }

    public static Map<String, Object> getSearchRequest(IUserService userService, IConfigService configService, ContentSearchCriteria criteria) {
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("query", criteria.getQuery());
        requestMap.put("filters", getFilterRequest(userService, configService, criteria));
        requestMap.put("limit", criteria.getLimit());
        requestMap.put("sort_by", getSortByMap(criteria.getSortBy()));
        requestMap.put("mode", criteria.getMode());
        requestMap.put("facets", Arrays.asList("contentType", "domain", "ageGroup", "language", "gradeLevel"));

        return requestMap;
    }

    private static Map<String, String> getSortByMap(String sortBy) {
        Map<String, String> sortMap = new HashMap<>();

        if (!StringUtil.isNullOrEmpty(sortBy)) {
            if (SortBy.NAME.getValue().equals(sortBy)) {
                sortMap.put("name", "asc");
            } else if (SortBy.MOST_POPULAR.getValue().equals(sortBy)) {
                sortMap.put("popularity", "desc");
            } else if (SortBy.NEWEST.getValue().equals(sortBy)) {
                sortMap.put("lastPublishedOn", "desc");
            }
        }

        return sortMap;
    }

    private static Map<String, Object> getFilterRequest(IUserService userService, IConfigService configService, ContentSearchCriteria criteria) {
        Map<String, Object> filterMap = new HashMap<>();

        if (criteria.getFilters() != null) {
            for (ContentSearchFilter filter : criteria.getFilters()) {
                List<String> filterValueList = new ArrayList<>();
                for (FilterValue filterValue : filter.getValues()) {
                    if (filterValue.isApply()) {
                        filterValueList.add(filterValue.getName());
                    }
                }

                if (!filterValueList.isEmpty()) {
                    filterMap.put(filter.getName(), filterValueList);
                }
            }
        }

        // Populating implicit search criteria.
        filterMap.put("compatibilityLevel", getCompatibilityLevelFilter());
        addFiltersIfNotAvailable(filterMap, "objectType", Collections.singletonList("Content"));
        addFiltersIfNotAvailable(filterMap, "contentType", Arrays.asList("Story", "Worksheet", "Collection", "Game", "TextBook"));
        addFiltersIfNotAvailable(filterMap, "status", Collections.singletonList("Live"));

        // Apply profile specific filters
        if (criteria.isProfileFilter()) {
            applyProfileFilter(getCurrentProfile(userService), configService, filterMap);
        }

        // Apply partner specific filters
        applyPartnerFilter(configService, criteria.getPartnerFilters(), filterMap);

        return filterMap;
    }

    private static Map<String, Integer> getCompatibilityLevelFilter() {
        Map<String, Integer> compatibilityLevelMap = new HashMap<>();
        compatibilityLevelMap.put("max", maxCompatibilityLevel);
        compatibilityLevelMap.put("min", minCompatibilityLevel);
        return compatibilityLevelMap;
    }

    private static void addFiltersIfNotAvailable(Map<String, Object> filterMap, String key, List<String> values) {
        if (filterMap.isEmpty() || filterMap.get(key) == null) {
            filterMap.put(key, values);
        }
    }

    private static void applyProfileFilter(Profile profile, IConfigService configService, Map<String, Object> filterMap) {
        if (profile != null) {
            // Add age filter
            applyFilter(configService, MasterDataType.AGEGROUP, String.valueOf(profile.getAge()), filterMap);

            // Add board filter
            applyFilter(configService, MasterDataType.BOARD, profile.getBoard(), filterMap);

            // Add medium filter
            applyFilter(configService, MasterDataType.MEDIUM, profile.getMedium(), filterMap);

            // Add standard filter
            applyFilter(configService, MasterDataType.GRADELEVEL, String.valueOf(profile.getStandard()), filterMap);
        }
    }

    private static void applyPartnerFilter(IConfigService configService, List<PartnerFilter> partnerFilters, Map<String, Object> filterMap) {
        if (configService != null && partnerFilters != null && !partnerFilters.isEmpty()) {
            for (PartnerFilter partnerFilter : partnerFilters) {
                for (String propertyValue : partnerFilter.getValues()) {
                    applyFilter(configService, partnerFilter.getMasterDataType(), propertyValue, filterMap);
                }
            }
        }
    }

    private static void applyFilter(IConfigService configService, MasterDataType masterDataType, String propertyValue, Map<String, Object> filterMap) {
        if (configService != null && propertyValue != null) {
            String property = masterDataType.getValue();

            if (masterDataType == MasterDataType.AGEGROUP) {
                masterDataType = MasterDataType.AGE;
            }

            GenieResponse<MasterData> masterDataResponse = configService.getMasterData(masterDataType);

            if (masterDataResponse.getStatus()) {
                MasterData masterData = masterDataResponse.getResult();

                for (MasterDataValues values : masterData.getValues()) {
                    if (values.getTelemetry().equals(propertyValue)) {
                        Map<String, Object> searchMap = values.getSearch();
                        Map filtersMap = (Map) searchMap.get("filters");
                        Set termSet = new HashSet((List) filtersMap.get(property));

                        if (filterMap.containsKey(property)) {
                            if (filterMap.get(property) != null) {
                                Set set = new HashSet(Arrays.asList(filterMap.get(property)));
                                if (set != null && termSet != null) {
                                    termSet.addAll(set);
                                }
                            }
                        }

                        String[] strArr = new String[termSet.size()];
                        termSet.toArray(strArr);
                        filterMap.put(property, strArr);
                        break;
                    }
                }
            }
        }
    }

    public static List<ContentSearchFilter> getFilters(IConfigService configService, List<Map<String, Object>> facets, Map<String, Object> appliedFilterMap) {
        List<ContentSearchFilter> filters = new ArrayList<>();

        if (facets == null) {
            return filters;
        }

        Map<String, Object> ordinalsMap = null;
        if (configService != null) {
            GenieResponse<Map<String, Object>> ordinalsResponse = configService.getOrdinals();
            if (ordinalsResponse.getStatus()) {
                ordinalsMap = ordinalsResponse.getResult();
            }
        }

        for (Map<String, Object> facetMap : facets) {
            ContentSearchFilter filter = new ContentSearchFilter();
            String facetName = null;
            if (facetMap.containsKey("name")) {
                facetName = (String) facetMap.get("name");
            }

            List<Map<String, Object>> facetValues = null;
            if (facetMap.containsKey("values")) {
                facetValues = (List<Map<String, Object>>) facetMap.get("values");
            }

            List<String> facetOrder = null;
            if (ordinalsMap != null && ordinalsMap.containsKey(facetName)) {
                facetOrder = (List<String>) ordinalsMap.get(facetName);
            }

            List<String> appliedFilter = null;
            if (appliedFilterMap != null) {
                appliedFilter = (List<String>) appliedFilterMap.get(facetName);
            }

            List<FilterValue> values = getSortedFilterValuesWithAppliedFilters(facetValues, facetOrder, appliedFilter);

            if (!StringUtil.isNullOrEmpty(facetName)) {
                filter.setName(facetName);
                filter.setValues(values);

                // Set the filter
                filters.add(filter);
            }
        }

        return filters;
    }

    private static List<FilterValue> getSortedFilterValuesWithAppliedFilters(List<Map<String, Object>> facetValues, List<String> facetOrder, List<String> appliedFilter) {
        Map<Integer, FilterValue> map = new TreeMap<>();

        for (Map<String, Object> valueMap : facetValues) {
            String name = (String) valueMap.get("name");
            int index = indexOf(facetOrder, name);

            boolean applied = false;
            if (appliedFilter != null && appliedFilter.contains(name)) {
                applied = true;
            }

            FilterValue filterValue = new FilterValue();
            filterValue.setName(name);
            filterValue.setApply(applied);
            if (valueMap.containsKey("count")) {
                Double count = (Double) valueMap.get("count");
                filterValue.setCount(count.intValue());
            }

            map.put(index, filterValue);
        }

        List<FilterValue> valuesList = new ArrayList<>(map.values());
        return valuesList;
    }

    private static int indexOf(List<String> facetsOrder, String key) {
        if (!StringUtil.isNullOrEmpty(key)) {
            for (int i = 0; i < facetsOrder.size(); i++) {
                if (key.equalsIgnoreCase(facetsOrder.get(i))) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static HashMap<String, Object> getRecommendedContentRequest(RecommendedContentRequest request, String did) {
        HashMap<String, Object> contextMap = new HashMap<>();
        contextMap.put("did", did);
        contextMap.put("dlang", request.getLanguage());

        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("context", contextMap);
        requestMap.put("limit", request.getLimit());

        return requestMap;
    }

    public static HashMap<String, Object> getRelatedContentRequest(IUserService userService, RelatedContentRequest request, String did) {
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
        contextMap.put("contentid", request.getContentId());
        contextMap.put("uid", uid);

        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("context", contextMap);
        requestMap.put("limit", request.getLimit());

        return requestMap;
    }

    public static boolean dataHasExpired(long ttl) {
        Long currentTime = DateUtil.getEpochTime();
        return currentTime > ttl;
    }

    public static Map<String, Object> getContentListingRequest(IConfigService configService, ContentListingCriteria contentListingCriteria, String did) {
        HashMap<String, Object> contextMap = new HashMap<>();

        Profile profile = contentListingCriteria.getProfile();
        if (profile != null) {
            contextMap.put("uid", profile.getUid());
            contextMap.put("dlang", profile.getLanguage());
        }
        contextMap.put("did", did);
        contextMap.put("contentid", "");

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("compatibilityLevel", getCompatibilityLevelFilter());

        // Add subject filter
        applyFilter(configService, MasterDataType.SUBJECT, contentListingCriteria.getSubject(), filterMap);

        // Apply profile specific filters
        applyProfileFilter(profile, configService, filterMap);

        // Apply partner specific filters
        applyPartnerFilter(configService, contentListingCriteria.getPartnerFilters(), filterMap);

        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("context", contextMap);
        requestMap.put("filters", filterMap);

        return requestMap;
    }

    public static void saveContentListingDataInDB(IDBSession dbSession, ContentListingCriteria contentListingCriteria, Profile profile, String jsonStr) {
        if (jsonStr == null) {
            return;
        }

        long expiryTime;

        long ttlInMilliSeconds = ContentConstants.CACHE_TIMEOUT_HOME_CONTENT;
        Long currentTime = DateUtil.getEpochTime();
        expiryTime = ttlInMilliSeconds + currentTime;

        ContentListingModel contentListingModel = ContentListingModel.build(dbSession, contentListingCriteria.getContentListingId(), jsonStr, profile, contentListingCriteria.getSubject(), expiryTime);
        contentListingModel.save();
    }

    public static ContentListingResult getContentListingResult(IDBSession dbSession, ContentListingCriteria contentListingCriteria, String jsonStr) {
        ContentListingResult contentListingResult = null;

        LinkedTreeMap map = GsonUtil.fromJson(jsonStr, LinkedTreeMap.class);
        LinkedTreeMap responseParams = (LinkedTreeMap) map.get("params");
        LinkedTreeMap result = (LinkedTreeMap) map.get("result");

        String responseMessageId = null;
        if (responseParams.containsKey("resmsgid")) {
            responseMessageId = (String) responseParams.get("resmsgid");
        }

        if (result != null) {
            contentListingResult = new ContentListingResult();
            contentListingResult.setId(contentListingCriteria.getContentListingId());
            contentListingResult.setResponseMessageId(responseMessageId);
            if (result.containsKey("page")) {
                contentListingResult.setSections(getSectionsFromPageMap(dbSession, (Map<String, Object>) result.get("page"), contentListingCriteria));
            }
        }

        return contentListingResult;
    }

    private static List<Section> getSectionsFromPageMap(IDBSession dbSession, Map<String, Object> pageMap, ContentListingCriteria contentListingCriteria) {
        List<Section> sectionList = new ArrayList<>();

        List<Map<String, Object>> sections = (List<Map<String, Object>>) pageMap.get("sections");
        for (Map<String, Object> sectionMap : sections) {
            List<Map<String, Object>> contentDataList = null;
            if (sectionMap.containsKey("contents")) {
                contentDataList = (List<Map<String, Object>>) sectionMap.get("contents");
            }

            ContentSearchCriteria contentSearchCriteria = null;
            Map<String, Object> searchMap = (Map<String, Object>) sectionMap.get("search");
            if (searchMap != null) {
                ContentSearchCriteria.Builder builder = new ContentSearchCriteria.Builder();
                if (searchMap.containsKey("query")) {
                    builder.query((String) searchMap.get("query"));
                }

                if (searchMap.containsKey("mode")) {
                    builder.mode((String) searchMap.get("mode"));
                }

                if (searchMap.containsKey("sort_by")) {
                    // TODO: 5/30/2017
                }

                if (searchMap.containsKey("filters")) {
                    Map<String, String[]> filtersMap = (Map<String, String[]>) searchMap.get("filters");
                    if (filtersMap != null && !filtersMap.isEmpty()) {

                        List<ContentSearchFilter> filters = new ArrayList<>();

                        Iterator it = filtersMap.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            String key = pair.getKey().toString();
                            Object value = pair.getValue();

                            if (value instanceof List) {
                                List<FilterValue> values = new ArrayList<>();
                                List<String> valueList = (List<String>) value;
                                for (String v : valueList) {
                                    FilterValue filterValue = new FilterValue();
                                    filterValue.setName(v);
                                    filterValue.setApply(true);

                                    values.add(filterValue);
                                }

                                ContentSearchFilter contentSearchFilter = new ContentSearchFilter();
                                contentSearchFilter.setName(key);
                                contentSearchFilter.setValues(values);

                                filters.add(contentSearchFilter);
                            } else {
                                // TODO: 5/30/2017 - handle object filter here.
//                                key.equals("compatibilityLevel") && key.equals("genieScore")
//                                String[] stringArray = mFilterMap.get(values.getName());
//                                filterSet.addAll(Arrays.asList(stringArray));
                            }

                            it.remove();
                        }

                        builder.applyFilters(filters);
                        builder.applyCurrentProfile(contentListingCriteria.getProfile() != null);
                        builder.applyPartnerFilters(contentListingCriteria.getPartnerFilters());
                    }
                }

                contentSearchCriteria = builder.build();
            }

            Section section = new Section();
            section.setResponseMessageId((String) sectionMap.get("resmsgid"));
            section.setApiId((String) sectionMap.get("apiid"));
            section.setDisplay(GsonUtil.fromMap((Map) sectionMap.get("display"), Display.class));
            section.setContents(convertContentMapListToBeanList(dbSession, contentDataList));
            section.setContentSearchCriteria(contentSearchCriteria);

            sectionList.add(section);
        }

        return sectionList;
    }

    public static List<Content> convertContentMapListToBeanList(IDBSession dbSession, List<Map<String, Object>> contentDataList) {
        List<Content> contents = new ArrayList<>();
        if (contentDataList != null) {
            for (Map contentDataMap : contentDataList) {
                ContentModel contentModel = convertContentMapToModel(dbSession, contentDataMap, null);
                Content content = convertContentModelToBean(contentModel);
                contents.add(content);
            }
        }
        return contents;
    }

    public static String getDownloadUrl(Map<String, Object> dataMap) {
        String downloadUrl = null;

        Object variants = dataMap.get(KEY_VARIANTS);
        if (variants != null) {
            String variantsString;
            if (variants instanceof Map) {
                variantsString = GsonUtil.getGson().toJson(variants);
            } else {
                variantsString = (String) variants;
            }

            variantsString = variantsString.replace("\\", "");
            ContentVariant contentVariant = GsonUtil.fromJson(variantsString, ContentVariant.class);

            String contentType = (String) dataMap.get(KEY_CONTENT_TYPE);
            if ((contentVariant.getSpine() != null && !StringUtil.isNullOrEmpty(contentVariant.getSpine().getEcarUrl()))
                    && (ContentType.TEXTBOOK.getValue().equalsIgnoreCase(contentType)
                    || ContentType.COLLECTION.getValue().equalsIgnoreCase(contentType))) {

                downloadUrl = contentVariant.getSpine().getEcarUrl();
            }
        } else {
            downloadUrl = (String) dataMap.get(KEY_DOWNLOAD_URL);
        }

        return downloadUrl;
    }

}
