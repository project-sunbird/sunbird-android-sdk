package org.ekstep.genieservices.content.chained.move;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentErrorResponse;
import org.ekstep.genieservices.commons.bean.enums.ExistingContentAction;
import org.ekstep.genieservices.commons.bean.enums.MoveContentStatus;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.content.ContentConstants;
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
public class DuplicateContentCheck implements IChainable<List<MoveContentErrorResponse>, MoveContentContext> {

    private static final String TAG = DuplicateContentCheck.class.getSimpleName();

    private IChainable<List<MoveContentErrorResponse>, MoveContentContext> nextLink;

    @Override
    public GenieResponse<List<MoveContentErrorResponse>> execute(AppContext appContext, MoveContentContext moveContentContext) {
        List<ContentModel> contentsInSource;
        if (CollectionUtil.isNullOrEmpty(moveContentContext.getContentIds())) {
            contentsInSource = ContentHandler.findAllContent(appContext.getDBSession());
        } else {
            contentsInSource = ContentHandler.findAllContentsWithIdentifiers(appContext.getDBSession(), moveContentContext.getContentIds());
        }
        moveContentContext.getContentsInSource().addAll(contentsInSource);

        List<ContentModel> duplicateContentsInSource = ContentHandler.findAllContentsWithIdentifiers(appContext.getDBSession(), moveContentContext.getValidContentIdsInDestination());

        if (!CollectionUtil.isNullOrEmpty(duplicateContentsInSource)) {
            //check if any of the action has been already set, if set then follow accordingly
            ExistingContentAction existingContentAction = moveContentContext.getExistingContentAction();
            if (existingContentAction == null) {
                List<MoveContentErrorResponse> moveContentErrorResponseList = new ArrayList<>();

                for (ContentModel duplicateContentModel : duplicateContentsInSource) {
                    MoveContentErrorResponse moveContentErrorResponse;

                    //get content model from file
                    Double destPkgVersion = getPkgVersionFromFile(moveContentContext, duplicateContentModel.getIdentifier());

                    Double srcPkgVersion = ContentHandler.readPkgVersion(duplicateContentModel.getLocalData());

                    if (destPkgVersion > srcPkgVersion) {
                        moveContentErrorResponse = new MoveContentErrorResponse(duplicateContentModel.getIdentifier(), MoveContentStatus.HIGHER_VERSION_IN_DESTINATION);
                        moveContentErrorResponseList.add(moveContentErrorResponse);
                    } else if (destPkgVersion > srcPkgVersion) {
                        moveContentErrorResponse = new MoveContentErrorResponse(duplicateContentModel.getIdentifier(), MoveContentStatus.LOWER_VERSION_IN_DESTINATION);
                        moveContentErrorResponseList.add(moveContentErrorResponse);
                    } else {
                        //both versions are same, do nothing
                    }
                }

                if (moveContentErrorResponseList.size() > 0) {
                    GenieResponse<List<MoveContentErrorResponse>> errorResponse = GenieResponseBuilder.getErrorResponse(ContentConstants.DUPLICATE_CONTENT, "Duplicate contents found", "DuplicateContentCheck");
                    errorResponse.setResult(moveContentErrorResponseList);
                    return errorResponse;
                }
            }
        }

        return nextLink.execute(appContext, moveContentContext);
    }

    @Override
    public IChainable<List<MoveContentErrorResponse>, MoveContentContext> then(IChainable<List<MoveContentErrorResponse>, MoveContentContext> link) {
        nextLink = link;
        return link;
    }

    public Double getPkgVersionFromFile(MoveContentContext moveContentContext, String identifier) {
        File storageFolder = FileUtil.getContentRootDir(moveContentContext.getDestinationFolder());
        File contentFolder = new File(storageFolder, identifier);
        Double packageVersion = 0.0;

        if (contentFolder.isDirectory()) {
            String manifestJson = FileUtil.readManifest(contentFolder);
            if (manifestJson == null) {
                return null;
            }

            LinkedTreeMap manifestMap = GsonUtil.fromJson(manifestJson, LinkedTreeMap.class);

            LinkedTreeMap archive = (LinkedTreeMap) manifestMap.get("archive");
            List<Map<String, Object>> items = null;
            if (archive.containsKey("items")) {
                items = (List<Map<String, Object>>) archive.get("items");
            }

            if (items != null && items.size() > 0) {
                for (Map<String, Object> map : items) {
                    if (ContentHandler.readIdentifier(map) != null &&
                            ContentHandler.readIdentifier(map).equalsIgnoreCase(identifier)) {
                        packageVersion = ContentHandler.readPkgVersion(map);
                    }
                }
            }
        }

        return packageVersion;
    }
}
