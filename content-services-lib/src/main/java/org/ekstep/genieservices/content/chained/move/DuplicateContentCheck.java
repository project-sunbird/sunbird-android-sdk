package org.ekstep.genieservices.content.chained.move;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentResponse;
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
public class DuplicateContentCheck implements IChainable<List<MoveContentResponse>, MoveContentContext> {

    private static final String TAG = DuplicateContentCheck.class.getSimpleName();

    private IChainable<List<MoveContentResponse>, MoveContentContext> nextLink;

    @Override
    public GenieResponse<List<MoveContentResponse>> execute(AppContext appContext, MoveContentContext moveContentContext) {
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
            List<MoveContentResponse> moveContentDiffPkgList = new ArrayList<>();
            List<MoveContentResponse> moveContentDupContentList = new ArrayList<>();

            for (ContentModel duplicateContentModel : duplicateContentsInSource) {
                MoveContentResponse moveContentResponse;

                //get content model from file
                Double destPkgVersion = getPkgVersionFromFile(moveContentContext, duplicateContentModel.getIdentifier());

                Double srcPkgVersion = ContentHandler.readPkgVersion(duplicateContentModel.getLocalData());

                if (destPkgVersion > srcPkgVersion) {
                    moveContentResponse = new MoveContentResponse(duplicateContentModel.getIdentifier(), MoveContentStatus.HIGHER_VERSION_IN_DESTINATION);
                    moveContentDiffPkgList.add(moveContentResponse);
                    moveContentDupContentList.add(moveContentResponse);
                } else if (destPkgVersion < srcPkgVersion) {
                    moveContentResponse = new MoveContentResponse(duplicateContentModel.getIdentifier(), MoveContentStatus.LOWER_VERSION_IN_DESTINATION);
                    moveContentDiffPkgList.add(moveContentResponse);
                    moveContentDupContentList.add(moveContentResponse);
                } else {
                    //both versions are same
                    moveContentResponse = new MoveContentResponse(duplicateContentModel.getIdentifier(), MoveContentStatus.SAME_VERSION_IN_BOTH);
                    moveContentDupContentList.add(moveContentResponse);
                }
            }

            if (moveContentDupContentList.size() > 0) {
                moveContentContext.getDuplicateContents().addAll(moveContentDupContentList);
            }

            if (existingContentAction == null && moveContentDiffPkgList.size() > 0) {
                GenieResponse<List<MoveContentResponse>> errorResponse = GenieResponseBuilder.getErrorResponse(ContentConstants.DUPLICATE_CONTENT, "Duplicate contents found", "DuplicateContentCheck");
                errorResponse.setResult(moveContentDiffPkgList);
                return errorResponse;
            }
        }

        return nextLink.execute(appContext, moveContentContext);
    }

    @Override
    public IChainable<List<MoveContentResponse>, MoveContentContext> then(IChainable<List<MoveContentResponse>, MoveContentContext> link) {
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
