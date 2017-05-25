package org.ekstep.genieservices.content.chained;

import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.utils.Decompress;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.bean.ImportContext;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.ContentHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ExtractPayloads implements IChainable {

    private static final String TAG = ExtractPayloads.class.getSimpleName();

    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        String identifier, artifactUrl, iconURL, posterImage, grayScaleAppIcon, uuid, destination, oldContentPath;
        Double newContentCompatibilityLevel;
        Decompress stage2;
        File payload;
        File payloadDestination = null;
        ContentModel newContentModel, oldContentModel;

        Type type = new TypeToken<List<HashMap<String, Object>>>() {
        }.getType();

        for (HashMap<String, Object> item : importContext.getItems()) {
            identifier = (String) item.get("identifier");
            newContentCompatibilityLevel = (item.get(ContentModel.KEY_COMPATIBILITY_LEVEL) != null) ? (Double) item.get(ContentModel.KEY_COMPATIBILITY_LEVEL) : ContentHandler.defaultCompatibilityLevel;
            artifactUrl = (String) item.get("artifactUrl");
            iconURL = (String) item.get("appIcon");

            // TODO: can we remove posterImage
            posterImage = (String) item.get("posterImage");

            // TODO: can we remove grayScaleAppIcon
            grayScaleAppIcon = (String) item.get("grayScaleAppIcon");

            String objectType = (String) item.get("objectType");
            String mimeType = (String) item.get("mimeType");

            String preRequisitesString = GsonUtil.toJson(item.get("pre_requisites"));
            List<HashMap> preRequisites = GsonUtil.getGson().fromJson(preRequisitesString, type);

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
            newContentModel = ContentModel.build(appContext.getDBSession(), item, importContext.getManifestVersion());
            boolean isContentExist = ContentHandler.isContentExist(oldContentModel, newContentModel);

            //Apk files
            if ((!StringUtil.isNullOrEmpty(mimeType) && mimeType.equalsIgnoreCase(ContentConstants.MimeType.APPLICATION)) ||
                    (!StringUtil.isNullOrEmpty(artifactUrl) && artifactUrl.contains("." + ServiceConstants.FileExtension.APK))) {

                String contentPath;
                if (isContentExist) {
                    payloadDestination = null;
                    contentPath = oldContentPath;
                } else {
                    uuid = UUID.randomUUID().toString();
                    // TODO: can remove uuid from destination
                    destination = identifier + "-" + uuid;
                    payloadDestination = new File(FileUtil.getContentRootDir(appContext.getPrimaryFilesDir()), destination);
                    payloadDestination.mkdirs();

                    try {
                        copyAssets(importContext.getTmpLocation().getPath(), iconURL, payloadDestination);
                    } catch (IOException e) {
                        Logger.e(TAG, "Cannot copy asset!", e);
                    }

                    try {
                        // If compatibility level is not in range then do not copy artifact
                        if (ContentHandler.isCompatible(newContentCompatibilityLevel)) {
                            copyAssets(importContext.getTmpLocation().getPath(), artifactUrl, payloadDestination);

                            newContentModel.addOrUpdateContentState(ContentConstants.State.ARTIFACT_AVAILABLE);
                        } else {
                            newContentModel.addOrUpdateContentState(ContentConstants.State.ONLY_SPINE);
                        }
                    } catch (IOException e) {
                        Logger.e(TAG, "Cannot copy asset!", e);

                        newContentModel.addOrUpdateContentState(ContentConstants.State.ONLY_SPINE);
                        // TODO: Do we need to break the import here if it's fails here.
//                            return new Response(Constants.FAILED_RESPONSE, Constants.COPY_FAILED, Arrays.asList("Cannot copy icon!"), "");
                    }

                    contentPath = payloadDestination.getPath();
                }

                // TODO: 5/18/2017 - Revisit this - handling the APK while importing ECAR.
                //launch system prompt for Install apk...
                showInstallAPKPrompt(context, contentPath, artifactUrl, preRequisites);

            } else {
                String status = (String) item.get("status");

                //If the content is exist then copy the old content data and add it into new content.
                if (isContentExist && (!StringUtil.isNullOrEmpty(status) && !(status.equalsIgnoreCase(ServiceConstants.ContentStatus.DRAFT)))) {
                    String visibility = newContentModel.getVisibility();
                    if (oldContentModel.getVisibility().equalsIgnoreCase(ContentConstants.Visibility.DEFAULT)) {
                        String oldContentLocalData = oldContentModel.getLocalData();
                        Map<String, Object> mapLocalData = GsonUtil.fromJson(oldContentLocalData, Map.class);

                        item.clear();
                        item.putAll(mapLocalData);

                        newContentModel = ContentModel.build(appContext.getDBSession(), item, importContext.getManifestVersion());
                        newContentModel.setVisibility(visibility);
                    }
                } else {
                    isContentExist = false;
                    uuid = UUID.randomUUID().toString();
                    // TODO: can remove uuid from destination
                    destination = identifier + "-" + uuid;
                    payloadDestination = new File(FileUtil.getContentRootDir(appContext.getPrimaryFilesDir()), destination);
                    payloadDestination.mkdirs();

                    // If compatibility level is not in range then do not copy artifact
                    if (ContentHandler.isCompatible(newContentCompatibilityLevel)) {
                        if (!StringUtil.isNullOrEmpty(artifactUrl)) {
                            payload = new File(importContext.getTmpLocation().getPath(), "/" + artifactUrl);
                            stage2 = new Decompress(payload, payloadDestination);
                            boolean unzipSuccess = stage2.unzip();

                            // Add or update the content_state
                            if (unzipSuccess) { // If unzip is success it means artifact is available.
                                newContentModel.addOrUpdateContentState(ContentConstants.State.ARTIFACT_AVAILABLE);
                            } else {
                                newContentModel.addOrUpdateContentState(ContentConstants.State.ONLY_SPINE);
                            }
                        } else {
                            if (ContentType.COLLECTION.getValue().equalsIgnoreCase(newContentModel.getContentType())
                                    || ContentType.TEXTBOOK.getValue().equalsIgnoreCase(newContentModel.getContentType())
                                    || ContentType.TEXTBOOK_UNIT.getValue().equalsIgnoreCase(newContentModel.getContentType())) {

                                newContentModel.addOrUpdateContentState(ContentConstants.State.ARTIFACT_AVAILABLE);
                            } else {
                                newContentModel.addOrUpdateContentState(ContentConstants.State.ONLY_SPINE);
                            }
                        }
                    } else {
                        newContentModel.addOrUpdateContentState(ContentConstants.State.ONLY_SPINE);
                    }

                    try {
                        copyAssets(importContext.getTmpLocation().getPath(), iconURL, payloadDestination);
                        copyAssets(importContext.getTmpLocation().getPath(), posterImage, payloadDestination);
                        copyAssets(importContext.getTmpLocation().getPath(), grayScaleAppIcon, payloadDestination);
                    } catch (IOException e) {
                        Logger.e(TAG, "Cannot copy asset!", e);
                        // TODO: Can we remove this, if any content folder is not available than complete import is failing
//                            return new Response(Constants.FAILED_RESPONSE, Constants.COPY_FAILED, Arrays.asList("Cannot copy icon!"), "");
                    }
                }
            }

            // Set content visibility
            if (!StringUtil.isNullOrEmpty(objectType) && objectType.equalsIgnoreCase("Library")) {
                newContentModel.setVisibility(ContentConstants.Visibility.PARENT);
            } else if (oldContentModel != null) {
                if (importContext.isChildContent()     // If import started from child content then do not update the visibility.
                        || !ContentConstants.Visibility.PARENT.equals(oldContentModel.getVisibility())) {  // If not started from child content then do not shrink visibility.
                    newContentModel.setVisibility(oldContentModel.getVisibility());
                }
            }

            //add or update the reference count for the content
            if (oldContentModel != null) {
                int refCount = oldContentModel.getRefCount();

                if (!importContext.isChildContent()) {    // If import started from child content then do not update the refCount.
                    // if the content has a 'Default' visibility and update the same content then don't increase the reference count...
                    if (!(ContentConstants.Visibility.DEFAULT.equals(oldContentModel.getVisibility()) && ContentConstants.Visibility.DEFAULT.equalsIgnoreCase(newContentModel.getVisibility()))) {
                        refCount = refCount + 1;
                    }
                }

                newContentModel.addOrUpdateRefCount(refCount);
            } else {
                newContentModel.addOrUpdateRefCount(1);
            }

            // Add or update the content_state. contentState should not update the spine_only when importing the spine content after importing content with artifacts.
            if (oldContentModel != null && oldContentModel.getContentState() > newContentModel.getContentState()) {
                newContentModel.addOrUpdateContentState(oldContentModel.getContentState());
            }

            //updated the content path if the content is already exists.
            if (payloadDestination != null && !isContentExist) {
                newContentModel.setPath(payloadDestination.getPath());
            } else {
                newContentModel.setPath(oldContentPath);
            }

            // TODO: 5/18/2017
            ContentHandler.addOrUpdateViralityMetadata(newContentModel.getLocalData(), appContext.getDeviceInfo().getDeviceID());
//            newContentModel.addOrUpdateViralityMetadata(appContext.getDeviceInfo().getDeviceID());

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
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return breakChain();
        }
    }

    @Override
    public Void postExecute() {
        return null;
    }

    @Override
    public GenieResponse<Void> breakChain() {
        return null;
    }

    @Override
    public IChainable then(IChainable link) {
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
