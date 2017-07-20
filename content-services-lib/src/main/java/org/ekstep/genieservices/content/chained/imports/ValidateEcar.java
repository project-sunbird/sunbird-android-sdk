package org.ekstep.genieservices.content.chained.imports;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.ImportContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ValidateEcar implements IChainable<Void, ImportContentContext> {

    private static final String TAG = ValidateEcar.class.getSimpleName();
    private final File tmpLocation;

    private IChainable<Void, ImportContentContext> nextLink;

    public ValidateEcar(File tmpLocation) {
        this.tmpLocation = tmpLocation;
    }

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContentContext importContext) {
        String manifestJson = FileUtil.readManifest(tmpLocation);
        if (manifestJson == null) {
            return getErrorResponse(importContext, ContentConstants.NO_CONTENT_TO_IMPORT, "Empty ecar, cannot import!");
        }

        LinkedTreeMap map = GsonUtil.fromJson(manifestJson, LinkedTreeMap.class);

        String manifestVersion = (String) map.get("ver");
        if (manifestVersion.equals("1.0")) {
            return getErrorResponse(importContext, ContentConstants.UNSUPPORTED_MANIFEST, "Cannot import outdated ECAR!");
        }

        LinkedTreeMap archive = (LinkedTreeMap) map.get("archive");
        List<Map<String, Object>> items = null;
        if (archive.containsKey("items")) {
            items = (List<Map<String, Object>>) archive.get("items");
        }

        if (items == null || items.isEmpty()) {
            return getErrorResponse(importContext, ContentConstants.NO_CONTENT_TO_IMPORT, "Empty ecar, cannot import!");
        }

        importContext.setManifestVersion(manifestVersion);
        importContext.setItems(items);
        // importContext.getMetadata().put(ServiceConstants.CONTENT_ITEMS_KEY, items);
        Logger.d(TAG, items.toString());

        for (Map<String, Object> item : items) {
            String identifier = ContentHandler.readIdentifier(item);
            boolean isDraftContent = ContentHandler.isDraftContent(ContentHandler.readStatus(item));

            //Draft content expiry .To prevent import of draft content if the expires date is lesser than from the current date.
            if (isDraftContent && ContentHandler.isExpired(ContentHandler.readExpiryDate(item))) {
                //Skip the content
                importContext.getSkippedItemsIdentifier().add(identifier);
//  TODO:              return getErrorResponse(importContext, ContentConstants.DRAFT_ECAR_FILE_EXPIRED, "The ECAR file is expired!!!");
                continue;
            }

            ContentModel oldContentModel = ContentModel.find(appContext.getDBSession(), identifier);
            String oldContentPath = oldContentModel == null ? null : oldContentModel.getPath();
            ContentModel newContentModel = ContentHandler.convertContentMapToModel(appContext.getDBSession(), item, manifestVersion);

            // To check whether the file is already imported or not
            if (ContentConstants.Visibility.DEFAULT.equals(newContentModel.getVisibility()) // Check if visibility is default for new content.
                    && isDuplicateCheckRequired(isDraftContent, ContentHandler.readPkgVersion(item))     // Check if its draft and pkgVersion is 0.
                    && !StringUtil.isNullOrEmpty(oldContentPath)     // Check if path of old content is not empty.
                    && ContentHandler.isImportFileExist(oldContentModel, newContentModel)) {   // Check whether the file is already imported or not.

                //Skip the content
                importContext.getSkippedItemsIdentifier().add(identifier);
                // TODO: 5/17/2017 - Instead of creating the skippedItemIdentifier, try to remove the item from existing items list.
                // TODO: And also need to handle the case when more than one content is bundled in one ECAR (and visibility is DEFAULT for both)
                // and one of them is collection which is already exists and imported first then the import stop there only and second content is not importing.
//                items.remove(item);
//                deleteChildItemsIfAny(appContext, items, newContentModel);
                if (items.size() > 1
                        && (ContentHandler.hasChildren(newContentModel.getLocalData()) || ContentHandler.hasPreRequisites(newContentModel.getLocalData()))) {
                    return getErrorResponse(importContext, ContentConstants.IMPORT_FILE_EXIST, "The ECAR file is imported already!!!");
                }

                //file already imported
                if (importContext.getSkippedItemsIdentifier().size() == items.size()) {
                    return getErrorResponse(importContext, ContentConstants.IMPORT_FILE_EXIST, "The ECAR file is imported already!!!");
                }
            }
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return getErrorResponse(importContext, ServiceConstants.ErrorCode.IMPORT_FAILED, "Import content failed");
        }
    }

    @Override
    public IChainable<Void, ImportContentContext> then(IChainable<Void, ImportContentContext> link) {
        nextLink = link;
        return link;
    }

    /**
     * If status is DRAFT and pkgVersion == 0 then don't do the duplicate check..
     */
    private boolean isDuplicateCheckRequired(boolean isDraftContent, Double pkgVersion) {
        return !(isDraftContent && pkgVersion == 0);
    }

    private GenieResponse<Void> getErrorResponse(ImportContentContext importContext, String error, String errorMessage) {
        Logger.e(TAG, errorMessage);
        FileUtil.rm(tmpLocation);
        return GenieResponseBuilder.getErrorResponse(error, errorMessage, TAG);
    }

}
