package org.ekstep.genieservices.content.utils;

import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessCriteria;
import org.ekstep.genieservices.commons.bean.ContentCriteria;
import org.ekstep.genieservices.commons.bean.ContentData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.bean.Variant;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.bean.ContentVariant;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.db.model.ContentsModel;
import org.ekstep.genieservices.content.network.ContentDetailsAPI;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 5/23/2017.
 *
 * @author anil
 */
public class ContentHandler {

    private static final int DEFAULT_PACKAGE_VERSION = -1;

    public static Map fetchContentDetails(AppContext appContext, String contentIdentifier) {
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
                Map contentData = ContentHandler.fetchContentDetails(appContext, contentIdentifier);

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

    public static Content getContent(ContentModel contentModel, boolean attachFeedback, boolean attachContentAccess, IContentFeedbackService contentFeedbackService, IUserService userService) {
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

        String uid = getCurrentUserId(userService);
        if (attachFeedback) {
            addContentFeedback(contentFeedbackService, content, uid);
        }

        if (attachContentAccess) {
            addContentAccess(userService, content, uid);
        }

        return content;
    }

    private static void addContentVariants(ContentData contentData, String dataJson) {
        List<Variant> variantList = new ArrayList<>();

        Type type = new TypeToken<List<HashMap<String, Object>>>() {
        }.getType();
        Map<String, Object> dataMap = GsonUtil.getGson().fromJson(dataJson, type);

        Object variants = dataMap.get("variants");
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

    private static void addContentFeedback(IContentFeedbackService contentFeedbackService, Content content, String uid) {
        if (contentFeedbackService != null) {
            content.setContentFeedback(contentFeedbackService.getFeedback(uid, content.getIdentifier()).getResult());
        }
    }

    private static void addContentAccess(IUserService userService, Content content, String uid) {
        if (userService != null) {
            List<ContentAccess> contentAccessList = getAllContentAccess(userService, uid, content.getIdentifier());
            if (contentAccessList.size() > 0) {
                content.setContentAccess(contentAccessList.get(0));
            }
        }
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

    public static String getCurrentUserId(IUserService userService) {
        if (userService != null) {
            UserSession userSession = userService.getCurrentUserSession().getResult();
            if (userSession != null) {
                return userSession.getUid();
            }
        }
        return null;
    }

    public static List<ContentModel> getAllLocalContentModel(AppContext appContext, ContentCriteria criteria) {
        String contentTypes;
        if (criteria.getContentTypes() != null) {
            List<String> contentTypeList = new ArrayList<>();
            for (ContentType contentType : criteria.getContentTypes()) {
                contentTypeList.add(contentType.getValue());
            }
            contentTypes = StringUtil.join("','", contentTypeList);
        } else {
            contentTypes = StringUtil.join("','", ContentType.values());
        }

        String isContentType = String.format(Locale.US, "%s in ('%s')", ContentEntry.COLUMN_NAME_CONTENT_TYPE, contentTypes);
        String isVisible = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_VISIBILITY, ContentConstants.Visibility.DEFAULT);
        // For hiding the non compatible imported content, which visibility is DEFAULT.
        String isArtifactAvailable = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);

        String filter = String.format(Locale.US, " where (%s AND %s AND %s)", isVisible, isArtifactAvailable, isContentType);

        List<ContentModel> contentModelListInDB;
        ContentsModel contentsModel = ContentsModel.find(appContext.getDBSession(), filter);
        if (contentsModel != null) {
            contentModelListInDB = contentsModel.getContentModelList();
        } else {
            contentModelListInDB = new ArrayList<>();
        }

        return contentModelListInDB;
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
}
