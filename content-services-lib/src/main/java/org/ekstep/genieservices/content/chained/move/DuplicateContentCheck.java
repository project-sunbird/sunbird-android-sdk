package org.ekstep.genieservices.content.chained.move;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.MoveContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 9/25/2017.
 *
 * @author anil
 */
public class DuplicateContentCheck implements IChainable<Void, MoveContentContext> {

    private static final String TAG = DuplicateContentCheck.class.getSimpleName();

    private IChainable<Void, MoveContentContext> nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, MoveContentContext moveContentContext) {
        List<ContentModel> contentsInSource;
        if (CollectionUtil.isNullOrEmpty(moveContentContext.getContentIds())) {
            contentsInSource = ContentHandler.findAllContent(appContext.getDBSession());
        } else {
            contentsInSource = ContentHandler.findAllContentsWithIdentifiers(appContext.getDBSession(), moveContentContext.getContentIds());
        }
        moveContentContext.getContentsInSource().addAll(contentsInSource);

        List<ContentModel> contentsInDestination = new ArrayList<>();
        for (String identifier : moveContentContext.getValidContentIdsInDestination()) {
            File destFile = new File(moveContentContext.getDestinationFolder(), identifier);
            String manifestJson = FileUtil.readManifest(destFile);
            LinkedTreeMap manifestMap = GsonUtil.fromJson(manifestJson, LinkedTreeMap.class);
            LinkedTreeMap archive = (LinkedTreeMap) manifestMap.get("archive");
            List<Map<String, Object>> items = (List<Map<String, Object>>) archive.get("items");
            createContentModelForDestinationContent(items);
        }

        List<ContentModel> duplicateContentsInSource = ContentHandler.findAllContentsWithIdentifiers(appContext.getDBSession(), moveContentContext.getValidContentIdsInDestination());
        if (!CollectionUtil.isNullOrEmpty(duplicateContentsInSource)) {

        }

        return nextLink.execute(appContext, moveContentContext);
    }

    @Override
    public IChainable<Void, MoveContentContext> then(IChainable<Void, MoveContentContext> link) {
        nextLink = link;
        return link;
    }

    private void createContentModelForDestinationContent(List<Map<String, Object>> items) {
//        File destinationFolder = new File(importContext.getDestinationFolder());
//        String identifier, mimeType, contentType, visibility, audience, path, contentEncoding, contentDisposition;
//        Double compatibilityLevel, pkgVersion;
//        int refCount;
//        int contentState = ContentConstants.State.ONLY_SPINE;
//        String oldContentPath;
//        String artifactUrl, iconURL, posterImage, grayScaleAppIcon;
//        File payload;
//        File payloadDestination = null;
//        ContentModel oldContentModel;
//
//        for (Map<String, Object> item : items) {
//            identifier = ContentHandler.readIdentifier(item);
//
//            //skip the content if already imported on the same version
//            if (!CollectionUtil.isNullOrEmpty(importContext.getSkippedItemsIdentifier())
//                    && importContext.getSkippedItemsIdentifier().contains(identifier)) {
//                continue;
//            }
//
//            mimeType = ContentHandler.readMimeType(item);
//            contentEncoding = ContentHandler.readContentEncoding(item);
//            contentDisposition = ContentHandler.readContentDisposition(item);
//            contentType = ContentHandler.readContentType(item);
//            visibility = ContentHandler.readVisibility(item);
//            audience = ContentHandler.readAudience(item);
//            compatibilityLevel = ContentHandler.readCompatibilityLevel(item);
//            pkgVersion = ContentHandler.readPkgVersion(item);
//            artifactUrl = ContentHandler.readArtifactUrl(item);
//            iconURL = ContentHandler.readAppIcon(item);
//            posterImage = ContentHandler.readPosterImage(item);
//            grayScaleAppIcon = ContentHandler.readGrayScaleAppIcon(item);
//
//            oldContentModel = ContentModel.find(appContext.getDBSession(), identifier);
//            oldContentPath = oldContentModel == null ? null : oldContentModel.getPath();
//            boolean isContentExist = ContentHandler.isContentExist(oldContentModel, identifier, pkgVersion);
//
//            //Apk files
//            if ((!StringUtil.isNullOrEmpty(mimeType) && mimeType.equalsIgnoreCase(ContentConstants.MimeType.APK)) ||
//                    (!StringUtil.isNullOrEmpty(artifactUrl) && artifactUrl.contains("." + ServiceConstants.FileExtension.APK))) {
//
//                if (isContentExist) {
//                    payloadDestination = null;
//                    path = oldContentPath;
//                } else {
//                    payloadDestination = new File(FileUtil.getContentRootDir(destinationFolder), identifier);
//                    payloadDestination.mkdirs();
//
//                    try {
//                        copyAssets(tmpLocation.getPath(), iconURL, payloadDestination);
//                    } catch (IOException e) {
//                        Logger.e(TAG, "Cannot copy asset!", e);
//                    }
//
//                    try {
//                        // If compatibility level is not in range then do not copy artifact
//                        if (ContentHandler.isCompatible(appContext, compatibilityLevel)) {
//                            copyAssets(tmpLocation.getPath(), artifactUrl, payloadDestination);
//                            contentState = ContentConstants.State.ARTIFACT_AVAILABLE;
//                        }
//                    } catch (IOException e) {
//                        Logger.e(TAG, "Cannot copy asset!", e);
//
//                        contentState = ContentConstants.State.ONLY_SPINE;
//                    }
//
//                    path = payloadDestination.getPath();
//                }
//
//                try {
//                    //launch system prompt for Install apk...
//                    File apkFile = new File(path, artifactUrl);
//                    appContext.getAPKInstaller().installApk(apkFile.getAbsolutePath());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                //If the content is exist then copy the old content data and add it into new content.
//                if (isContentExist && !(ContentConstants.ContentStatus.DRAFT.equalsIgnoreCase(ContentHandler.readStatus(item)))) {
//                    if (oldContentModel.getVisibility().equalsIgnoreCase(ContentConstants.Visibility.DEFAULT)) {
//                        Map<String, Object> oldContentLocalDataMap = GsonUtil.fromJson(oldContentModel.getLocalData(), Map.class);
//
//                        item.clear();
//                        item.putAll(oldContentLocalDataMap);
//                    }
//                } else {
//                    isContentExist = false;
//                    payloadDestination = new File(FileUtil.getContentRootDir(destinationFolder), identifier);
//                    payloadDestination.mkdirs();
//
//                    // If compatibility level is not in range then do not copy artifact
//                    if (ContentHandler.isCompatible(appContext, compatibilityLevel)) {
//                        boolean unzipSuccess = false;
//                        if (!StringUtil.isNullOrEmpty(artifactUrl)) {
//                            if (StringUtil.isNullOrEmpty(contentDisposition) || StringUtil.isNullOrEmpty(contentEncoding)
//                                    || (ContentConstants.ContentDisposition.INLINE.equals(contentDisposition) && ContentConstants.ContentEncoding.GZIP.equals(contentEncoding))) { // Content with zip artifact
//                                payload = new File(tmpLocation.getPath(), "/" + artifactUrl);
//                                unzipSuccess = Decompress.unzip(payload, payloadDestination);
//                            } else if (ContentConstants.ContentDisposition.INLINE.equals(contentDisposition) && ContentConstants.ContentEncoding.IDENTITY.equals(contentEncoding)) {    // Content with artifact without zip i.e. pfd, mp4
//                                try {
//                                    copyAssets(tmpLocation.getPath(), artifactUrl, payloadDestination);
//                                    unzipSuccess = true;
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                    unzipSuccess = false;
//                                }
//                            } else if (ContentConstants.ContentDisposition.ONLINE.equals(contentDisposition)) { // Content with no artifact
//                                unzipSuccess = true;
//                            }
//                        }
//
//                        // Add or update the content_state
//                        if (unzipSuccess    // If unzip is success it means artifact is available.
//                                || ContentConstants.MimeType.COLLECTION.equals(mimeType)) {
//
//                            contentState = ContentConstants.State.ARTIFACT_AVAILABLE;
//                        } else {
//                            contentState = ContentConstants.State.ONLY_SPINE;
//                        }
//                    }
//
//                    try {
//                        copyAssets(tmpLocation.getPath(), iconURL, payloadDestination);
//                        copyAssets(tmpLocation.getPath(), posterImage, payloadDestination);
//                        copyAssets(tmpLocation.getPath(), grayScaleAppIcon, payloadDestination);
//                    } catch (IOException e) {
//                        Logger.e(TAG, "Cannot copy asset!", e);
//                    }
//                }
//            }
//
//            //add or update the reference count for the content
//            if (oldContentModel != null) {
//                refCount = oldContentModel.getRefCount();
//
//                if (!importContext.isChildContent()) {    // If import started from child content then do not update the refCount.
//                    // if the content has a 'Default' visibility and update the same content then don't increase the reference count...
//                    if (!(ContentConstants.Visibility.DEFAULT.equals(oldContentModel.getVisibility()) && ContentConstants.Visibility.DEFAULT.equalsIgnoreCase(visibility))) {
//                        refCount = refCount + 1;
//                    }
//                }
//            } else {
//                refCount = 1;
//            }
//
//            // Set content visibility
//            if ("Library".equalsIgnoreCase(ContentHandler.readObjectType(item))) {
//                visibility = ContentConstants.Visibility.PARENT;
//            } else if (oldContentModel != null) {
//                if (importContext.isChildContent()     // If import started from child content then do not update the visibility.
//                        || !ContentConstants.Visibility.PARENT.equals(oldContentModel.getVisibility())) {  // If not started from child content then do not shrink visibility.
//                    visibility = oldContentModel.getVisibility();
//                }
//            }
//
//            // Add or update the content_state. contentState should not update the spine_only when importing the spine content after importing content with artifacts.
//            if (oldContentModel != null && oldContentModel.getContentState() > contentState) {
//                contentState = oldContentModel.getContentState();
//            }
//
//            //updated the content path if the content is already exists.
//            if (payloadDestination != null && !isContentExist) {
//                path = payloadDestination.getPath();
//            } else {
//                path = oldContentPath;
//            }
//
//            long sizeOnDevice = FileUtil.getFileSize(new File(path));
//
//            ContentHandler.addOrUpdateViralityMetadata(item, appContext.getDeviceInfo().getDeviceID());
//            ContentModel newContentModel = ContentModel.build(appContext.getDBSession(), identifier, importContext.getManifestVersion(), GsonUtil.toJson(item),
//                    mimeType, contentType, visibility, path, refCount, contentState, audience, sizeOnDevice);
//
//            if (oldContentModel == null) {
//                newContentModel.save();
//            } else {
//                newContentModel.update();
//            }
//
//            //Delete the content
//            if (oldContentPath != null && payloadDestination != null && !oldContentPath.equals(payloadDestination.getPath()) && !isContentExist) {
//                if (!StringUtil.isNullOrEmpty(artifactUrl) && !artifactUrl.contains("." + ServiceConstants.FileExtension.APK)) {
//                    FileUtil.rm(new File(oldContentPath));
//                }
//            }
//            importContext.getIdentifiers().add(identifier);
//        }
    }
}
