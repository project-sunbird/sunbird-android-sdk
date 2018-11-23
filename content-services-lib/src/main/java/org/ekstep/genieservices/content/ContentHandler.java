package org.ekstep.genieservices.content;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.IConfigService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
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
import org.ekstep.genieservices.commons.bean.ContentSortCriteria;
import org.ekstep.genieservices.commons.bean.ContentUpdateAvailable;
import org.ekstep.genieservices.commons.bean.FilterValue;
import org.ekstep.genieservices.commons.bean.FlagContentRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.MasterDataValues;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.SummarizerContentFilterCriteria;
import org.ekstep.genieservices.commons.bean.SunbirdContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.commons.bean.enums.SearchType;
import org.ekstep.genieservices.commons.bean.enums.SortOrder;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
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
import org.ekstep.genieservices.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
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
    public static final String KEY_DB_DIAL_CODES = "DIAL_CODES-";

    private static final String TAG = ContentHandler.class.getSimpleName();

    private static final String KEY_IDENTIFIER = "identifier";
    private static final String KEY_PKG_VERSION = "pkgVersion";
    private static final String KEY_ARTIFACT_URL = "artifactUrl";
    private static final String KEY_APP_ICON = "appIcon";
    private static final String KEY_POSTER_IMAGE = "posterImage";
    private static final String KEY_GRAY_SCALE_APP_ICON = "grayScaleAppIcon";
    private static final String KEY_STATUS = "status";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_OBJECT_TYPE = "objectType";
    private static final String KEY_VARIANTS = "variants";
    private static final String KEY_DOWNLOAD_URL = "downloadUrl";
    private static final String KEY_COMPATIBILITY_LEVEL = "compatibilityLevel";
    private static final String KEY_MIME_TYPE = "mimeType";
    private static final String AUDIENCE_KEY = "audience";
    private static final String PRAGMA_KEY = "pragma";
    private static final String KEY_LAST_UPDATED_ON = "lastUpdatedOn";
    private static final String KEY_PRE_REQUISITES = "pre_requisites";
    private static final String KEY_CHILDREN = "children";
    private static final String KEY_CONTENT_TYPE = "contentType";
    private static final String KEY_NAME = "name";
    private static final String KEY_CONTENT_ENCODING = "contentEncoding";
    private static final String KEY_CONTENT_DISPOSITION = "contentDisposition";
    private static final String KEY_DIAL_CODES = "dialcodes";

    private static final String KEY_CONTENT_METADATA = "contentMetadata";
    private static final String KEY_VIRALITY_METADATA = "virality";
    private static final String KEY_TRANSFER_COUNT = "transferCount";
    private static final String KEY_ORIGIN = "origin";
    private static final String KEY_SIZE = "size";

    private static final int DEFAULT_PACKAGE_VERSION = -1;
    private static final int INITIAL_VALUE_FOR_TRANSFER_COUNT = 0;
    private static final int MAX_CONTENT_NAME = 30;

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
        String pragma = readPragma(contentData);

        ContentModel contentModel = ContentModel.build(dbSession, identifier, serverData, serverLastUpdatedOn,
                manifestVersion, localData, mimeType, contentType, visibility, audience, pragma);

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

    public static String readContentEncoding(Map contentData) {
        if (contentData.containsKey(KEY_CONTENT_ENCODING)) {
            return (String) contentData.get(KEY_CONTENT_ENCODING);
        }
        return null;
    }

    public static String readContentDisposition(Map contentData) {
        if (contentData.containsKey(KEY_CONTENT_DISPOSITION)) {
            return (String) contentData.get(KEY_CONTENT_DISPOSITION);
        }
        return null;
    }

    public static List<String> readDialcodes(Map contentData) {
        if (contentData.containsKey(KEY_DIAL_CODES)) {
            return (ArrayList<String>) contentData.get(KEY_DIAL_CODES);
        }
        return null;
    }

    public static boolean isOnlineContent(Map contentData) {
        String contentDisposition = readContentDisposition(contentData);

        return !StringUtil.isNullOrEmpty(contentDisposition) && ContentConstants.ContentDisposition.ONLINE.equals(contentDisposition);
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

    private static Map readVariant(Map contentDataMap) {
        Map variantData = null;
        Object variants = contentDataMap.get(KEY_VARIANTS);
        if (variants != null) {
            if (variants instanceof Map) {
                variantData = (Map) variants;
            } else {
                variantData = GsonUtil.fromJson(((String) variants).replace("\\", ""), Map.class);
            }
        }
        return variantData;
    }

    private static String readName(Map contentData) {
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
        List<String> audienceList = null;
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
            audienceList.add("Learner");
        }
        Collections.sort(audienceList);
        return StringUtil.join(",", audienceList);
    }

    public static String readPragma(Map contentData) {
        List<String> pragmaList = null;
        if (contentData.containsKey(PRAGMA_KEY)) {
            pragmaList = (ArrayList<String>) contentData.get(PRAGMA_KEY);
        }

        if (!CollectionUtil.isNullOrEmpty(pragmaList)) {
            Collections.sort(pragmaList);
            return StringUtil.join(",", pragmaList);
        }

        return null;
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

    public static boolean isDraftContent(String status) {
        return !StringUtil.isNullOrEmpty(status) && status.equalsIgnoreCase(ContentConstants.ContentStatus.DRAFT);
    }

    public static String readExpiryDate(Map contentData) {
        if (contentData.containsKey(KEY_EXPIRES)) {
            return (String) contentData.get(KEY_EXPIRES);
        }
        return null;
    }

    public static boolean isExpired(String expiryDate) {
        if (!StringUtil.isNullOrEmpty(expiryDate)) {
            long millis = DateUtil.getTime(expiryDate);
            if (System.currentTimeMillis() > millis) {
                return true;
            }
        }
        return false;
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

    public static Double readPkgVersion(String localData) {
        return readPkgVersion(GsonUtil.fromJson(localData, Map.class));
    }

    public static Double readPkgVersion(Map contentData) {
        return (Double) contentData.get(KEY_PKG_VERSION);
    }

    public static boolean hasPreRequisites(String localData) {
        return GsonUtil.fromJson(localData, Map.class).get(KEY_PRE_REQUISITES) != null;
    }

    public static boolean hasPreRequisites(Map contentData) {
        return contentData.get(KEY_PRE_REQUISITES) != null;
    }

    private static List<Map<String, Object>> readPreRequisites(Map contentData) {
        if (contentData.containsKey(KEY_PRE_REQUISITES)) {
            return (List<Map<String, Object>>) contentData.get(KEY_PRE_REQUISITES);
        }
        return null;
    }

    private static List<String> getPreRequisitesIdentifiers(String localData) {
        return getPreRequisitesIdentifiers(GsonUtil.fromJson(localData, Map.class));
    }

    public static List<String> getPreRequisitesIdentifiers(Map contentData) {
        List<String> childIdentifiers = new ArrayList<>();

        List<Map<String, Object>> preRequisites = readPreRequisites(contentData);
        if (preRequisites != null) {
            for (Map child : preRequisites) {
                String childIdentifier = readIdentifier(child);
                childIdentifiers.add(childIdentifier);
            }
        }

        // Return the pre_requisites in DB
        return childIdentifiers;
    }

    public static boolean hasChildren(String localData) {
        return GsonUtil.fromJson(localData, Map.class).get(KEY_CHILDREN) != null;
    }

    public static boolean hasChildren(Map contentData) {
        return contentData.get(KEY_CHILDREN) != null;
    }

    private static List<Map<String, Object>> readChildren(Map contentData) {
        if (contentData.containsKey(KEY_CHILDREN)) {
            return (List<Map<String, Object>>) contentData.get(KEY_CHILDREN);
        }
        return null;
    }

    private static List<String> getChildContentsIdentifiers(String localData) {
        return getChildContentsIdentifiers(GsonUtil.fromJson(localData, Map.class));
    }

    public static List<String> getChildContentsIdentifiers(Map contentData) {
        List<String> childIdentifiers = new ArrayList<>();

        List<Map<String, Object>> children = readChildren(contentData);
        if (children != null) {
            for (Map child : children) {
                String childIdentifier = readIdentifier(child);
                childIdentifiers.add(childIdentifier);
            }
        }

        // Return the childrenInDB
        return childIdentifiers;
    }

    public static GenieResponse fetchContentDetailsFromServer(AppContext appContext, String contentIdentifier) {
        ContentDetailsAPI api = new ContentDetailsAPI(appContext, contentIdentifier);
        return api.get();
    }

    public static Map getContentDetailsMap(String body) {
        Map map = GsonUtil.fromJson(body, Map.class);
        Map result = (Map) map.get("result");

        return (Map) result.get("content");
    }

    public static void refreshContentDetailsFromServer(final AppContext appContext, final String contentIdentifier, final ContentModel existingContentModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                GenieResponse contentDetailsAPIResponse = fetchContentDetailsFromServer(appContext, contentIdentifier);
                if (contentDetailsAPIResponse.getStatus()) {
                    Map contentData = getContentDetailsMap(contentDetailsAPIResponse.getResult().toString());

                    if (contentData != null) {
                        existingContentModel.setServerData(GsonUtil.toJson(contentData));
                        existingContentModel.setServerLastUpdatedOn(serverLastUpdatedOn(contentData));
                        existingContentModel.setAudience(readAudience(contentData));
                        existingContentModel.update();

                        if (!StringUtil.isNullOrEmpty(contentDetailsAPIResponse.getResult().toString()) &&
                                !StringUtil.isNullOrEmpty(existingContentModel.getLocalData())) {
                            ContentData serverContentData = GsonUtil.fromJson(GsonUtil.toJson(contentData), ContentData.class);
                            ContentData localContentData = GsonUtil.fromJson(existingContentModel.getLocalData(), ContentData.class);

                            if (isUpdateAvailable(serverContentData, localContentData)) {
                                //Fire the event saying an update is available for the identifier
                                EventBus.postEvent(new ContentUpdateAvailable(contentIdentifier));
                            }
                        }
                    }
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
            content.setContentData(localData);
        }
        if (contentModel.getServerData() != null) {
            serverData = GsonUtil.fromJson(contentModel.getServerData(), ContentData.class);
            if (localData == null) {
                content.setContentData(serverData);
            }
        }

        content.setUpdateAvailable(isUpdateAvailable(serverData, localData));
        content.setMimeType(contentModel.getMimeType());
        content.setBasePath(contentModel.getPath());
        content.setContentType(contentModel.getContentType());
        content.setAvailableLocally(isAvailableLocally(contentModel.getContentState()));
        content.setReferenceCount(contentModel.getRefCount());
        content.setSizeOnDevice(contentModel.getSizeOnDevice());
        content.setLastUsedTime(contentModel.getLastUsedTime());

        long contentCreationTime = 0;
        String localLastUpdatedTime = contentModel.getLocalLastUpdatedTime();
        if (!StringUtil.isNullOrEmpty(localLastUpdatedTime)) {
            contentCreationTime = DateUtil.getTime(localLastUpdatedTime);
        }
        content.setLastUpdatedTime(contentCreationTime);

        return content;
    }

    /**
     * Content with artifact without zip i.e. pfd, mp4
     *
     * @param contentDisposition
     * @param contentEncoding
     * @return
     */
    public static boolean isInlineIdentity(String contentDisposition, String contentEncoding) {
        return !StringUtil.isNullOrEmpty(contentDisposition) && !StringUtil.isNullOrEmpty(contentEncoding)
                && ContentConstants.ContentDisposition.INLINE.equals(contentDisposition) && ContentConstants.ContentEncoding.IDENTITY.equals(contentEncoding);
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
        if (criteria == null) {
            criteria = new ContentFilterCriteria.Builder().build();
        }

        String uid = criteria.getUid();
        String[] contentTypes = criteria.getContentTypes();
        String contentTypesStr = StringUtil.join("','", contentTypes);

        StringBuilder audienceFilterBuilder = new StringBuilder();
        if (!CollectionUtil.isEmpty(criteria.getAudience())) {
            for (String audience : criteria.getAudience()) {
                audienceFilterBuilder.append(audienceFilterBuilder.length() > 0 ? " or " : "");     // "instructor", "learner", "instructor,learner"
                audienceFilterBuilder.append(String.format(Locale.US, "c.%s like '%%%s%%'", ContentEntry.COLUMN_NAME_AUDIENCE, audience));
            }
        }

        StringBuilder pragmaFilterBuilder = new StringBuilder();
        if (!CollectionUtil.isEmpty(criteria.getExclPragma())) {
            for (String pragma : criteria.getExclPragma()) {
                pragmaFilterBuilder.append(pragmaFilterBuilder.length() > 0 ? " OR " : "");
                pragmaFilterBuilder.append(String.format(Locale.US, "c.%s not like '%%%s%%'", ContentEntry.COLUMN_NAME_PRAGMA, pragma));
            }
        } else if (!CollectionUtil.isEmpty(criteria.getPragma())) {
            for (String pragma : criteria.getPragma()) {
                pragmaFilterBuilder.append(pragmaFilterBuilder.length() > 0 ? " OR " : "");
                pragmaFilterBuilder.append(String.format(Locale.US, "c.%s like '%%%s%%'", ContentEntry.COLUMN_NAME_PRAGMA, pragma));
            }
        }

        String contentTypeFilter = String.format(Locale.US, "c.%s in ('%s')", ContentEntry.COLUMN_NAME_CONTENT_TYPE, contentTypesStr.toLowerCase());
        String contentVisibilityFilter = String.format(Locale.US, "c.%s = '%s'", ContentEntry.COLUMN_NAME_VISIBILITY, ContentConstants.Visibility.DEFAULT);
        String artifactAvailabilityFilter = String.format(Locale.US, "c.%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);

        String filter = String.format(Locale.US, "%s AND %s AND %s", contentVisibilityFilter, artifactAvailabilityFilter, contentTypeFilter);
        if (audienceFilterBuilder.length() > 0) {
            filter = String.format(Locale.US, " %s AND (%s)", filter, audienceFilterBuilder.toString());
        }
        if (pragmaFilterBuilder.length() > 0) {
            filter = String.format(Locale.US, " %s AND (%s)", filter, pragmaFilterBuilder.toString());
        }

        String whereClause = String.format(Locale.US, "WHERE (%s)", filter);

        StringBuilder orderBy = new StringBuilder();
        int i = 0;
        for (ContentSortCriteria sortCriteria : criteria.getSortCriteria()) {
            if (sortCriteria != null) {
                if ("lastUsedOn".equals(sortCriteria.getSortAttribute()) && uid != null) {
                    if (i > 0) {
                        orderBy.append(",");
                    } else {
                        orderBy.append("ORDER BY");
                    }
                    orderBy.append(String.format(Locale.US, " ca.%s %s", ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP, sortCriteria.getSortOrder().getValue()));
                    i++;
                } else if ("localLastUpdatedOn".equals(sortCriteria.getSortAttribute())) {
                    if (i > 0) {
                        orderBy.append(",");
                    } else {
                        orderBy.append("ORDER BY");
                    }
                    orderBy.append(String.format(Locale.US, " c.%s %s", ContentEntry.COLUMN_NAME_LOCAL_LAST_UPDATED_ON, sortCriteria.getSortOrder().getValue()));
                    i++;
                } else if ("sizeOnDevice".equals(sortCriteria.getSortAttribute())) {
                    if (i > 0) {
                        orderBy.append(",");
                    } else {
                        orderBy.append("ORDER BY");
                    }
                    orderBy.append(String.format(Locale.US, " c.%s %s", ContentEntry.COLUMN_NAME_SIZE_ON_DEVICE, sortCriteria.getSortOrder().getValue()));
                    i++;
                }
            }
        }

        String query;
        if (uid != null) {
            query = String.format(Locale.US, "SELECT c.*, ca.%s FROM  %s c LEFT JOIN %s ca ON c.%s = ca.%s AND ca.%s = '%s' %s %s;",
                    ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP,
                    ContentEntry.TABLE_NAME, ContentAccessEntry.TABLE_NAME,
                    ContentEntry.COLUMN_NAME_IDENTIFIER, ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER,
                    ContentAccessEntry.COLUMN_NAME_UID, uid,
                    whereClause, orderBy.toString());
        } else {
            query = String.format(Locale.US, "SELECT c.* FROM  %s c %s %s;",
                    ContentEntry.TABLE_NAME,
                    whereClause, orderBy.toString());
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

    public static List<ContentModel> getAllLocalContentModel(IDBSession dbSession) {
        String contentVisibilityFilter = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_VISIBILITY, ContentConstants.Visibility.DEFAULT);
        // For hiding the non compatible imported content, which visibility is DEFAULT.
        String artifactAvailabilityFilter = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);

        String filter = String.format(Locale.US, " where (%s AND %s)", contentVisibilityFilter, artifactAvailabilityFilter);

        List<ContentModel> contentModelListInDB;
        ContentsModel contentsModel = ContentsModel.find(dbSession, filter);
        if (contentsModel != null) {
            contentModelListInDB = contentsModel.getContentModelList();
        } else {
            contentModelListInDB = new ArrayList<>();
        }

        return contentModelListInDB;
    }

    public static List<ContentModel> getLocalContents(IDBSession dbSession, SummarizerContentFilterCriteria criteria) {
        if (criteria == null) {
            criteria = new SummarizerContentFilterCriteria.Builder().build();
        }

        List<String> uids = criteria.getUid();
        String[] contentTypes = criteria.getContentTypes();
        String contentTypesStr = StringUtil.join("','", contentTypes);


        String contentTypeFilter = String.format(Locale.US, "c.%s in ('%s')", ContentEntry.COLUMN_NAME_CONTENT_TYPE, contentTypesStr.toLowerCase());
        String artifactAvailabilityFilter = String.format(Locale.US, "c.%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);

        String filter = String.format(Locale.US, " %s AND %s", artifactAvailabilityFilter, contentTypeFilter);


        String whereClause = String.format(Locale.US, "WHERE (%s)", filter);

        StringBuilder orderBy = new StringBuilder();
        int i = 0;
        for (ContentSortCriteria sortCriteria : criteria.getSortCriteria()) {
            if (sortCriteria != null) {
                if ("lastUsedOn".equals(sortCriteria.getSortAttribute()) && uids != null) {
                    if (i > 0) {
                        orderBy.append(",");
                    } else {
                        orderBy.append("ORDER BY");
                    }
                    orderBy.append(String.format(Locale.US, " ca.%s %s", ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP, sortCriteria.getSortOrder().getValue()));
                    i++;
                }
            }
        }

        String query;
        if (uids != null) {
            query = String.format(Locale.US, "SELECT c.*, ca.%s FROM  %s c LEFT JOIN %s ca ON c.%s = ca.%s AND ca.%s IN (%s) %s %s;",
                    ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP,
                    ContentEntry.TABLE_NAME, ContentAccessEntry.TABLE_NAME,
                    ContentEntry.COLUMN_NAME_IDENTIFIER, ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER,
                    ContentAccessEntry.COLUMN_NAME_UID, StringUtil.join(",", StringUtil.getStringWithQuoteList(uids)),
                    whereClause, orderBy.toString());
        } else {
            query = String.format(Locale.US, "SELECT c.* FROM  %s c %s %s;",
                    ContentEntry.TABLE_NAME,
                    whereClause, orderBy.toString());
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

    public static void deleteOrUpdateContent(AppContext appContext, ContentModel contentModel, boolean isChildItems, boolean isChildContent) {
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
            // Do not update the content state if mimeType is "application/vnd.ekstep.content-collection"
            if (ContentConstants.MimeType.COLLECTION.equals(contentModel.getMimeType())) {
                contentState = ContentConstants.State.ARTIFACT_AVAILABLE;
            } else {
                contentState = ContentConstants.State.ONLY_SPINE;
            }

        } else {
            // TODO: This check should be before updating the existing refCount.
            // Do not update the content state if mimeType is "application/vnd.ekstep.content-collection" and refCount is more than 1.
            if (ContentConstants.MimeType.COLLECTION.equals(contentModel.getMimeType()) && refCount > 1) {
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

                String contentRootPath = StringUtil.getFirstPartOfThePathNameOnLastDelimiter(contentModel.getPath());

                //store the folder's last modified time
                if (!StringUtil.isNullOrEmpty(contentRootPath)) {
                    File contentRootFolder = new File(contentRootPath);

                    if (FileUtil.doesFileExists(contentRootFolder.getPath())) {
                        appContext.getKeyValueStore().putLong(ServiceConstants.PreferenceKey.KEY_LAST_MODIFIED, contentRootFolder.lastModified());
                    }
                }
            }

            contentModel.setVisibility(visibility);
            // Update the refCount
            contentModel.addOrUpdateRefCount(refCount);
            contentModel.addOrUpdateContentState(contentState);
            contentModel.setSizeOnDevice(FileUtil.getFileSize(new File(contentModel.getPath())));
            contentModel.doNotUpdateLocalLastUpdatedTime();

            contentModel.update();
        }
    }

    public static void deleteAllPreRequisites(AppContext appContext, ContentModel contentModel, boolean isChildContent) {
        List<String> preRequisitesIdentifier = getPreRequisitesIdentifiers(contentModel.getLocalData());
        List<ContentModel> contentModelListInDB = findAllContentsWithIdentifiers(appContext.getDBSession(), preRequisitesIdentifier);

        if (contentModelListInDB != null) {
            for (ContentModel c : contentModelListInDB) {
                deleteOrUpdateContent(appContext, c, true, isChildContent);
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
                deleteOrUpdateContent(appContext, node, true, isChildContent);
            }
        }
    }

    public static List<ContentModel> findAllContentsWithIdentifiers(IDBSession dbSession, List<String> identifiers) {
        String filter = String.format(Locale.US, " where %s in ('%s') AND %s > 0", ContentEntry.COLUMN_NAME_IDENTIFIER, StringUtil.join("','", identifiers), ContentEntry.COLUMN_NAME_REF_COUNT);

        List<ContentModel> contentModelListInDB = null;
        ContentsModel contentsModel = ContentsModel.find(dbSession, filter);
        if (contentsModel != null) {
            contentModelListInDB = contentsModel.getContentModelList();
        }

        return contentModelListInDB;
    }

    public static List<ContentModel> findAllContent(IDBSession dbSession) {
        String contentStateFilter = String.format(Locale.US, "%s > '0'", ContentEntry.COLUMN_NAME_REF_COUNT);

        String filter = String.format(Locale.US, " where %s", contentStateFilter);

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
    public static boolean isCompatible(AppContext appContext, Double compatibilityLevel) {
        return (compatibilityLevel >= appContext.getParams().getInt(IParams.Key.MIN_COMPATIBILITY_LEVEL))
                && (compatibilityLevel <= appContext.getParams().getInt(IParams.Key.MAX_COMPATIBILITY_LEVEL));
    }

    public static boolean isImportFileExist(ContentModel oldContentModel, Map newContentData) {
        if (oldContentModel == null || newContentData == null || newContentData.isEmpty()) {
            return false;
        }

        boolean isExist = false;
        try {
            String oldIdentifier = oldContentModel.getIdentifier();
            String newIdentifier = readIdentifier(newContentData);
            String oldVisibility = oldContentModel.getVisibility();
            String newVisibility = readVisibility(newContentData);

            if (oldIdentifier.equals(newIdentifier) && oldVisibility.equals(newVisibility)) {
                isExist = readPkgVersion(oldContentModel.getLocalData()) >= readPkgVersion(newContentData);
            }
        } catch (Exception e) {
            Logger.e(TAG, "isImportFileExist", e);
        }

        return isExist;
    }

    /**
     * If status is DRAFT and pkgVersion == 0 then don't do the duplicate check..
     */
    public static boolean isDuplicateCheckRequired(boolean isDraftContent, Double pkgVersion) {
        return isDraftContent && pkgVersion == 0;
    }

    /**
     * To Check whether the content is exist or not.
     *
     * @param oldContent    Old ContentModel
     * @param newIdentifier New content identifier
     * @return True - if file exists, False- does not exists
     */
    public static boolean isContentExist(ContentModel oldContent, String newIdentifier, Double newPkgVersion, boolean keepLowerVersion) {
        if (oldContent == null) {
            return false;
        }

        boolean isExist = false;
        try {
            String oldIdentifier = oldContent.getIdentifier();
            if (oldIdentifier.equalsIgnoreCase(newIdentifier)) {
                boolean overrideDB = false;
                if (keepLowerVersion) {
                    if ((readPkgVersion(oldContent.getLocalData()) < newPkgVersion)) {
                        overrideDB = false;
                    } else {
                        overrideDB = true;
                    }
                } else if ((readPkgVersion(oldContent.getLocalData()) < newPkgVersion)) {
                    overrideDB = true;
                }

                if (overrideDB
                        // If old content's pkgVersion is less than the new content then return false.
//                        && ((readPkgVersion(oldContent.getLocalData()) < newPkgVersion)
                        //  If content_state is other than artifact available then also return  false.
                        || (!keepLowerVersion && oldContent.getContentState() != ContentConstants.State.ARTIFACT_AVAILABLE)) {
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
                return Double.valueOf(count).intValue();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String readSizeFromContentMap(Map contentData) {
        return contentData.get(KEY_SIZE) != null ? String.valueOf(contentData.get(KEY_SIZE)) : "";
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
            viralityMetadata.put(KEY_TRANSFER_COUNT, transferCount(viralityMetadata) + 1);
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
            Double transferCount = (Double) viralityMetadata.get(KEY_TRANSFER_COUNT);
            return transferCount.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static List<ContentModel> getSortedChildrenList(IDBSession dbSession, String localData, int childContents) {
        Map<String, Object> localDataMap = GsonUtil.fromJson(localData, Map.class);
        List<Map> children = (List<Map>) localDataMap.get("children");
        List<ContentModel> contentModelListInDB = null;
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
                case ContentConstants.ChildContents.DOWNLOADED:
                    filter = String.format(Locale.US, " AND %s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);
                    break;

                case ContentConstants.ChildContents.SPINE:
                    filter = String.format(Locale.US, " AND %s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ONLY_SPINE);
                    break;

                case ContentConstants.ChildContents.ALL:
                default:
                    filter = "";
                    break;
            }

            String query = String.format(Locale.US, "Select * from %s where %s in ('%s') %s %s",
                    ContentEntry.TABLE_NAME, ContentEntry.COLUMN_NAME_IDENTIFIER, StringUtil.join("','", childIdentifiers), filter, orderBy);
            ContentsModel contentsModel = ContentsModel.findWithCustomQuery(dbSession, query);
            if (contentsModel != null) {
                contentModelListInDB = contentsModel.getContentModelList();
            }
        }

        if (contentModelListInDB == null) {
            contentModelListInDB = new ArrayList<>();
        }

        return contentModelListInDB;
    }

    public static Map<String, Object> getSearchRequest(AppContext appContext, Set<String> contentIds, String[] status) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("compatibilityLevel", getCompatibilityLevelFilter(appContext));
        filterMap.put("identifier", contentIds);
        filterMap.put("status", Arrays.asList(status));
        filterMap.put("objectType", Collections.singletonList("Content"));

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("filters", filterMap);
        requestMap.put("fields", Arrays.asList(KEY_DOWNLOAD_URL, KEY_VARIANTS, KEY_MIME_TYPE));

        return requestMap;
    }

    public static Map<String, Object> getSearchContentRequest(AppContext appContext, IConfigService configService, ContentSearchCriteria criteria) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("query", criteria.getQuery());
        requestMap.put("limit", criteria.getLimit());
        requestMap.put("mode", criteria.getMode());

        String[] facets = criteria.getFacets();
        if (!CollectionUtil.isEmpty(facets)) {
            requestMap.put("facets", Arrays.asList(facets));
        }

        addSortCriteria(requestMap, criteria.getSortCriteria());
        if (SearchType.SEARCH.equals(criteria.getSearchType())) {
            requestMap.put("filters", getSearchRequest(appContext, configService, criteria));
        } else {
            requestMap.put("filters", getFilterRequest(appContext, criteria));
        }
        return requestMap;
    }

    public static Map<String, Object> getSearchContentRequest(AppContext appContext, IConfigService configService, SunbirdContentSearchCriteria criteria) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("query", criteria.getQuery());
        requestMap.put("offset", criteria.getOffset());
        requestMap.put("limit", criteria.getLimit());
        requestMap.put("mode", criteria.getMode());
        requestMap.put("framework", criteria.getFramework());
        requestMap.put("languageCode", criteria.getLanguageCode());

        if (!CollectionUtil.isEmpty(criteria.getExists())) {
            requestMap.put("exists", Arrays.asList(criteria.getExists()));
        }

        String[] facets = criteria.getFacets();
        if (!CollectionUtil.isEmpty(facets)) {
            requestMap.put("facets", Arrays.asList(facets));
        }

        addSortCriteria(requestMap, criteria.getSortCriteria());
        if (SearchType.SEARCH.equals(criteria.getSearchType())) {
            requestMap.put("filters", getSearchRequest(appContext, configService, criteria));
        } else {
            requestMap.put("filters", getFilterRequest(appContext, criteria));
        }
        return requestMap;
    }

    private static void addSortCriteria(Map<String, Object> requestMap, List<ContentSortCriteria> sortCriteria) {
        Map<String, String> sortMap = new HashMap<>();
        if (sortCriteria != null && sortCriteria.size() > 0) {
            //TODO for now only handling the first sort criteria. As of now only one criteria is used in the system and the list has been kept for future compatibility.
            sortMap.put(sortCriteria.get(0).getSortAttribute(), sortCriteria.get(0).getSortOrder().getValue());
            requestMap.put("sort_by", sortMap);
        }
    }

    private static Map<String, Object> getSearchRequest(AppContext appContext, IConfigService configService, ContentSearchCriteria criteria) {
        Map<String, Object> filterMap = new HashMap<>();

        // Populating implicit search criteria.
        filterMap.put("compatibilityLevel", getCompatibilityLevelFilter(appContext));
        filterMap.put("status", Arrays.asList(criteria.getContentStatusArray()));
        filterMap.put("objectType", Collections.singletonList("Content"));
        if (!CollectionUtil.isEmpty(criteria.getContentTypes())) {
            filterMap.put("contentType", Arrays.asList(criteria.getContentTypes()));
        }
        if (criteria.getKeywords() != null && criteria.getKeywords().length > 0) {
            filterMap.put("keywords", Arrays.asList(criteria.getKeywords()));
        }

        // Add createdBy filter
        if (!CollectionUtil.hasEmptyData(criteria.getCreatedBy())) {
            filterMap.put("createdBy", Arrays.asList(criteria.getCreatedBy()));
        }

        //Add filters for criteria attributes
        // Add subject filter
        if (criteria.getAge() > 0) {
            applyListingFilter(configService, MasterDataType.AGEGROUP, String.valueOf(criteria.getAge()), filterMap, false);
        }

        // Add standard filter
        if (criteria.getGrade() > 0) {
            applyListingFilter(configService, MasterDataType.GRADELEVEL, String.valueOf(criteria.getGrade()), filterMap, false);
        }

        // Add medium filter
        String[] mediumArr = criteria.getMedium();

        if (!CollectionUtil.isEmpty(mediumArr)) {
            for (String medium : mediumArr) {
                applyListingFilter(configService, MasterDataType.MEDIUM, medium, filterMap, false);
            }

        }

        // Add board filter
        String[] boardArr = criteria.getBoard();
        if (!CollectionUtil.isEmpty(boardArr)) {
            for (String board : boardArr) {
                applyListingFilter(configService, MasterDataType.BOARD, board, filterMap, false);
            }
        }

        String[] audienceArr = criteria.getAudience();
        if (!CollectionUtil.isEmpty(audienceArr)) {
            for (String audience : audienceArr) {
                applyListingFilter(configService, MasterDataType.AUDIENCE, audience, filterMap, false);
            }
        }

        String[] channelArr = criteria.getChannel();
        if (!CollectionUtil.isEmpty(channelArr)) {
            for (String channel : channelArr) {
                applyListingFilter(configService, MasterDataType.CHANNEL, channel, filterMap, false);
            }
        }

        boolean exclPragma = true;
        String[] pragmaArr = criteria.getExclPragma();
        if (CollectionUtil.isEmpty(pragmaArr)) {
            pragmaArr = criteria.getPragma();
            exclPragma = false;
        }

        if (!CollectionUtil.isEmpty(pragmaArr)) {
            for (String pragma : pragmaArr) {
                applyListingFilter(configService, MasterDataType.PRAGMA, pragma, filterMap, exclPragma);
            }
        }

        return filterMap;
    }

    private static Map<String, Object> getSearchRequest(AppContext appContext, IConfigService configService, SunbirdContentSearchCriteria criteria) {
        Map<String, Object> filterMap = new HashMap<>();

        // Populating implicit search criteria.
        filterMap.put("compatibilityLevel", getCompatibilityLevelFilter(appContext));
        filterMap.put("status", Arrays.asList(criteria.getContentStatusArray()));
        filterMap.put("objectType", Collections.singletonList("Content"));
        if (!CollectionUtil.isEmpty(criteria.getContentTypes())) {
            filterMap.put("contentType", Arrays.asList(criteria.getContentTypes()));
        }
        if (!CollectionUtil.hasEmptyData(criteria.getKeywords())) {
            filterMap.put("keywords", Arrays.asList(criteria.getKeywords()));
        }
        if (!CollectionUtil.hasEmptyData(criteria.getDialCodes())) {
            filterMap.put("dialcodes", Arrays.asList(criteria.getDialCodes()));
        }

        // Add createdBy filter
        if (!CollectionUtil.hasEmptyData(criteria.getCreatedBy())) {
            filterMap.put("createdBy", Arrays.asList(criteria.getCreatedBy()));
        }

        //Add filters for criteria attributes
        // Add subject filter
        if (criteria.getAge() > 0) {
            applyListingFilter(configService, MasterDataType.AGEGROUP, String.valueOf(criteria.getAge()), filterMap, false);
        }

        // Add standard filter
        if (!CollectionUtil.hasEmptyData(criteria.getGrade())) {
            filterMap.put("gradeLevel", Arrays.asList(criteria.getGrade()));
        }

        // Add medium filter
        if (!CollectionUtil.hasEmptyData(criteria.getMedium())) {
            filterMap.put("medium", Arrays.asList(criteria.getMedium()));
        }

        // Add board filter
        if (!CollectionUtil.hasEmptyData(criteria.getBoard())) {
            filterMap.put("board", Arrays.asList(criteria.getBoard()));
        }

        if (!CollectionUtil.hasEmptyData(criteria.getLanguage())) {
            filterMap.put("language", Arrays.asList(criteria.getLanguage()));
        }

        String[] audienceArr = criteria.getAudience();
        if (!CollectionUtil.isEmpty(audienceArr)) {
            for (String audience : audienceArr) {
                applyListingFilter(configService, MasterDataType.AUDIENCE, audience, filterMap, false);
            }
        }

        String[] channelArr = criteria.getChannel();
        if (!CollectionUtil.isEmpty(channelArr)) {
            for (String channel : channelArr) {
                applyListingFilter(configService, MasterDataType.CHANNEL, channel, filterMap, false);
            }
        }

        boolean exclPragma = true;
        String[] pragmaArr = criteria.getExclPragma();
        if (CollectionUtil.isEmpty(pragmaArr)) {
            pragmaArr = criteria.getPragma();
            exclPragma = false;
        }

        if (!CollectionUtil.isEmpty(pragmaArr)) {
            for (String pragma : pragmaArr) {
                applyListingFilter(configService, MasterDataType.PRAGMA, pragma, filterMap, exclPragma);
            }
        }

        return filterMap;
    }

    private static Map<String, Object> getFilterRequest(AppContext appContext, ContentSearchCriteria criteria) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("compatibilityLevel", getCompatibilityLevelFilter(appContext));
        if (criteria.getFacetFilters() != null) {
            for (ContentSearchFilter filter : criteria.getFacetFilters()) {
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

        if (criteria.getImpliedFilters() != null) {
            for (ContentSearchFilter filter : criteria.getImpliedFilters()) {
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

        if (!filterMap.containsKey("contentType")) {
            filterMap.put("contentType", Arrays.asList(criteria.getContentTypes()));
        }

        return filterMap;
    }

    private static Map<String, Object> getFilterRequest(AppContext appContext, SunbirdContentSearchCriteria criteria) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("compatibilityLevel", getCompatibilityLevelFilter(appContext));
        if (criteria.getFacetFilters() != null) {
            for (ContentSearchFilter filter : criteria.getFacetFilters()) {
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

        if (criteria.getImpliedFilters() != null) {
            for (ContentSearchFilter filter : criteria.getImpliedFilters()) {
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

        if (!filterMap.containsKey("contentType")) {
            filterMap.put("contentType", Arrays.asList(criteria.getContentTypes()));
        }

        return filterMap;
    }

    private static Map<String, Integer> getCompatibilityLevelFilter(AppContext appContext) {
        Map<String, Integer> compatibilityLevelMap = new HashMap<>();
        compatibilityLevelMap.put("min", appContext.getParams().getInt(IParams.Key.MIN_COMPATIBILITY_LEVEL));
        compatibilityLevelMap.put("max", appContext.getParams().getInt(IParams.Key.MAX_COMPATIBILITY_LEVEL));
        return compatibilityLevelMap;
    }

    private static void applyListingFilter(IConfigService configService, MasterDataType masterDataType, String propertyValue, Map<String, Object> filterMap, boolean excludeFilter) {
        if (configService != null && propertyValue != null) {

            if (masterDataType == MasterDataType.AGEGROUP) {
                masterDataType = MasterDataType.AGE;
            }

            GenieResponse<MasterData> masterDataResponse = configService.getMasterData(masterDataType);

            if (masterDataResponse.getStatus()) {
                MasterData masterData = masterDataResponse.getResult();

                for (MasterDataValues masterDataValues : masterData.getValues()) {
                    if (propertyValue.equals(masterDataValues.getTelemetry())) {
                        Map<String, Object> searchMap = masterDataValues.getSearch();

                        // inclusion/exclusion Filter map in terms API.
                        if (excludeFilter) {
                            applyExclusionFilter(filterMap, searchMap);
                        } else {
                            applyInclusionFilter(filterMap, searchMap);
                        }
                        break;
                    }
                }
            }
        }
    }

    private static void applyInclusionFilter(Map<String, Object> filterMap, Map<String, Object> searchMap) {
        Map<String, Object> filtersMap = (Map<String, Object>) searchMap.get("filters");
        if (filtersMap != null) {
            for (Map.Entry<String, Object> entry : filtersMap.entrySet()) {
                Set values = new HashSet();

                // Existing or already applied filter.
                Object filterMapValue = filterMap.get(entry.getKey());
                if (filterMapValue != null && filterMapValue instanceof List) {
                    values.addAll((List) filterMapValue);
                }

                // Add new filter in existing filter.
                values.addAll((List) filtersMap.get(entry.getKey()));

                filterMap.put(entry.getKey(), new ArrayList<>(values));
            }
        }
    }

    private static void applyExclusionFilter(Map<String, Object> filterMap, Map<String, Object> searchMap) {
        Map<String, Object> filtersMap = (Map<String, Object>) searchMap.get("exclFilters");
        if (filtersMap != null) {
            for (String key : filtersMap.keySet()) {
                Map appliedFilter = new HashMap<>();

                // Existing or already applied filter.
                Object filterMapValue = filterMap.get(key);
                if (filterMapValue != null && filterMapValue instanceof Map) {
                    Map<String, Object> map = (Map) filterMapValue;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        Set values = new HashSet();
                        values.addAll((List) entry.getValue());  // e.g. key would be "notIn"
                        appliedFilter.put(entry.getKey(), new ArrayList<>(values));
                    }
                }

                // Add new filter in existing filter.
                Map<String, Object> masterDataFilterMapValue = (Map) filtersMap.get(key);
                for (Map.Entry<String, Object> entry : masterDataFilterMapValue.entrySet()) {
                    Set values = new HashSet();
                    if (appliedFilter.get(entry.getKey()) != null) {
                        values.addAll((List) appliedFilter.get(entry.getKey()));
                    }
                    values.addAll((List) entry.getValue());     // e.g. key would be "notIn"
                    appliedFilter.put(entry.getKey(), new ArrayList<>(values));
                }

                filterMap.put(key, appliedFilter);
            }
        }
    }

    public static ContentSearchCriteria createFilterCriteria(IConfigService configService, ContentSearchCriteria previousCriteria,
                                                             List<Map<String, Object>> facets, Map<String, Object> appliedFilterMap) {
        List<ContentSearchFilter> facetFilters = new ArrayList<>();
        ContentSearchCriteria.FilterBuilder filterBuilder = new ContentSearchCriteria.FilterBuilder();
        filterBuilder.query(previousCriteria.getQuery())
                .limit(previousCriteria.getLimit())
                .contentTypes(previousCriteria.getContentTypes())
                .facets(previousCriteria.getFacets())
                .sort(previousCriteria.getSortCriteria() == null ? new ArrayList<ContentSortCriteria>() : previousCriteria.getSortCriteria());

        if ("soft".equals(previousCriteria.getMode())) {
            filterBuilder.softFilters();
        }

        if (facets == null) {
            filterBuilder.facetFilters(facetFilters);
            return filterBuilder.build();
        }

        Map<String, Object> ordinalsMap = new HashMap<>();
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
            if (ordinalsMap.containsKey(facetName)) {
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
                filter.setValues(values);

                // Set the filter
                facetFilters.add(filter);
            }

            appliedFilterMap.remove(facetName);
        }
        filterBuilder.facetFilters(facetFilters);
        filterBuilder.impliedFilters(mapFilterValues(appliedFilterMap));
        return filterBuilder.build();
    }

    public static SunbirdContentSearchCriteria createFilterCriteria(IConfigService configService, SunbirdContentSearchCriteria previousCriteria,
                                                                    List<Map<String, Object>> facets, Map<String, Object> appliedFilterMap) {
        List<ContentSearchFilter> facetFilters = new ArrayList<>();
        SunbirdContentSearchCriteria.FilterBuilder filterBuilder = new SunbirdContentSearchCriteria.FilterBuilder();
        filterBuilder.query(previousCriteria.getQuery())
                .limit(previousCriteria.getLimit())
                .offset(previousCriteria.getOffset())
                .contentTypes(previousCriteria.getContentTypes())
                .facets(previousCriteria.getFacets())
                .sort(previousCriteria.getSortCriteria() == null ? new ArrayList<ContentSortCriteria>() : previousCriteria.getSortCriteria());

        if ("soft".equals(previousCriteria.getMode())) {
            filterBuilder.softFilters();
        }

        if (facets == null) {
            filterBuilder.facetFilters(facetFilters);
            return filterBuilder.build();
        }

        Map<String, Object> ordinalsMap = new HashMap<>();
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
            if (ordinalsMap.containsKey(facetName)) {
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
                facetFilters.add(filter);
            }

            appliedFilterMap.remove(facetName);
        }
        filterBuilder.facetFilters(facetFilters);
        filterBuilder.impliedFilters(mapFilterValues(appliedFilterMap));
        return filterBuilder.build();
    }

    private static List<FilterValue> getSortedFilterValuesWithAppliedFilters(List<Map<String, Object>> facetValues, List<String> facetOrder, List<String> appliedFilter) {
        Map<Integer, FilterValue> map = new TreeMap<>();
        List<FilterValue> list = new ArrayList<>();

        for (Map<String, Object> valueMap : facetValues) {
            String name = (String) valueMap.get("name");
            String translations = (String) valueMap.get("translations");
            int index = indexOf(facetOrder, name);

            boolean applied = false;
            if (appliedFilter != null && appliedFilter.contains(name)) {
                applied = true;
            }

            FilterValue filterValue = new FilterValue();
            filterValue.setName(name);
            filterValue.setApply(applied);
            filterValue.setTranslations(translations);
            if (valueMap.containsKey("count")) {
                Double count = (Double) valueMap.get("count");
                filterValue.setCount(count.intValue());
            }

            if (index == -1) {
                list.add(filterValue);
            } else {
                map.put(index, filterValue);
            }
        }

        int index = 0;
        List<Integer> keys = new ArrayList<>(map.keySet());
        if (keys.size() > 0) {
//            Collections.sort(keys);
            index = keys.get(keys.size() - 1);
        }

        for (FilterValue filterValue : list) {
            index++;
            map.put(index, filterValue);
        }

        List<FilterValue> valuesList = new ArrayList<>(map.values());
        return valuesList;
    }

    private static int indexOf(List<String> facetsOrder, String key) {
        if (!CollectionUtil.isNullOrEmpty(facetsOrder) && !StringUtil.isNullOrEmpty(key)) {
            for (int i = 0; i < facetsOrder.size(); i++) {
                if (key.equalsIgnoreCase(facetsOrder.get(i))) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static Map<String, Object> getRecommendedContentRequest(RecommendedContentRequest request, String did) {
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("did", did);
        contextMap.put("dlang", request.getLanguage());

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("context", contextMap);
        requestMap.put("limit", request.getLimit());

        return requestMap;
    }

    public static Map<String, Object> getRelatedContentRequest(RelatedContentRequest request, String did) {
        String dlang = "";
        String uid = "";
        if (!StringUtil.isNullOrEmpty(request.getUid())) {
            uid = request.getUid();
        }
        if (!StringUtil.isNullOrEmpty(request.getLanguage())) {
            dlang = request.getLanguage();
        }

        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("contentid", request.getContentId());
        contextMap.put("uid", uid);
        contextMap.put("dlang", dlang);
        contextMap.put("did", did);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("context", contextMap);
        requestMap.put("limit", request.getLimit());

        return requestMap;
    }

    public static void refreshContentListingFromServer(final AppContext appContext, final IConfigService configService,
                                                       final ContentListingCriteria contentListingCriteria, final String did) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchContentListingFromServer(appContext, configService, contentListingCriteria, did);
            }
        }).start();
    }

    public static String fetchContentListingFromServer(AppContext appContext, IConfigService configService, ContentListingCriteria contentListingCriteria, String did) {
        Map<String, Object> requestMap = getContentListingRequest(appContext, configService, contentListingCriteria, did);
        ContentListingAPI api = new ContentListingAPI(appContext, contentListingCriteria.getContentListingId(), requestMap);
        GenieResponse apiResponse = api.post();
        String jsonStr = null;
        if (apiResponse.getStatus()) {
            jsonStr = apiResponse.getResult().toString();
            saveContentListingDataInDB(appContext, contentListingCriteria, jsonStr);
        }
        return jsonStr;
    }

    private static Map<String, Object> getContentListingRequest(AppContext appContext, IConfigService configService, ContentListingCriteria contentListingCriteria, String did) {
        Map<String, Object> contextMap = new HashMap<>();

        if (!StringUtil.isNullOrEmpty(contentListingCriteria.getUid())) {
            contextMap.put("uid", contentListingCriteria.getUid());
        }
        if (!StringUtil.isNullOrEmpty(contentListingCriteria.getLanguage())) {
            contextMap.put("dlang", contentListingCriteria.getLanguage());
        }
        contextMap.put("did", did);
        contextMap.put("contentid", "");

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("compatibilityLevel", getCompatibilityLevelFilter(appContext));

        // Add subject filter
        if (!StringUtil.isNullOrEmpty(contentListingCriteria.getSubject()))
            applyListingFilter(configService, MasterDataType.SUBJECT, contentListingCriteria.getSubject(), filterMap, false);

        if (contentListingCriteria.getAge() > 0)
            applyListingFilter(configService, MasterDataType.AGEGROUP, String.valueOf(contentListingCriteria.getAge()), filterMap, false);

        // Add board filter
        String[] boardArr = contentListingCriteria.getBoard();
        if (!CollectionUtil.isEmpty(boardArr))
            for (String board : boardArr) {
                applyListingFilter(configService, MasterDataType.BOARD, board, filterMap, false);
            }


        // Add medium filter
        String[] mediumArr = contentListingCriteria.getMedium();
        if (!CollectionUtil.isEmpty(mediumArr))
            for (String medium : mediumArr) {
                applyListingFilter(configService, MasterDataType.MEDIUM, medium, filterMap, false);

            }

        // Add standard filter
        if (contentListingCriteria.getGrade() > 0)
            applyListingFilter(configService, MasterDataType.GRADELEVEL, String.valueOf(contentListingCriteria.getGrade()), filterMap, false);

        String[] audienceArr = contentListingCriteria.getAudience();
        if (!CollectionUtil.isEmpty(audienceArr)) {
            for (String audience : audienceArr) {
                applyListingFilter(configService, MasterDataType.AUDIENCE, audience, filterMap, false);
            }
        }

        String[] channelArr = contentListingCriteria.getChannel();
        if (!CollectionUtil.isEmpty(channelArr)) {
            for (String channel : channelArr) {
                applyListingFilter(configService, MasterDataType.CHANNEL, channel, filterMap, false);
            }
        }

        boolean exclPragma = true;
        String[] pragmaArr = contentListingCriteria.getExclPragma();
        if (CollectionUtil.isEmpty(pragmaArr)) {
            pragmaArr = contentListingCriteria.getPragma();
            exclPragma = false;
        }

        if (!CollectionUtil.isEmpty(pragmaArr)) {
            for (String pragma : pragmaArr) {
                applyListingFilter(configService, MasterDataType.PRAGMA, pragma, filterMap, exclPragma);
            }
        }

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("context", contextMap);
        requestMap.put("filters", filterMap);

        return requestMap;
    }

    private static void saveContentListingDataInDB(AppContext appContext, ContentListingCriteria contentListingCriteria, String jsonStr) {
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

    public static ContentListing getContentListingResult(ContentListingCriteria contentListingCriteria, String jsonStr) {
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
                contentListing.setContentListingSections(getSectionsFromPageMap((Map<String, Object>) result.get("page")));
            }
        }

        return contentListing;
    }

    private static List<ContentListingSection> getSectionsFromPageMap(Map<String, Object> pageMap) {
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
                ContentSearchCriteria.FilterBuilder builder = new ContentSearchCriteria.FilterBuilder();
                if (searchMap.containsKey("query")) {
                    builder.query((String) searchMap.get("query"));
                }

                if (searchMap.containsKey("mode") && "soft".equals(searchMap.get("mode"))) {
                    builder.softFilters();
                }

                if (searchMap.containsKey("sort_by")) {
                    Map sortMap = (Map) searchMap.get("sort_by");
                    if (sortMap != null) {
                        Iterator it = sortMap.entrySet().iterator();
                        List<ContentSortCriteria> sortCriterias = new ArrayList<>();
                        while (it.hasNext()) {
                            Map.Entry keyValue = (Map.Entry) it.next();
                            ContentSortCriteria criteria = new ContentSortCriteria(keyValue.getKey().toString(), SortOrder.valueOf(keyValue.getValue().toString().toUpperCase()));
                            sortCriterias.add(criteria);
                        }
                        builder.sort(sortCriterias);
                    }
                } else {
                    builder.sort(new ArrayList<ContentSortCriteria>());
                }

                if (searchMap.containsKey("filters")) {
                    Map filtersMap = (Map) searchMap.get("filters");
                    if (filtersMap.containsKey("contentType")) {
                        List<String> contentType = (ArrayList<String>) filtersMap.get("contentType");
                        builder.contentTypes(contentType.toArray(new String[contentType.size()]));
                    }

                    builder.impliedFilters(mapFilterValues(filtersMap));
                }

                if (searchMap.containsKey("facets")) {
                    List<String> facets = (ArrayList<String>) searchMap.get("facets");
                    builder.facets(facets.toArray(new String[facets.size()]));
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

    public static SunbirdContentSearchCriteria getSearchCriteria(Map<String, Object> searchMap) {
        SunbirdContentSearchCriteria contentSearchCriteria = null;
        if (searchMap != null) {
            SunbirdContentSearchCriteria.FilterBuilder builder = new SunbirdContentSearchCriteria.FilterBuilder();
            if (searchMap.containsKey("query")) {
                builder.query((String) searchMap.get("query"));
            }

            if (searchMap.containsKey("exists")) {
                List<String> exists = (ArrayList<String>) searchMap.get("exists");
                builder.exists(exists.toArray(new String[exists.size()]));
            }

            if (searchMap.containsKey("mode") && "soft".equals(searchMap.get("mode"))) {
                builder.softFilters();
            }

            if (searchMap.containsKey("sort_by")) {
                Map sortMap = (Map) searchMap.get("sort_by");
                if (sortMap != null) {
                    Iterator it = sortMap.entrySet().iterator();
                    List<ContentSortCriteria> sortCriterias = new ArrayList<>();
                    while (it.hasNext()) {
                        Map.Entry keyValue = (Map.Entry) it.next();
                        ContentSortCriteria criteria = new ContentSortCriteria(keyValue.getKey().toString(), SortOrder.valueOf(keyValue.getValue().toString().toUpperCase()));
                        sortCriterias.add(criteria);
                    }
                    builder.sort(sortCriterias);
                }
            } else {
                builder.sort(new ArrayList<ContentSortCriteria>());
            }

            if (searchMap.containsKey("filters")) {
                Map filtersMap = (Map) searchMap.get("filters");
                if (filtersMap.containsKey("contentType")) {
                    List<String> contentType = (ArrayList<String>) filtersMap.get("contentType");
                    builder.contentTypes(contentType.toArray(new String[contentType.size()]));
                }

                builder.impliedFilters(mapFilterValues(filtersMap));
            }

            if (searchMap.containsKey("facets")) {
                List<String> facets = (ArrayList<String>) searchMap.get("facets");
                builder.facets(facets.toArray(new String[facets.size()]));
            }

            contentSearchCriteria = builder.build();
        }

        return contentSearchCriteria;
    }

    private static List<ContentSearchFilter> mapFilterValues(Map filtersMap) {
        List<ContentSearchFilter> filters = new ArrayList<>();
        if (filtersMap != null && !filtersMap.isEmpty()) {
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
//                  TODO: No change required here. Best of genie will be removed soon and so we don't have to handle the genieScore.
//                  TODO: Also compatibility level gets auto added into every search.
//                  key.equals("compatibilityLevel") && key.equals("genieScore")
//                  String[] stringArray = mFilterMap.get(values.getName());
//                  filterSet.addAll(Arrays.asList(stringArray));
                }
            }
        }
        return filters;
    }

    public static String getDownloadUrl(ContentData contentData) {
        String downloadUrl = null;

        if (ContentConstants.MimeType.COLLECTION.equals(contentData.getMimeType())) {
            Map variantMap = contentData.getVariants();
            if (variantMap != null && variantMap.get("spine") != null) {
                Map spineData = (Map) variantMap.get("spine");
                if (spineData != null) {
                    downloadUrl = spineData.get("ecarUrl").toString();
                }
            }
        }

        if (StringUtil.isNullOrEmpty(downloadUrl)) {
            downloadUrl = contentData.getDownloadUrl();
        }

        if (downloadUrl != null) {
            downloadUrl = downloadUrl.trim();
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
                List<ContentModel> preRequisitesListInDB = findAllContentsWithIdentifiers(dbSession, preRequisitesIdentifiers);
                if (preRequisitesListInDB != null) {
                    queue.addAll(preRequisitesListInDB);
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
                pkgVersion = readPkgVersion(firstContent.getLocalData());
            } catch (Exception e) {
                pkgVersion = 0;
            }

            fileName = String.format(Locale.US, "%s-v%s%s.ecar", name, "" + pkgVersion, appendName);
        }

        return fileName;
    }

    // TODO: 5/10/17 Re-iterating of the queue should be optimised
    public static void createAndWriteManifest(AppContext appContext, List<ContentModel> contentModelList) {
        for (ContentModel contentModel : contentModelList) {
            Queue<ContentModel> queue = new LinkedList<>();

            queue.add(contentModel);

            ContentModel node;

            List<ContentModel> contentWithAllChildren = new ArrayList<>();
            contentWithAllChildren.add(contentModel);

            while (!queue.isEmpty()) {
                node = queue.remove();

                if (hasChildren(node.getLocalData())) {
                    List<String> childContentsIdentifiers = getChildContentsIdentifiers(node.getLocalData());
                    List<ContentModel> contentModelListInDB = findAllContentsWithIdentifiers(appContext.getDBSession(), childContentsIdentifiers);
                    if (contentModelListInDB != null) {
                        queue.addAll(contentModelListInDB);
                        contentWithAllChildren.addAll(contentModelListInDB);
                    }
                }
            }

            Map<String, Object> manifest = createManifest(appContext, contentWithAllChildren);

            try {
                FileUtil.writeManifest(new File(contentModel.getPath()), manifest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: 5/10/17 This method needs to be reused in CreateContentExportManifest class
    private static Map<String, Object> createManifest(AppContext appContext, List<ContentModel> contentWithAllChildren) {
        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Map<String, Object>> contentIndex = new HashMap<>();
        List<String> childIdentifiers = new ArrayList<>();
        List<String> allContents = new ArrayList<>();
        Map<String, Object> item;


        for (ContentModel c : contentWithAllChildren) {
            // item local data
            item = GsonUtil.fromJson(c.getLocalData(), Map.class);

            // index item
            contentIndex.put(c.getIdentifier(), item);
            addViralityMetadataIfMissing(item, appContext.getDeviceInfo().getDeviceID());

            // get item's children only to mark children with visibility as Parent
            if (hasChildren(item)) {
                // store children identifiers
                childIdentifiers.addAll(getChildContentsIdentifiers(item));
            }
            if (hasPreRequisites(item)) {
                // store children identifiers
                childIdentifiers.addAll(getPreRequisitesIdentifiers(item));
            }

            allContents.add(c.getIdentifier());
        }

        for (String contentIdentifier : allContents) {
            Map<String, Object> contentData = contentIndex.get(contentIdentifier);
            if (childIdentifiers.contains(contentIdentifier)) {
                contentData.put(KEY_VISIBILITY, ContentConstants.Visibility.PARENT);
            }
            items.add(contentData);
        }

        HashMap<String, Object> archive = new HashMap<>();
        archive.put("ttl", ContentConstants.TTL);
        archive.put("count", items.size());
        archive.put("items", items);

        // Initialize manifest
        Map<String, Object> manifest = new HashMap<>();
        manifest.put("id", ContentConstants.EKSTEP_CONTENT_ARCHIVE);
        manifest.put("ver", ContentConstants.SUPPORTED_MANIFEST_VERSION);
        manifest.put("ts", DateUtil.getFormattedDateWithTimeZone(DateUtil.TIME_ZONE_GMT));
        manifest.put("archive", archive);
        return manifest;
    }

    /**
     * This method deletes all the contents that were manually deleted from the memory and reflects the same in the db
     *
     * @param dbSession
     * @param deletedIdentifier
     */
    public static void deleteContentsFromDb(IDBSession dbSession, String deletedIdentifier) {
        List<ContentModel> deletedIdsContentModels = new ArrayList<>();
        List<String> deletedIdentifierList = new ArrayList<>();
        deletedIdentifierList.add(deletedIdentifier);

        if (deletedIdentifierList.size() > 0) {
            deletedIdsContentModels.addAll(findAllContentsWithIdentifiers(dbSession, deletedIdentifierList));

            if (deletedIdsContentModels.size() > 0) {
                for (ContentModel contentModel : deletedIdsContentModels) {
                    //update the content state to spine
                    if (ContentConstants.MimeType.COLLECTION.equals(contentModel.getMimeType()) && contentModel.getRefCount() > 1) {
                        contentModel.addOrUpdateContentState(ContentConstants.State.ARTIFACT_AVAILABLE);
                    } else {
                        contentModel.addOrUpdateContentState(ContentConstants.State.ONLY_SPINE);
                    }

                    //check the ref-count and update the value
                    if (contentModel.getVisibility().equalsIgnoreCase(ContentConstants.Visibility.DEFAULT) &&
                            contentModel.getRefCount() > 0) {
                        contentModel.addOrUpdateRefCount(contentModel.getRefCount() - 1);
                    }

                    contentModel.setVisibility(ContentConstants.Visibility.PARENT);

                    contentModel.update();
                }
            }

        }
    }

    /**
     * This method returns only the valid identifiers, present in a particular folder by checking if the manifest of that content is valid
     *
     * @param appContext
     * @param storageFolder
     * @param addedContentIdentifiers
     * @return
     */
    public static List<String> getValidIdentifiersFromPath(AppContext appContext, File storageFolder, List<String> addedContentIdentifiers) {
        List<String> validIdentifiers = new ArrayList<>();

        // Read content in destination folder.
        for (String file : addedContentIdentifiers) {
            File destFile = new File(storageFolder, file);
            if (destFile.isDirectory()) {
                String manifestJson = FileUtil.readManifest(destFile);
                if (manifestJson == null) {
                    continue;
                }

                LinkedTreeMap manifestMap = GsonUtil.fromJson(manifestJson, LinkedTreeMap.class);

                String manifestVersion = (String) manifestMap.get("ver");
                if (manifestVersion.equals("1.0")) {
                    continue;
                }

                LinkedTreeMap archive = (LinkedTreeMap) manifestMap.get("archive");
                List<Map<String, Object>> items = null;
                if (archive.containsKey("items")) {
                    items = (List<Map<String, Object>>) archive.get("items");
                }

                if (items == null || items.isEmpty()) {
                    continue;
                }

                Logger.d(TAG, items.toString());

                for (Map<String, Object> item : items) {
                    String visibility = readVisibility(item);

                    // If compatibility level is not in range then do not copy artifact
                    if (ContentConstants.Visibility.PARENT.equals(visibility)
                            || !isCompatible(appContext, readCompatibilityLevel(item))) {
                        continue;
                    }

                    boolean isDraftContent = isDraftContent(readStatus(item));
                    //Draft content expiry .To prevent import of draft content if the expires date is lesser than from the current date.
                    if (isDraftContent && isExpired(readExpiryDate(item))) {
                        continue;
                    }

                    validIdentifiers.add(file);
                }
            }
        }

        return validIdentifiers;
    }

    /**
     * This method adds the necessary information of the content to db, when the content is added manually.
     *
     * @param appContext
     * @param identifier
     * @param storageFolder
     */
    public static void addContentToDb(AppContext appContext, String identifier, File storageFolder, boolean keepLowerVersion) {
        String mimeType, contentType, visibility, audience, pragma, path;
        Double compatibilityLevel, pkgVersion;
        int refCount;
        int contentState = ContentConstants.State.ONLY_SPINE;
        String oldContentPath;
        String artifactUrl;
        ContentModel oldContentModel;

//        read the manifest for that identifier and read the items from the same manifest and pass it to below loop
        File destFile = new File(storageFolder, identifier);

        if (destFile.isDirectory()) {
            String manifestJson = FileUtil.readManifest(destFile);

            LinkedTreeMap manifestMap = GsonUtil.fromJson(manifestJson, LinkedTreeMap.class);

            LinkedTreeMap archive = (LinkedTreeMap) manifestMap.get("archive");
            List<Map<String, Object>> items = (List<Map<String, Object>>) archive.get("items");

            String manifestVersion = (String) manifestMap.get("ver");

            for (Map<String, Object> item : items) {
                mimeType = readMimeType(item);
                contentType = readContentType(item);
                visibility = readVisibility(item);
                audience = readAudience(item);
                pragma = readPragma(item);
                compatibilityLevel = readCompatibilityLevel(item);
                pkgVersion = readPkgVersion(item);
                artifactUrl = readArtifactUrl(item);

                oldContentModel = ContentModel.find(appContext.getDBSession(), identifier);
                oldContentPath = oldContentModel == null ? null : oldContentModel.getPath();
                boolean isContentExist = isContentExist(oldContentModel, identifier, pkgVersion, keepLowerVersion);

                //Apk files
                if ((!StringUtil.isNullOrEmpty(mimeType) && mimeType.equalsIgnoreCase(ContentConstants.MimeType.APK)) ||
                        (!StringUtil.isNullOrEmpty(artifactUrl) && artifactUrl.contains("." + ServiceConstants.FileExtension.APK))) {
                    contentState = ContentConstants.State.ONLY_SPINE;
                } else {
                    //If the content is exist then copy the old content data and add it into new content.
                    if (isContentExist && !(ContentConstants.ContentStatus.DRAFT.equalsIgnoreCase(readStatus(item)))) {
                        if (oldContentModel.getVisibility().equalsIgnoreCase(ContentConstants.Visibility.DEFAULT)) {
                            Map<String, Object> oldContentLocalDataMap = GsonUtil.fromJson(oldContentModel.getLocalData(), Map.class);

                            item.clear();
                            item.putAll(oldContentLocalDataMap);
                        }
                    } else {
                        isContentExist = false;

                        // If compatibility level is not in range then do not copy artifact
                        if (isCompatible(appContext, compatibilityLevel)) {
                            // Add or update the content_state
                            if (ContentConstants.MimeType.COLLECTION.equals(mimeType)) {
                                contentState = ContentConstants.State.ARTIFACT_AVAILABLE;
                            } else {
                                // TODO: 11/10/17 how will you check if the content is spine or not
                                contentState = ContentConstants.State.ARTIFACT_AVAILABLE;
                            }
                        }
                    }
                }

                //add or update the reference count for the content
                if (oldContentModel != null) {
                    refCount = oldContentModel.getRefCount();

//                if (!importContext.isChildContent()) {    // If import started from child content then do not update the refCount.
                    // if the content has a 'Default' visibility and update the same content then don't increase the reference count...
                    if (!(ContentConstants.Visibility.DEFAULT.equals(oldContentModel.getVisibility()) && ContentConstants.Visibility.DEFAULT.equalsIgnoreCase(visibility))) {
                        refCount = refCount + 1;
                    }
//                }
                } else {
                    refCount = 1;
                }

                // Set content visibility
                if ("Library".equalsIgnoreCase(readObjectType(item))) {
                    visibility = ContentConstants.Visibility.PARENT;
                } else if (oldContentModel != null) {
                    if (!ContentConstants.Visibility.PARENT.equals(oldContentModel.getVisibility())) {  // If not started from child content then do not shrink visibility.
                        visibility = oldContentModel.getVisibility();
                    }
                }

                // Add or update the content_state. contentState should not update the spine_only when importing the spine content after importing content with artifacts.
                if (oldContentModel != null && oldContentModel.getContentState() > contentState) {
                    contentState = oldContentModel.getContentState();
                }

                //updated the content path if the content is already exists.
                if (!isContentExist) {
                    path = destFile.getPath();
                } else {
                    path = oldContentPath;
                }

                long sizeOnDevice = 0;
                if (!StringUtil.isNullOrEmpty(path)) {
                    sizeOnDevice = FileUtil.getFileSize(new File(path));
                }

                addOrUpdateViralityMetadata(item, appContext.getDeviceInfo().getDeviceID());
                ContentModel newContentModel = ContentModel.build(appContext.getDBSession(), identifier, manifestVersion, GsonUtil.toJson(item),
                        mimeType, contentType, visibility, path, refCount, contentState, audience, pragma, sizeOnDevice);

                if (oldContentModel == null) {
                    newContentModel.save();
                } else {
                    newContentModel.update();
                }
            }
        }
    }

    public static void updateSizeOnDevice(AppContext appContext) {
        List<ContentModel> dbContentModelList = findAllContent(appContext.getDBSession());
        if (!CollectionUtil.isNullOrEmpty(dbContentModelList)) {
            for (ContentModel contentModel : dbContentModelList) {
                if (hasChildren(contentModel.getLocalData())) {
                    long sizeOnDevice = 0;
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

                        if (ContentConstants.MimeType.COLLECTION.equals(node.getMimeType())) {
                            sizeOnDevice = sizeOnDevice + FileUtil.getFileSize(new File(node.getPath()));
                        } else {
                            sizeOnDevice = sizeOnDevice + node.getSizeOnDevice();
                        }
                    }

                    contentModel.setSizeOnDevice(sizeOnDevice);
                    contentModel.doNotUpdateLocalLastUpdatedTime();
                    contentModel.update();
                }
            }
        }
    }

    public static long getUsageSpace(String path, AppContext appContext) {
        String query = String.format(Locale.US, "select sum(%s) from %s where %s LIKE '%s' AND %s != '%s';",
                ContentEntry.COLUMN_NAME_SIZE_ON_DEVICE, ContentEntry.TABLE_NAME, ContentEntry.COLUMN_NAME_PATH, (path + "%"), ContentEntry.COLUMN_NAME_MIME_TYPE, ContentConstants.MimeType.COLLECTION);
        return ContentsModel.totalSizeOnDevice(appContext.getDBSession(), query);
    }

    public static Map<String, Object> getFlagContentRequestMap(FlagContentRequest flagContentRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("flagReasons", flagContentRequest.getFlagReasons());
        requestMap.put("flaggedBy", flagContentRequest.getFlaggedBy());
        requestMap.put("versionKey", flagContentRequest.getVersionKey());
        requestMap.put("flags", flagContentRequest.getFlags());
        return requestMap;
    }

    public static String addOrUpdateDialcodeMapping(String jsonStr, String identifier, String rootNodeIdentifier) {
        Map<String, Object> dialcodeMapping;
        if (!StringUtil.isNullOrEmpty(jsonStr)) {
            dialcodeMapping = GsonUtil.fromJson(jsonStr, Map.class);
        } else {
            dialcodeMapping = new HashMap<>();
        }

        if (!dialcodeMapping.containsKey("identifier")) {
            dialcodeMapping.put("identifier", identifier);
        }

        Set<String> rootNodes = new HashSet<>();
        if (dialcodeMapping.containsKey("rootNodes")) {
            rootNodes.addAll((List) dialcodeMapping.get("rootNodes"));
        }

        rootNodes.add(rootNodeIdentifier);
        dialcodeMapping.put("rootNodes", new ArrayList<>(rootNodes));

        return GsonUtil.toJson(dialcodeMapping);
    }
}
