package org.ekstep.genieservices.content;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IContentService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessCriteria;
import org.ekstep.genieservices.commons.bean.ContentData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.db.model.ContentsModel;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

/**
 * Created on 5/10/2017.
 *
 * @author anil
 */
public class ContentServiceImpl extends BaseService implements IContentService {

    private static final String TAG = ContentServiceImpl.class.getSimpleName();
    private static final int DEFAULT_PACKAGE_VERSION = -1;

    private IUserService userService;
    private IContentFeedbackService contentFeedbackService;

    public ContentServiceImpl(AppContext appContext, IUserService userService, IContentFeedbackService contentFeedbackService) {
        super(appContext);

        this.userService = userService;
        this.contentFeedbackService = contentFeedbackService;
    }

    @Override
    public GenieResponse<Content> getContentDetails(String contentIdentifier) {
        // TODO: Telemetry logger
        String methodName = "getContentDetails@ContentServiceImpl";

        GenieResponse<Content> response;
        ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), contentIdentifier);

        if (contentModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.NO_DATA_FOUND, "No content found for identifier = " + contentIdentifier, TAG);
            return response;
        }

        Content content = getContent(contentModel, true, true);

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(content);
        return response;
    }

    private Content getContent(ContentModel contentModel, boolean attachFeedback, boolean attachContentAccess) {
        Content content = new Content();
        content.setIdentifier(contentModel.getIdentifier());

        ContentData serverData = null;
        if (contentModel.getServerData() != null) {
            serverData = GsonUtil.fromJson(contentModel.getServerData(), ContentData.class);
        }

        ContentData localData = null;
        if (contentModel.getLocalData() != null) {
            localData = GsonUtil.fromJson(contentModel.getLocalData(), ContentData.class);
        }

        content.setContentData(serverData);

        content.setMimeType(contentModel.getMimeType());
        content.setBasePath(contentModel.getPath());
        content.setContentType(contentModel.getContentType());
        content.setAvailableLocally(isAvailableLocally(contentModel.getContentState()));
        content.setReferenceCount(contentModel.getRefCount());
        content.setUpdateAvailable(isUpdateAvailable(serverData, localData));

        long contentCreationTime = 0;
        String localLastUpdatedTime = contentModel.getLocalLastUpdatedTime();
        if (!StringUtil.isNullOrEmpty(localLastUpdatedTime)) {
            contentCreationTime = DateUtil.dateToEpoch(localLastUpdatedTime.substring(0, localLastUpdatedTime.lastIndexOf(".")), "yyyy-MM-dd'T'HH:mm:ss");
        }
        content.setLastUpdatedTime(contentCreationTime);

        String uid = getCurrentUserId();
        if (attachFeedback) {
            addContentFeedback(content, uid);
        }

        if (attachContentAccess && userService != null) {
            addContentAccess(content, uid);
        }

        return content;
    }

    private void addContentFeedback(Content content, String uid) {
        if (contentFeedbackService != null) {
            content.setContentFeedback(contentFeedbackService.getFeedback(uid, content.getIdentifier()).getResult());
        }
    }

    private void addContentAccess(Content content, String uid) {
        if (userService != null) {
            ContentAccessCriteria criteria = new ContentAccessCriteria();
            criteria.setUid(uid);
            criteria.setContentIdentifier(content.getIdentifier());

            List<ContentAccess> contentAccessList = userService.getAllContentAccess(criteria).getResult();
            if (contentAccessList.size() > 0) {
                content.setContentAccess(contentAccessList.get(0));
            }
        }
    }

    private boolean isUpdateAvailable(ContentData serverData, ContentData localData) {
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

    private boolean isAvailableLocally(int contentState) {
        return contentState == ContentConstants.State.ARTIFACT_AVAILABLE;
    }

    @Override
    public GenieResponse<List<Content>> getAllLocalContent() {
        // TODO: Telemetry logger
        String methodName = "getAllLocalContent@ContentServiceImpl";

        GenieResponse<List<Content>> response;
        String uid = getCurrentUserId();

        List<Content> contentList = getProfileSpecificContents(uid, getAllContentsFilter());

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentList);
        return response;
    }

    private String getCurrentUserId() {
        if (userService != null) {
            UserSession userSession = userService.getCurrentUserSession().getResult();
            if (userSession != null) {
                return userSession.getUid();
            }
        }
        return null;
    }

    private List<Content> getProfileSpecificContents(String uid, String contentFilter) {
        List<Content> childSpecificContents = new ArrayList<>();

        List<ContentModel> contentModelList;
        ContentsModel contentsModel = ContentsModel.find(mAppContext.getDBSession(), contentFilter);
        if (contentsModel != null) {
            contentModelList = contentsModel.getContentModelList();
        } else {
            contentModelList = new ArrayList<>();
        }

        // Get the content access for profile.
        List<ContentAccess> contentAccessList;
        if (userService != null) {
            ContentAccessCriteria criteria = new ContentAccessCriteria();
            criteria.setUid(uid);
            contentAccessList = userService.getAllContentAccess(criteria).getResult();
        } else {
            contentAccessList = new ArrayList<>();
        }

        for (ContentAccess contentAccess : contentAccessList) {
            ContentModel contentModel = ContentModel.find(mAppContext.getDBSession(), contentAccess.getIdentifier());
            if (contentModel != null && contentModelList.contains(contentModel)) {
                Content c = getContent(contentModel, false, false);
                c.setContentAccess(contentAccess);
                childSpecificContents.add(c);
                contentModelList.remove(contentModel);
            }
        }

        // Add the remaining content into list
        for (ContentModel contentModel : contentModelList) {
            Content c = getContent(contentModel, false, false);
            childSpecificContents.add(c);
        }

        return childSpecificContents;
    }

    /**
     * Get filter condition for profile specific contents.
     *
     * @return Filter.
     */
    private String getAllContentsFilter() {
        String isVisible = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_VISIBILITY, ContentConstants.Visibility.DEFAULT);
        // For hiding the non compatible imported content, which visibility is DEFAULT.
        String isArtifactAvailable = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);

        // Filter for content table
        return String.format(Locale.US, " where (%s AND %s)", isVisible, isArtifactAvailable);
    }

    /**
     * Get filter condition for profile specific contents.
     *
     * @return Filter.
     */
    private String getAllNonTextbookContentsFilter() {
        String isNotTextbook = String.format(Locale.US, "%s is not '%s'", ContentEntry.COLUMN_NAME_CONTENT_TYPE, ContentConstants.Type.TEXTBOOK);
        String isVisible = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_VISIBILITY, ContentConstants.Visibility.DEFAULT);
        // For hiding the non compatible imported content, which visibility is DEFAULT.
        String isArtifactAvailable = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_STATE, ContentConstants.State.ARTIFACT_AVAILABLE);

        // Filter for content table
        return String.format(Locale.US, " where (%s AND %s AND %s)", isVisible, isNotTextbook, isArtifactAvailable);
    }

    private String getAllTextbookContentFilter() {
        String isTextbook = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_CONTENT_TYPE, ContentConstants.Type.TEXTBOOK);
        String isVisible = String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_VISIBILITY, ContentConstants.Visibility.DEFAULT);

        // Filter for content table
        return String.format(Locale.US, " where (%s AND %s)", isVisible, isTextbook);
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
                childContentList = getAllChildContents(content, childContents);
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

        if (contentModel.isExternalContent() && mAppContext.getDeviceInfo().getAndroidSdkVersion() <= mAppContext.getDeviceInfo().getKitkatVersionCode()) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE, "This content cannot be deleted.", TAG);
            return response;
        }

        if (contentModel.hasPreRequisites()) {
            List<String> preRequisitesIdentifier = contentModel.getPreRequisitesIdentifiers();
            ContentsModel contentsModel = ContentsModel.findAllContentsWithIdentifiers(mAppContext.getDBSession(), preRequisitesIdentifier);

            if (contentsModel != null) {
                for (ContentModel c : contentsModel.getContentModelList()) {
                    deleteOrUpdateContent(c, true, level);
                }
            }
        }

        //delete or update child items
        if (contentModel.hasChildren()) {
            deleteAllChild(contentModel, level);
        }

        //delete or update parent items
        deleteOrUpdateContent(contentModel, false, level);

        if (contentModel.isExternalContent()) {
            FileHandler.refreshSDCard();
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        return response;
    }

    private void deleteAllChild(ContentModel contentModel, int level) {
        Queue<ContentModel> queue = new LinkedList<>();

        queue.add(contentModel);

        ContentModel node;
        while (!queue.isEmpty()) {
            node = queue.remove();

            if (node.hasChildren()) {
                List<String> childContentsIdentifiers = node.getChildContentsIdentifiers();
                ContentsModel contentsModel = ContentsModel.findAllContentsWithIdentifiers(mAppContext.getDBSession(), childContentsIdentifiers);
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

    private void deleteOrUpdateContent(ContentModel contentModel, boolean isChildItems, int level) {

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
            if (ContentConstants.Type.COLLECTION.equalsIgnoreCase(contentModel.getContentType())
                    || ContentConstants.Type.TEXTBOOK.equalsIgnoreCase(contentModel.getContentType())
                    || ContentConstants.Type.TEXTBOOK_UNIT.equalsIgnoreCase(contentModel.getContentType())) {
                contentModel.addOrUpdateContentState(ContentConstants.State.ARTIFACT_AVAILABLE);
            } else {
                contentModel.addOrUpdateContentState(ContentConstants.State.ONLY_SPINE);

                // if there are no entry in DB for any content then on this case contentModel.getPath() will be null
                if (contentModel.getPath() != null) {
                    FileHandler.rm(new File(contentModel.getPath()), contentModel.getIdentifier());
                }
            }

        } else {
            // TODO: This check should be before updating the existing refCount.
            // Do not update the content state if contentType is Collection / TextBook / TextBookUnit and refCount is more than 1.
            if ((ContentConstants.Type.COLLECTION.equalsIgnoreCase(contentModel.getContentType())
                    || ContentConstants.Type.TEXTBOOK.equalsIgnoreCase(contentModel.getContentType())
                    || ContentConstants.Type.TEXTBOOK_UNIT.equalsIgnoreCase(contentModel.getContentType()))
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
                    FileHandler.rm(new File(contentModel.getPath()), contentModel.getIdentifier());
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
}
