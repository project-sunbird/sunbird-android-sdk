package org.ekstep.genieservices.content.chained;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileHandler;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.bean.ImportContext;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.db.model.ContentsModel;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ValidateEcar implements IChainable {

    private static final String TAG = ValidateEcar.class.getSimpleName();

    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        String json = FileHandler.readManifest(importContext.getTmpLocation());
        if (json == null) {
            return getErrorResponse(importContext, ContentConstants.NO_CONTENT_TO_IMPORT, "Empty ecar, cannot import!");
        }

        LinkedTreeMap map = GsonUtil.fromJson(json, LinkedTreeMap.class);
        String manifestVersion = (String) map.get("ver");
        LinkedTreeMap archive = (LinkedTreeMap) map.get("archive");
        String itemsString = GsonUtil.toJson(archive.get("items"));

        if (manifestVersion.equals("1.0")) {
            return getErrorResponse(importContext, ContentConstants.UNSUPPORTED_MANIFEST, "Cannot import outdated ECAR!");
        }

        if (StringUtil.isNullOrEmpty(itemsString)) {
            return getErrorResponse(importContext, ContentConstants.NO_CONTENT_TO_IMPORT, "Empty ecar, cannot import!");
        }

        Type type = new TypeToken<List<HashMap<String, Object>>>() {
        }.getType();
        List<HashMap<String, Object>> items = GsonUtil.getGson().fromJson(itemsString, type);
        if (items.isEmpty()) {
            return getErrorResponse(importContext, ContentConstants.NO_CONTENT_TO_IMPORT, "Empty ecar, cannot import!");
        }

        for (HashMap<String, Object> item : items) {
            String identifier = (String) item.get(ContentModel.KEY_IDENTIFIER);
            ContentModel oldContentModel = ContentModel.find(appContext.getDBSession(), identifier);
            String oldContentPath = oldContentModel == null ? null : oldContentModel.getPath();
            ContentModel newContentModel = ContentModel.build(appContext.getDBSession(), item, manifestVersion);

            //Draft content expiry .To prevent import of draft content if the expires-date is expired from the current date
            String expiryDate = (String) item.get("expires");
            String status = (String) item.get("status");
            Double pkgVersion = (Double) item.get(ContentModel.KEY_PKG_VERSION);
            if (!StringUtil.isNullOrEmpty(expiryDate) && (!StringUtil.isNullOrEmpty(status) && status.equalsIgnoreCase(ServiceConstants.ContentStatus.DRAFT))) {
                long millis = -1;
                try {
                    millis = DateUtil.convertLocalTimeMillis(expiryDate);
                } catch (ParseException e) {
                    Logger.e(TAG, "Error in parsing expiry date.");
                }
                if (millis > 0 && System.currentTimeMillis() > millis) {
                    Logger.e(TAG, "The ECAR file is expired!!!");
                    FileHandler.rm(importContext.getTmpLocation());
                    return getErrorResponse(importContext, ContentConstants.DRAFT_ECAR_FILE_EXPIRED, "The ECAR file is expired!!!");
                }
            }

            // To check whether the file is already imported or not
            if (ContentConstants.Visibility.DEFAULT.equals(newContentModel.getVisibility()) // Check if visibility is default for new content. // TODO: 5/17/2017 - Is this check really needed?
                    && isDuplicateCheckRequired(pkgVersion, status)     // Check if its draft and pkgVersion is 0.
                    && !StringUtil.isNullOrEmpty(oldContentPath)     // Check if path of old content is not empty.
                    && isImportFileExist(oldContentModel, newContentModel)) {   // Check whether the file is already imported or not.

                //Skip the content
                importContext.getSkippedItemsIdentifier().add(identifier);
                // TODO: 5/17/2017
                deleteChildItemsIfAny(items, newContentModel);
//                if (items.size() > 1
//                        && (newContentModel.hasChildren() || newContentModel.hasPreRequisites())) {
//                    return getErrorResponse(importContext, ContentConstants.IMPORT_FILE_EXIST, "The ECAR file is imported already!!!");
//                }

                //file already imported
                if (importContext.getSkippedItemsIdentifier().size() == items.size()) {
                    return getErrorResponse(importContext, ContentConstants.IMPORT_FILE_EXIST, "The ECAR file is imported already!!!");
                }
            }
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            FileHandler.rm(importContext.getTmpLocation());
            return breakChain();
        }
    }

    @Override
    public Void postExecute() {
        return null;
    }

    @Override
    public GenieResponse<Void> breakChain() {
        Logger.e(TAG, "Import content failed");
        return GenieResponseBuilder.getErrorResponse(ContentConstants.IMPORT_FAILED, "Import content failed", TAG);
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }

    private boolean isDuplicateCheckRequired(Double pkgVersion, String status) {
        //if status is DRAFT and pkgVersion == 0 then don't do the duplicate check..
        return !(!StringUtil.isNullOrEmpty(status) && status.equalsIgnoreCase(ServiceConstants.ContentStatus.DRAFT) && pkgVersion == 0);
    }

    private boolean isImportFileExist(ContentModel oldContentModel, ContentModel newContentModel) {
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

    private GenieResponse<Void> getErrorResponse(ImportContext importContext, String error, String errorMessage) {
        Logger.e(TAG, errorMessage);
        FileHandler.rm(importContext.getTmpLocation());
        return GenieResponseBuilder.getErrorResponse(error, errorMessage, TAG);
    }

    private void deleteChildItemsIfAny(List<HashMap<String, Object>> items, ContentModel contentModel) {
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

}
