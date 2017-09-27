package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.ImportContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class CreateContentImportManifest implements IChainable<List<ContentImportResponse>, ImportContentContext> {

    private static final String TAG = CreateContentImportManifest.class.getSimpleName();

    private IChainable<List<ContentImportResponse>, ImportContentContext> nextLink;

    @Override
    public GenieResponse<List<ContentImportResponse>> execute(AppContext appContext, ImportContentContext importContentContext) {

        //get all the content models
        List<ContentModel> contentModelList = ContentHandler.findAllContentsWithIdentifiers(appContext.getDBSession(), importContentContext.getIdentifiers());

        for (ContentModel contentModel : contentModelList) {
            List<Map<String, Object>> items = new ArrayList<>();

            createManifestForContent(appContext, contentModel, items);
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContentContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Import content failed", TAG);
        }
    }

    private void createManifestForContent(AppContext appContext, ContentModel contentModel, List<Map<String, Object>> items) {
        Map<String, Object> item;// item local data
        Map<String, Map<String, Object>> contentIndex = new HashMap<>();
        item = GsonUtil.fromJson(contentModel.getLocalData(), Map.class);

        // index item
        contentIndex.put(contentModel.getIdentifier(), item);
        ContentHandler.addViralityMetadataIfMissing(item, appContext.getDeviceInfo().getDeviceID());

        Map<String, Object> contentData = contentIndex.get(contentModel.getIdentifier());

        // get item's children only to mark children with visibility as Parent
        if (ContentHandler.hasChildren(item)) {
            List<String> childIdentifiers = new ArrayList<>();

            // store children identifiers
            childIdentifiers.addAll(ContentHandler.getChildContentsIdentifiers(item));

            if (childIdentifiers != null && childIdentifiers.size() > 0){
                List<ContentModel> childContentModelList = ContentHandler.findAllContentsWithIdentifiers(appContext.getDBSession(), childIdentifiers);

                for (ContentModel childContentModel : childContentModelList){
                    createManifestForContent(appContext, childContentModel, items);

                    if (childIdentifiers.contains(childContentModel.getIdentifier())) {
                        contentData.put(ContentHandler.KEY_VISIBILITY, ContentConstants.Visibility.PARENT);
                    }
                }
            }

            // TODO: 27/9/17 We need to check if the manifest has to be added to the pre-requisite item
            if (ContentHandler.hasPreRequisites(item)) {
                // store children identifiers
                childIdentifiers.addAll(ContentHandler.getPreRequisitesIdentifiers(item));
            }
        }

        items.add(contentData);

        //check the visibility of the content

        //if the visibility is default, then create a manifest

        //else if the visibility is parent, then iterate the child contents and create manifest for each of them


        HashMap<String, Object> archive = new HashMap<>();
        archive.put("ttl", ContentConstants.TTL);
        archive.put("count", items.size());
        archive.put("items", items);

        Map<String, Object> manifest = new HashMap<>();

        // Initialize manifest
        manifest.put("id", ContentConstants.EKSTEP_CONTENT_ARCHIVE);
        manifest.put("ver", ContentConstants.SUPPORTED_MANIFEST_VERSION);
        manifest.put("ts", DateUtil.getFormattedDateWithTimeZone(DateUtil.TIME_ZONE_GMT));
        manifest.put("archive", archive);

        try {
            FileUtil.writeManifest(new File(contentModel.getPath()), manifest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IChainable<List<ContentImportResponse>, ImportContentContext> then(IChainable<List<ContentImportResponse>, ImportContentContext> link) {
        nextLink = link;
        return link;
    }
}
