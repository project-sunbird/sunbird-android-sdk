package org.ekstep.genieservices.content.chained.imports;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.enums.ContentImportStatus;
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
public class ValidateEcar implements IChainable<List<ContentImportResponse>, ImportContentContext> {

    private static final String TAG = ValidateEcar.class.getSimpleName();
    private final File tmpLocation;

    private IChainable<List<ContentImportResponse>, ImportContentContext> nextLink;

    public ValidateEcar(File tmpLocation) {
        this.tmpLocation = tmpLocation;
    }

    @Override
    public GenieResponse<List<ContentImportResponse>> execute(AppContext appContext, ImportContentContext importContext) {
        String manifestJson = FileUtil.readManifest(tmpLocation);
        if (manifestJson == null) {
            return getErrorResponse(ContentConstants.NO_CONTENT_TO_IMPORT, "Empty ecar, cannot import!");
        }

        LinkedTreeMap map = GsonUtil.fromJson(manifestJson, LinkedTreeMap.class);

        String manifestVersion = (String) map.get("ver");
        if (manifestVersion.equals("1.0")) {
            return getErrorResponse(ContentConstants.UNSUPPORTED_MANIFEST, "Cannot import outdated ECAR!");
        }

        LinkedTreeMap archive = (LinkedTreeMap) map.get("archive");
        List<Map<String, Object>> items = null;
        if (archive.containsKey("items")) {
            items = (List<Map<String, Object>>) archive.get("items");
        }

        if (items == null || items.isEmpty()) {
            return getErrorResponse(ContentConstants.NO_CONTENT_TO_IMPORT, "Empty ecar, cannot import!");
        }

        importContext.setManifestVersion(manifestVersion);
        importContext.setItems(items);
//        importContext.getMetadata().put(ServiceConstants.CONTENT_ITEMS_COUNT_KEY, items.size());
        Logger.d(TAG, items.toString());

        for (Map<String, Object> item : items) {
            String identifier = ContentHandler.readIdentifier(item);
            String visibility = ContentHandler.readVisibility(item);
            boolean isDraftContent = ContentHandler.isDraftContent(ContentHandler.readStatus(item));

            // If compatibility level is not in range then do not copy artifact
            if (ContentConstants.Visibility.DEFAULT.equals(visibility)
                    && !ContentHandler.isCompatible(appContext, ContentHandler.readCompatibilityLevel(item))) {
                skipContent(importContext, identifier, visibility, ContentImportStatus.NOT_COMPATIBLE);
                continue;
            }

            //Draft content expiry .To prevent import of draft content if the expires date is lesser than from the current date.
            if (isDraftContent && ContentHandler.isExpired(ContentHandler.readExpiryDate(item))) {
                skipContent(importContext, identifier, visibility, ContentImportStatus.CONTENT_EXPIRED);
                continue;
            }

            ContentModel oldContentModel = ContentModel.find(appContext.getDBSession(), identifier);
            String oldContentPath = oldContentModel == null ? null : oldContentModel.getPath();

            // To check whether the file is already imported or not
            if (!StringUtil.isNullOrEmpty(oldContentPath)     // Check if path of old content is not empty.
//                    && ContentConstants.Visibility.DEFAULT.equals(visibility) // Check if visibility is default for new content.
                    && isDuplicateCheckRequired(isDraftContent, ContentHandler.readPkgVersion(item))     // Check if its draft and pkgVersion is 0.
                    && ContentHandler.isImportFileExist(oldContentModel, item)) {   // Check whether the file is already imported or not.

                skipContent(importContext, identifier, visibility, ContentImportStatus.ALREADY_EXIST);
            }
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import content failed");
        }
    }

    @Override
    public IChainable<List<ContentImportResponse>, ImportContentContext> then(IChainable<List<ContentImportResponse>, ImportContentContext> link) {
        nextLink = link;
        return link;
    }

    /**
     * Skip the content.
     */
    private void skipContent(ImportContentContext importContext, String identifier, String visibility, ContentImportStatus contentImportStatus) {
        if (ContentConstants.Visibility.DEFAULT.equals(visibility)) {
            importContext.getContentImportResponseList().add(new ContentImportResponse(identifier, contentImportStatus));
        }
        importContext.getSkippedItemsIdentifier().add(identifier);
//        items.remove(item);
    }

    /**
     * If status is DRAFT and pkgVersion == 0 then don't do the duplicate check..
     */
    private boolean isDuplicateCheckRequired(boolean isDraftContent, Double pkgVersion) {
        return !(isDraftContent && pkgVersion == 0);
    }

    private GenieResponse<List<ContentImportResponse>> getErrorResponse(String error, String errorMessage) {
        Logger.e(TAG, errorMessage);
        FileUtil.rm(tmpLocation);
        return GenieResponseBuilder.getErrorResponse(error, errorMessage, TAG);
    }

}
