package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.Decompress;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.ImportContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ExtractPayloads implements IChainable<Void, ImportContentContext> {

    private static final String TAG = ExtractPayloads.class.getSimpleName();

    private IChainable<Void, ImportContentContext> nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContentContext importContext) {

        String identifier, mimeType, contentType, visibility, audience, path;
        Double compatibilityLevel, pkgVersion;
        int refCount;
        int contentState = ContentConstants.State.ONLY_SPINE;
        String oldContentPath;
        String artifactUrl, iconURL, posterImage, grayScaleAppIcon, uuid, destination;
        File payload;
        File payloadDestination = null;
        ContentModel oldContentModel;

        for (Map<String, Object> item : importContext.getItems()) {
            identifier = ContentHandler.readIdentifier(item);
            mimeType = ContentHandler.readMimeType(item);
            contentType = ContentHandler.readContentType(item);
            visibility = ContentHandler.readVisibility(item);
            audience = ContentHandler.readAudience(item);
            compatibilityLevel = ContentHandler.readCompatibilityLevel(item);
            pkgVersion = ContentHandler.readPkgVersion(item);
            artifactUrl = ContentHandler.readArtifactUrl(item);
            iconURL = ContentHandler.readAppIcon(item);
            posterImage = ContentHandler.readPosterImage(item);
            grayScaleAppIcon = ContentHandler.readGrayScaleAppIcon(item);

            //skip the content if already imported on the same version
            boolean isSkip = false;
            for (String skipIdentifier : importContext.getSkippedItemsIdentifier()) {
                if (skipIdentifier.equalsIgnoreCase(identifier)) {
                    isSkip = true;
                    break;
                }
            }
            if (isSkip) {
                continue;
            }

            oldContentModel = ContentModel.find(appContext.getDBSession(), identifier);
            oldContentPath = oldContentModel == null ? null : oldContentModel.getPath();
            boolean isContentExist = ContentHandler.isContentExist(oldContentModel, identifier, pkgVersion);

            //Apk files
            if ((!StringUtil.isNullOrEmpty(mimeType) && mimeType.equalsIgnoreCase(ContentConstants.MimeType.APK)) ||
                    (!StringUtil.isNullOrEmpty(artifactUrl) && artifactUrl.contains("." + ServiceConstants.FileExtension.APK))) {

                List<Map<String, Object>> preRequisites = (List<Map<String, Object>>) item.get("pre_requisites");

                if (isContentExist) {
                    payloadDestination = null;
                    path = oldContentPath;
                } else {
                    uuid = UUID.randomUUID().toString();
                    // TODO: can remove uuid from destination
                    destination = identifier + "-" + uuid;
                    payloadDestination = new File(FileUtil.getContentRootDir(importContext.getDestinationFolder()), destination);
                    payloadDestination.mkdirs();

                    try {
                        copyAssets(importContext.getTmpLocation().getPath(), iconURL, payloadDestination);
                    } catch (IOException e) {
                        Logger.e(TAG, "Cannot copy asset!", e);
                    }

                    try {
                        // If compatibility level is not in range then do not copy artifact
                        if (ContentHandler.isCompatible(appContext, compatibilityLevel)) {
                            copyAssets(importContext.getTmpLocation().getPath(), artifactUrl, payloadDestination);
                            contentState = ContentConstants.State.ARTIFACT_AVAILABLE;
                        }
                    } catch (IOException e) {
                        Logger.e(TAG, "Cannot copy asset!", e);

                        contentState = ContentConstants.State.ONLY_SPINE;
                    }

                    path = payloadDestination.getPath();
                }

                // TODO: 5/18/2017 - Revisit this - handling the APK while importing ECAR.
                //launch system prompt for Install apk...
                appContext.getAPKInstaller().showInstallAPKPrompt(path, artifactUrl, preRequisites);
            } else {
                //If the content is exist then copy the old content data and add it into new content.
                if (isContentExist && !(ContentConstants.ContentStatus.DRAFT.equalsIgnoreCase(ContentHandler.readStatus(item)))) {
                    if (oldContentModel.getVisibility().equalsIgnoreCase(ContentConstants.Visibility.DEFAULT)) {
                        Map<String, Object> oldContentLocalDataMap = GsonUtil.fromJson(oldContentModel.getLocalData(), Map.class);

                        item.clear();
                        item.putAll(oldContentLocalDataMap);
                    }
                } else {
                    isContentExist = false;
                    uuid = UUID.randomUUID().toString();
                    // TODO: can remove uuid from destination
                    destination = identifier + "-" + uuid;
                    payloadDestination = new File(FileUtil.getContentRootDir(importContext.getDestinationFolder()), destination);
                    payloadDestination.mkdirs();

                    // If compatibility level is not in range then do not copy artifact
                    if (ContentHandler.isCompatible(appContext, compatibilityLevel)) {
                        boolean unzipSuccess = false;
                        if (!StringUtil.isNullOrEmpty(artifactUrl)) {
                            payload = new File(importContext.getTmpLocation().getPath(), "/" + artifactUrl);
                            unzipSuccess = Decompress.unzip(payload, payloadDestination);
                        }

                        // Add or update the content_state
                        if (unzipSuccess    // If unzip is success it means artifact is available.
                                || ContentConstants.MimeType.COLLECTION.equals(mimeType)) {

                            contentState = ContentConstants.State.ARTIFACT_AVAILABLE;
                        } else {
                            contentState = ContentConstants.State.ONLY_SPINE;
                        }
                    }

                    try {
                        copyAssets(importContext.getTmpLocation().getPath(), iconURL, payloadDestination);
                        copyAssets(importContext.getTmpLocation().getPath(), posterImage, payloadDestination);
                        copyAssets(importContext.getTmpLocation().getPath(), grayScaleAppIcon, payloadDestination);
                    } catch (IOException e) {
                        Logger.e(TAG, "Cannot copy asset!", e);
                    }
                }
            }

            //add or update the reference count for the content
            if (oldContentModel != null) {
                refCount = oldContentModel.getRefCount();

                if (!importContext.isChildContent()) {    // If import started from child content then do not update the refCount.
                    // if the content has a 'Default' visibility and update the same content then don't increase the reference count...
                    if (!(ContentConstants.Visibility.DEFAULT.equals(oldContentModel.getVisibility()) && ContentConstants.Visibility.DEFAULT.equalsIgnoreCase(visibility))) {
                        refCount = refCount + 1;
                    }
                }
            } else {
                refCount = 1;
            }

            // Set content visibility
            if ("Library".equalsIgnoreCase(ContentHandler.readObjectType(item))) {
                visibility = ContentConstants.Visibility.PARENT;
            } else if (oldContentModel != null) {
                if (importContext.isChildContent()     // If import started from child content then do not update the visibility.
                        || !ContentConstants.Visibility.PARENT.equals(oldContentModel.getVisibility())) {  // If not started from child content then do not shrink visibility.
                    visibility = oldContentModel.getVisibility();
                }
            }

            // Add or update the content_state. contentState should not update the spine_only when importing the spine content after importing content with artifacts.
            if (oldContentModel != null && oldContentModel.getContentState() > contentState) {
                contentState = oldContentModel.getContentState();
            }

            //updated the content path if the content is already exists.
            if (payloadDestination != null && !isContentExist) {
                path = payloadDestination.getPath();
            } else {
                path = oldContentPath;
            }

            ContentHandler.addOrUpdateViralityMetadata(item, appContext.getDeviceInfo().getDeviceID());
            ContentModel newContentModel = ContentModel.build(appContext.getDBSession(), identifier, importContext.getManifestVersion(), GsonUtil.toJson(item),
                    mimeType, contentType, visibility, path, refCount, contentState, audience);

            if (oldContentModel == null) {
                newContentModel.save();
            } else {
                newContentModel.update();
            }

            //Delete the content
            if (oldContentPath != null && !isContentExist) {
                if (!StringUtil.isNullOrEmpty(artifactUrl) && !artifactUrl.contains("." + ServiceConstants.FileExtension.APK)) {
                    FileUtil.rm(new File(oldContentPath));
                }
            }
            importContext.setIdentifiers(identifier);
        }


        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE, "Import failed.", TAG);
        }
    }

    @Override
    public IChainable<Void, ImportContentContext> then(IChainable<Void, ImportContentContext> link) {
        nextLink = link;
        return link;
    }

    private void copyAssets(String tempLocationPath, String asset, File payloadDestination) throws IOException {
        String folderContainingFile;

        if (asset != null && asset.length() > 0) {

            File iconSrc = new File(tempLocationPath, "/" + asset);
            File iconDestination = new File(payloadDestination, asset);

            Logger.d(TAG, "Copy: " + iconSrc + "To: " + iconDestination);

            folderContainingFile = asset.substring(0, asset.lastIndexOf("/"));
            FileUtil.createFolders(payloadDestination.getPath(), folderContainingFile);

            // If source icon is not available then copy assets is failing and throwing exception.
            FileUtil.cp(iconSrc, iconDestination);
        }
    }
}
