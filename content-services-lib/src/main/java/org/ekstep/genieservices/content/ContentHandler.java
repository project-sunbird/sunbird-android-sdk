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
import org.ekstep.genieservices.commons.bean.ContentData;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.ContentFeedbackFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentListing;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.bean.ContentListingSection;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchFilter;
import org.ekstep.genieservices.commons.bean.ContentVariant;
import org.ekstep.genieservices.commons.bean.FilterValue;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.MasterDataValues;
import org.ekstep.genieservices.commons.bean.PartnerFilter;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.UserSession;
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
import org.ekstep.genieservices.content.db.model.ContentListingModel;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.db.model.ContentsModel;
import org.ekstep.genieservices.content.network.ContentDetailsAPI;
import org.ekstep.genieservices.content.network.ContentListingAPI;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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

    public static final String KEY_VISIBILITY = "visibility";
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
    private static final Object AUDIENCE_KEY = "audience";
    private static final String KEY_LAST_UPDATED_ON = "lastUpdatedOn";
    private static final String KEY_PRE_REQUISITES = "pre_requisites";
    private static final String KEY_CHILDREN = "children";
    private static final String KEY_CONTENT_TYPE = "contentType";
    private static final String KEY_NAME = "name";

    private static final String KEY_CONTENT_METADATA = "contentMetadata";
    private static final String KEY_VIRALITY_METADATA = "virality";
    private static final String KEY_TRANSFER_COUNT = "transferCount";
    private static final String KEY_ORIGIN = "origin";

    private static final int DEFAULT_PACKAGE_VERSION = -1;
    private static final int INITIAL_VALUE_FOR_TRANSFER_COUNT = 0;
    private static final int MAX_CONTENT_NAME = 30;
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
        String audience = readAudience(contentData);

        ContentModel contentModel = ContentModel.build(dbSession, identifier, serverData, serverLastUpdatedOn,
                manifestVersion, localData, mimeType, contentType, visibility, audience);

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

    public static String readName(Map contentData) {
        if (contentData.containsKey(KEY_NAME)) {
            return (String) contentData.get(KEY_NAME);
        }
        return null;
    }

    public static String readVisibility(Map contentData) {
        if (contentData.containsKey(KEY_VISIBILITY)) {
            return (String) contentData.get(KEY_VISIBILITY);
        }
        return ContentConstants.Visibility.DEFAULT;
    }

    public static String readAudience(Map contentData) {
        ArrayList<String> audienceList = null;
        if (contentData.containsKey(AUDIENCE_KEY)) {
            Object o = contentData.get(AUDIENCE_KEY);
            if (o instanceof String) {
                audienceList = new ArrayList<>();
                audienceList.add((String) o);
            } else {
                audienceList = (ArrayList<String>) contentData.get(AUDIENCE_KEY);
            }
        }
        if (audienceList == null) {
            audienceList = new ArrayList<>();
        }
        if (audienceList.isEmpty()) {
            audienceList.add("learner");
        }
        Collections.sort(audienceList);
        return StringUtil.join(",", audienceList);
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

    public static List<String> getPreRequisitesIdentifiers(String localData) {
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

    public static List<String> getChildContentsIdentifiers(String localData) {
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
                    existingContentModel.setServerData(GsonUtil.toJson(contentData));
                    existingContentModel.setServerLastUpdatedOn(serverLastUpdatedOn(contentData));
                    existingContentModel.setAudience(readAudience(contentData));
                    existingContentModel.update();
                }
            }
        }).start();
    }

    public static Content convertContentModelToBean(ContentModel contentModel) {
        Content content = new Content();
        content.setIdentifier(contentModel.getIdentifier());
        ContentData localData = null, serverData = null;
        if (contentModel.getLocalData() != null) {
            localData = GsonUtil.fromJson(contentModel.getLocalData(), ContentData.class);
            localData.setVariants(getContentVariants(contentModel.getLocalData()));
            content.setContentData(localData);
        } else if (contentModel.getServerData() != null) {
            serverData = GsonUtil.fromJson(contentModel.getServerData(), ContentData.class);
            serverData.setVariants(getContentVariants(contentModel.getServerData()));
            content.setContentData(serverData);
        }

        content.setUpdateAvailable(isUpdateAvailable(serverData, localData));
        content.setMimeType(contentModel.getMimeType());
        content.setBasePath(contentModel.getPath());
        content.setContentType(contentModel.getContentType());
        content.setAvailableLocally(isAvailableLocally(contentModel.getContentState()));
        content.setReferenceCount(contentModel.getRefCount());

        long contentCreationTime = 0;
        String localLastUpdatedTime = contentModel.getLocalLastUpdatedTime();
        if (!StringUtil.isNullOrEmpty(localLastUpdatedTime)) {
            contentCreationTime = DateUtil.getTime(localLastUpdatedTime);
        }
        content.setLastUpdatedTime(contentCreationTime);

        return content;
    }

    private static List<ContentVariant> getContentVariants(String dataJson) {
        List<ContentVariant> contentVariantList = new ArrayList<>();

        Map<String, Object> dataMap = GsonUtil.fromJson(dataJson, Map.class);
        ContentVariant spineContentVariant = getVariant(dataMap, "spine");
        if (spineContentVariant != null) contentVariantList.add(spineContentVariant);
        return contentVariantList;
    }

    private static ContentVariant getVariant(Map contentDataMap, String variantName) {
        Object variants = contentDataMap.get(KEY_VARIANTS);
        ContentVariant contentVariant = null;
        if (variants != null) {
            Map variantData = null;
            if (variants instanceof Map) {
                variantData = (Map) variants;
            } else {
                variantData = GsonUtil.fromJson(((String) variants).replace("\\", ""), Map.class);
            }

            if (variantData.get(variantName) != null) {
                Map spineData = (Map) variantData.get(variantName);
                contentVariant = new ContentVariant(variantName, spineData.get("ecarUrl").toString(), spineData.get("size").toString());
            }
        }
        return contentVariant;
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

    public static List<ContentFeedback> getContentFeedback(IContentFeedbackService contentFeedbackService, String contentIdentifier, String uid) {
        if (contentFeedbackService != null) {
            ContentFeedbackFilterCriteria.Builder builder = new ContentFeedbackFilterCriteria.Builder().byUser(uid).forContent(contentIdentifier);
            return contentFeedbackService.getFeedback(builder.build()).getResult();
        }

        return null;
    }

    public static List<ContentAccess> getContentAccess(IUserService userService, String contentIdentifier, String uid) {
        if (userService != null) {
            ContentAccessFilterCriteria.Builder builder = new ContentAccessFilterCriteria.Builder().byUser(uid).forContent(contentIdentifier);
            return userService.getAllContentAccess(builder.build()).getResult();
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

    public static boolean isAvailableLocally(int contentState) {
        return contentState == ContentConstants.State.ARTIFACT_AVAILABLE;
    }

    public static List<ContentModel> getAllLocalContent(IDBSession dbSession, ContentFilterCriteria criteria) {
        String uid = null;
        ContentType[] contentTypes = null;
        if (criteria != null) {
            uid = criteria.getUid();
            contentTypes = criteria.getContentTypes();
        }
        String contentTypesStr = ContentType.getCommaSeparatedContentTypes(contentTypes);

        String contentTypeFilter = String.format(Locale.US, "c.%s in ('%s')", ContentEntry.COLUMN_NAME_CONTENT_TYPE, contentTypesStr);
        String contentVisibilityFilter = String.format(Locale.US, "c.%s = '%s'", ContentEntry.COLUMN_NAME_VISIBILITY, ContentConstants.Visibility.DEFAULT);
        String artifactAvailabilityFilter = String.format(Locale.US, "c.%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);
        String filter = String.format(Locale.US, "WHERE (%s AND %s AND %s)", contentVisibilityFilter, artifactAvailabilityFilter, contentTypeFilter);

        String orderBy = String.format(Locale.US, "ORDER BY ca.%s desc, c.%s desc, c.%s desc", ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP, ContentEntry.COLUMN_NAME_LOCAL_LAST_UPDATED_ON, ContentEntry.COLUMN_NAME_SERVER_LAST_UPDATED_ON);

        String query = null;
        if (uid != null) {
            query = String.format(Locale.US, "SELECT c.* FROM  %s c LEFT JOIN %s ca ON c.%s = ca.%s AND ca.%s = '%s' %s %s;",
                    ContentEntry.TABLE_NAME, ContentAccessEntry.TABLE_NAME, ContentEntry.COLUMN_NAME_IDENTIFIER, ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER, ContentAccessEntry.COLUMN_NAME_UID, uid,
                    filter, orderBy);
        } else {
            query = String.format(Locale.US, "SELECT c.* FROM  %s c %s;",
                    ContentEntry.TABLE_NAME, filter);
        }

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
        String contentTypesStr = ContentType.getCommaSeparatedContentTypes(contentTypes);

        String contentTypeFilter = String.format(Locale.US, "%s in ('%s')", ContentEntry.COLUMN_NAME_CONTENT_TYPE, contentTypesStr);
        String contentVisibilityFilter = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_VISIBILITY, ContentConstants.Visibility.DEFAULT);
        // For hiding the non compatible imported content, which visibility is DEFAULT.
        String artifactAvailabilityFilter = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);

        String filter = String.format(Locale.US, " where (%s AND %s AND %s)", contentVisibilityFilter, artifactAvailabilityFilter, contentTypeFilter);

        List<ContentModel> contentModelListInDB;
        ContentsModel contentsModel = ContentsModel.find(dbSession, filter);
        if (contentsModel != null) {
            contentModelListInDB = contentsModel.getContentModelList();
        } else {
            contentModelListInDB = new ArrayList<>();
        }

        return contentModelListInDB;
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
                if ((pkgVersion(oldContent.getLocalData()) < newPkgVersion) // If old content's pkgVersion is less than the new content then return false.
                        || oldContent.getContentState() != ContentConstants.State.ARTIFACT_AVAILABLE) {  //  If content_state is other than artifact available then also return  false.
                    isExist = false;
                } else {
                    isExist = true;
                }
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
                String count = String.valueOf(viralityMetadataMap.get(KEY_TRANSFER_COUNT));
                return Integer.valueOf(count);
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

    public static void addViralityMetadataIfMissing(Map<String, Object> localDataMap, String origin) {
        if (localDataMap.get(KEY_CONTENT_METADATA) == null) {
            localDataMap.put(KEY_CONTENT_METADATA, new HashMap<String, Object>());
        }

        Map<String, Object> contentMetadata = (Map<String, Object>) localDataMap.get(KEY_CONTENT_METADATA);
        if (contentMetadata.get(KEY_VIRALITY_METADATA) == null) {
            contentMetadata.put(KEY_VIRALITY_METADATA, new HashMap<String, Object>());
        }

        Map<String, Object> viralityMetadata = (Map<String, Object>) contentMetadata.get(KEY_VIRALITY_METADATA);
        if (viralityMetadata.get(KEY_ORIGIN) == null) {
            viralityMetadata.put(KEY_ORIGIN, origin);
        }
        if (viralityMetadata.get(KEY_TRANSFER_COUNT) == null) {
            viralityMetadata.put(KEY_TRANSFER_COUNT, INITIAL_VALUE_FOR_TRANSFER_COUNT);
        }
    }

    private static boolean isContentMetadataAbsent(Map<String, Object> localDataMap) {
        return localDataMap.get(KEY_CONTENT_METADATA) == null;
    }

    private static boolean isContentMetadataPresentWithoutViralityMetadata(Map<String, Object> localDataMap) {
        return ((Map<String, Object>) localDataMap.get(KEY_CONTENT_METADATA)).get(KEY_VIRALITY_METADATA) == null;
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
        List<Map> children = (List<Map>) localDataMap.get("children");
        List<ContentModel> contentModelListInDB;
        if (children != null && children.size() > 0) {
            // Sort by index of child content
            Comparator<Map> comparator = new Comparator<Map>() {
                @Override
                public int compare(Map left, Map right) {
                    double leftIndex = Double.valueOf(left.get("index").toString());
                    double rightIndex = Double.valueOf(right.get("index").toString());
                    return (int) (leftIndex - rightIndex); // use your logic
                }
            };
            Collections.sort(children, comparator);
            List<String> childIdentifiers = new ArrayList<>();
            StringBuilder whenAndThen = new StringBuilder();
            int i = 0;

            for (Map contentChild : children) {
                childIdentifiers.add(contentChild.get("identifier").toString());
                whenAndThen.append(String.format(Locale.US, " WHEN '%s' THEN %s ", contentChild.get("identifier").toString(), i));
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
            ContentsModel contentsModel = ContentsModel.findWithCustomQuery(dbSession, query);
            if (contentsModel != null) {
                contentModelListInDB = contentsModel.getContentModelList();
            } else {
                contentModelListInDB = new ArrayList<>();
            }
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

    private static void applyListingFilter(IConfigService configService, MasterDataType masterDataType, String propertyValue, Map<String, Object> filterMap) {
        if (configService != null && propertyValue != null) {
            String property = masterDataType.getValue();

            if (masterDataType == MasterDataType.AGEGROUP) {
                masterDataType = MasterDataType.AGE;
            }

            GenieResponse<MasterData> masterDataResponse = configService.getMasterData(masterDataType);

            if (masterDataResponse.getStatus()) {
                MasterData masterData = masterDataResponse.getResult();

                for (MasterDataValues masterDataValues : masterData.getValues()) {
                    if (masterDataValues.getTelemetry().equals(propertyValue)) {
                        Map<String, Object> searchMap = masterDataValues.getSearch();
                        Map filtersMap = (Map) searchMap.get("filters");
                        if (filtersMap != null) {
                            Set entrySet = filtersMap.entrySet();
                            for (Object key : entrySet) {
                                Set values = new HashSet();
                                Object filterMapValue = filterMap.get(key);
                                if (filterMapValue != null) {
                                    values.addAll(Arrays.asList(filterMapValue));
                                }
                                values.addAll(Arrays.asList(filtersMap.get(key)));
                                filterMap.put(property, values.toArray(new String[values.size()]));
                            }
                        }
                        break;
                    }
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

    public static void refreshContentListingFromServer(final AppContext appContext, final IConfigService configService, final ContentListingCriteria contentListingCriteria,
                                                       final Profile profile, final String did) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchContentListingFromServer(appContext, configService, contentListingCriteria, profile, did);
            }
        }).start();
    }

    public static String fetchContentListingFromServer(AppContext appContext, IConfigService configService, ContentListingCriteria contentListingCriteria, Profile profile, String did) {
        Map<String, Object> requestMap = ContentHandler.getContentListingRequest(configService, contentListingCriteria, profile, did);
        ContentListingAPI api = new ContentListingAPI(appContext, contentListingCriteria.getContentListingId(), requestMap);
        GenieResponse apiResponse = api.post();
        String jsonStr = null;
        if (apiResponse.getStatus()) {
            jsonStr = apiResponse.getResult().toString();
            ContentHandler.saveContentListingDataInDB(appContext, contentListingCriteria, jsonStr);
        }
        return jsonStr;
    }


    public static Map<String, Object> getContentListingRequest(IConfigService configService, ContentListingCriteria contentListingCriteria, Profile profile, String did) {
        HashMap<String, Object> contextMap = new HashMap<>();

        if (profile != null) {
            contextMap.put("uid", profile.getUid());
            contextMap.put("dlang", profile.getLanguage());
        }
        contextMap.put("did", did);
        contextMap.put("contentid", "");

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("compatibilityLevel", getCompatibilityLevelFilter());

        // Add subject filter
        if (!StringUtil.isNullOrEmpty(contentListingCriteria.getSubject()))
            applyListingFilter(configService, MasterDataType.SUBJECT, contentListingCriteria.getSubject(), filterMap);

        if (contentListingCriteria.getAge() > 0)
            applyListingFilter(configService, MasterDataType.AGEGROUP, String.valueOf(contentListingCriteria.getAge()), filterMap);

        // Add board filter
        if (!StringUtil.isNullOrEmpty(contentListingCriteria.getBoard()))
            applyListingFilter(configService, MasterDataType.BOARD, contentListingCriteria.getBoard(), filterMap);

        // Add medium filter
        if (!StringUtil.isNullOrEmpty(contentListingCriteria.getMedium()))
            applyListingFilter(configService, MasterDataType.MEDIUM, contentListingCriteria.getMedium(), filterMap);

        // Add standard filter
        if (contentListingCriteria.getGrade() > 0)
            applyListingFilter(configService, MasterDataType.GRADELEVEL, String.valueOf(contentListingCriteria.getGrade()), filterMap);

        String[] audienceArr = contentListingCriteria.getAudience();
        if (audienceArr != null && audienceArr.length > 0) {
            for (String audience : audienceArr) {
                applyListingFilter(configService, MasterDataType.AUDIENCE, audience, filterMap);
            }
        }

        String[] channelArr = contentListingCriteria.getChannel();
        if (channelArr != null && channelArr.length > 0) {
            for (String channel : channelArr) {
                applyListingFilter(configService, MasterDataType.CHANNEL, channel, filterMap);
            }
        }

        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("context", contextMap);
        requestMap.put("filters", filterMap);

        return requestMap;
    }

    public static void saveContentListingDataInDB(AppContext appContext, ContentListingCriteria contentListingCriteria, String jsonStr) {
        if (jsonStr == null) {
            return;
        }

        long expiryTime = DateUtil.getEpochTime() + ContentConstants.CACHE_TIMEOUT_HOME_CONTENT;

        ContentListingModel contentListingModelInDB = ContentListingModel.find(appContext.getDBSession(), contentListingCriteria);
        if (contentListingModelInDB != null) {
            contentListingModelInDB.delete();
        }
        ContentListingModel contentListingModel = ContentListingModel.build(appContext.getDBSession(), contentListingCriteria, jsonStr, expiryTime);
        contentListingModel.save();
    }

    public static ContentListing getContentListingResult(IDBSession dbSession, ContentListingCriteria contentListingCriteria, String jsonStr) {
        ContentListing contentListing = null;

        LinkedTreeMap map = GsonUtil.fromJson(jsonStr, LinkedTreeMap.class);
        LinkedTreeMap responseParams = (LinkedTreeMap) map.get("params");
        LinkedTreeMap result = (LinkedTreeMap) map.get("result");

        String responseMessageId = null;
        if (responseParams.containsKey("resmsgid")) {
            responseMessageId = (String) responseParams.get("resmsgid");
        }

        if (result != null) {
            contentListing = new ContentListing();
            contentListing.setContentListingId(contentListingCriteria.getContentListingId());
            contentListing.setResponseMessageId(responseMessageId);
            if (result.containsKey("page")) {
                contentListing.setContentListingSections(getSectionsFromPageMap(dbSession, (Map<String, Object>) result.get("page"), contentListingCriteria));
            }
        }

        return contentListing;
    }

    private static List<ContentListingSection> getSectionsFromPageMap(IDBSession dbSession, Map<String, Object> pageMap, ContentListingCriteria contentListingCriteria) {
        List<ContentListingSection> contentListingSectionList = new ArrayList<>();

        List<Map<String, Object>> sections = (List<Map<String, Object>>) pageMap.get("sections");
        for (Map<String, Object> sectionMap : sections) {
            String contentDataList = null;
            if (sectionMap.containsKey("contents")) {
                contentDataList = GsonUtil.toJson(sectionMap.get("contents"));
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
                    }
                }

                contentSearchCriteria = builder.build();
            }

            ContentListingSection contentListingSection = new ContentListingSection();
            contentListingSection.setResponseMessageId((String) sectionMap.get("resmsgid"));
            contentListingSection.setApiId((String) sectionMap.get("apiid"));
            contentListingSection.setSectionName(((Map) ((Map) sectionMap.get("display")).get("name")).get("en").toString());

            if (!StringUtil.isNullOrEmpty(contentDataList)) {
                Type type = new TypeToken<List<ContentData>>() {
                }.getType();
                List<ContentData> contentData = GsonUtil.getGson().fromJson(contentDataList, type);
                contentListingSection.setContentDataList(contentData);
            }
            contentListingSection.setContentSearchCriteria(contentSearchCriteria);

            contentListingSectionList.add(contentListingSection);
        }

        return contentListingSectionList;
    }

    public static String getDownloadUrl(Map<String, Object> dataMap) {
        String downloadUrl = null;

        String contentType = (String) dataMap.get(KEY_CONTENT_TYPE);
        if ((ContentType.TEXTBOOK.getValue().equalsIgnoreCase(contentType)
                || ContentType.COLLECTION.getValue().equalsIgnoreCase(contentType))) {
            ContentVariant spineContentVariant = getVariant(dataMap, "spine");
            if (spineContentVariant != null) {
                downloadUrl = spineContentVariant.getEcarUrl();
            }
        }

        if (StringUtil.isNullOrEmpty(downloadUrl)) {
            downloadUrl = (String) dataMap.get(KEY_DOWNLOAD_URL);
        }
        return downloadUrl;
    }

    public static List<ContentModel> getContentModelToExport(IDBSession dbSession, List<String> contentIds) {
        List<ContentModel> contentModelToExport = new ArrayList<>();
        Queue<ContentModel> queue = new LinkedList<>();

        List<ContentModel> contentModelList = findAllContentsWithIdentifiers(dbSession, contentIds);
        if (contentModelList != null) {
            queue.addAll(contentModelList);
        }

        ContentModel node;
        while (!queue.isEmpty()) {
            node = queue.remove();

            if (hasChildren(node.getLocalData())) {
                List<String> childContentsIdentifiers = getChildContentsIdentifiers(node.getLocalData());
                List<ContentModel> contentModelListInDB = findAllContentsWithIdentifiers(dbSession, childContentsIdentifiers);
                if (contentModelListInDB != null) {
                    queue.addAll(contentModelListInDB);
                }
            } else if (hasPreRequisites(node.getLocalData())) {
                List<String> preRequisitesIdentifiers = getPreRequisitesIdentifiers(node.getLocalData());
                List<ContentModel> preRequisitiesListInDB = findAllContentsWithIdentifiers(dbSession, preRequisitesIdentifiers);
                if (preRequisitiesListInDB != null) {
                    queue.addAll(preRequisitiesListInDB);
                }
            }

            contentModelToExport.add(node);
        }

        return contentModelToExport;
    }

    public static List<ContentModel> deDupe(List<ContentModel> contentModelList) {
        Set<ContentModel> uniqueContentModels = new LinkedHashSet<>(contentModelList);

        return new ArrayList<>(uniqueContentModels);
    }

    public static String getExportedFileName(List<ContentModel> contentModelList) {
        String fileName = "blank.ecar";
        ContentModel firstContent = null;
        int rootContents = 0;

        if (contentModelList.size() > 0) {
            firstContent = contentModelList.get(0);
        }

        String appendName = "";

        for (ContentModel contentModel : contentModelList) {
            if (ContentConstants.Visibility.DEFAULT.equalsIgnoreCase(contentModel.getVisibility())) {
                rootContents++;
            }
        }

        if (rootContents > 1) {
            appendName = String.format(Locale.US, "+%s", rootContents - 1);
        }

        if (firstContent != null) {
            String name = readName(GsonUtil.fromJson(firstContent.getLocalData(), Map.class));
            if (!StringUtil.isNullOrEmpty(name) && name.length() > MAX_CONTENT_NAME) {
                name = name.substring(0, MAX_CONTENT_NAME - 3) + "...";
            }

            double pkgVersion;

            try {
                pkgVersion = pkgVersion(firstContent.getLocalData());
            } catch (Exception e) {
                pkgVersion = 0;
            }

            fileName = String.format(Locale.US, "%s-v%s%s.ecar", name, "" + pkgVersion, appendName);
        }

        return fileName;
    }

}
