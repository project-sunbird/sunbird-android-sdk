package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class CreateContentExportManifest implements IChainable<ContentExportResponse> {

    private static final String TAG = CreateContentExportManifest.class.getSimpleName();

    private IChainable<ContentExportResponse> nextLink;
    private List<ContentModel> contentModelsToExport;
    private List<Map<String, Object>> items;

    public CreateContentExportManifest(List<ContentModel> contentModelsToExport) {
        this.contentModelsToExport = contentModelsToExport;
        this.items = new ArrayList<>();
    }

    @Override
    public GenieResponse<ContentExportResponse> execute(AppContext appContext, ImportContext importContext) {
        Map<String, Object> item;
        Map<String, Map<String, Object>> contentIndex = new HashMap<>();
        List<String> childIdentifiers = new ArrayList<>();
        List<String> allContents = new ArrayList<>();

        for (ContentModel contentModel : contentModelsToExport) {
            // item local data
            item = GsonUtil.fromJson(contentModel.getLocalData(), Map.class);

            // index item
            contentIndex.put(contentModel.getIdentifier(), item);
            ContentHandler.addViralityMetadataIfMissing(item, appContext.getDeviceInfo().getDeviceID());

            // get item's children only to mark children with visibility as Parent
            if (ContentHandler.hasChildren(contentModel.getLocalData())) {
                // store children identifiers
                childIdentifiers.addAll(ContentHandler.getChildContentsIdentifiers(contentModel.getLocalData()));
            }
            if (ContentHandler.hasPreRequisites(contentModel.getLocalData())) {
                // store children identifiers
                childIdentifiers.addAll(ContentHandler.getPreRequisitesIdentifiers(contentModel.getLocalData()));
            }

            allContents.add(contentModel.getIdentifier());
        }

        for (String contentIdentifier : allContents) {
            Map<String, Object> contentData = contentIndex.get(contentIdentifier);
            if (childIdentifiers.contains(contentIdentifier)) {
                contentData.put(ContentHandler.KEY_VISIBILITY, ContentConstants.Visibility.PARENT);
            }
            items.add(contentData);
        }

        importContext.setItems(items);

        HashMap<String, Object> archive = new HashMap<>();
        archive.put("ttl", ContentConstants.TTL);
        archive.put("count", this.items.size());
        archive.put("items", this.items);

        // Initialize manifest
        importContext.getManifest().put("id", ContentConstants.EKSTEP_CONTENT_ARCHIVE);
        importContext.getManifest().put("ver", ContentConstants.SUPPORTED_MANIFEST_VERSION);
        importContext.getManifest().put("ts", DateUtil.getFormattedDateWithTimeZone(DateUtil.TIME_ZONE_GMT));
        importContext.getManifest().put("archive", archive);

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export content failed", TAG);
        }
    }

    @Override
    public IChainable<ContentExportResponse> then(IChainable<ContentExportResponse> link) {
        nextLink = link;
        return link;
    }
}
