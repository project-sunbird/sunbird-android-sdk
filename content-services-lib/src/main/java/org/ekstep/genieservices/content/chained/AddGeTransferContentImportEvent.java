package org.ekstep.genieservices.content.chained;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferContentMap;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.ImportContext;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class AddGeTransferContentImportEvent implements IChainable {

    private static final String TAG = AddGeTransferContentImportEvent.class.getSimpleName();

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        Map<String, Object> metadata = importContext.getMetadata();
        String contentDataType = (String) metadata.get(GETransferEventKnowStructure.FILE_TYPE);
        if (StringUtil.isNullOrEmpty(contentDataType)) {
            contentDataType = GETransferEventKnowStructure.DATATYPE_CONTENT;
        } else {
            contentDataType = GETransferEventKnowStructure.DATATYPE_EXPLODED_CONTENT;
        }

        GETransferEventKnowStructure eks = new GETransferEventKnowStructure(
                GETransferEventKnowStructure.TRANSFER_DIRECTION_IMPORT,
                contentDataType,
                ((List) metadata.get(GETransferEventKnowStructure.CONTENT_ITEMS_KEY)).size(),
                (Long) metadata.get(GETransferEventKnowStructure.FILE_SIZE),
                buildContentsMetadata(metadata));

        GETransfer geTransfer = new GETransfer(new GameData(appContext.getParams().getGid(), appContext.getParams().getVersionName()), eks);
        TelemetryLogger.log(geTransfer);
        return GenieResponseBuilder.getSuccessResponse("Successfully imported");
    }

    @Override
    public IChainable then(IChainable link) {
        return link;
    }

    private List<GETransferContentMap> buildContentsMetadata(Map<String, Object> metadata) {
        List<HashMap> contents = (List<HashMap>) metadata.get(GETransferEventKnowStructure.CONTENT_ITEMS_KEY);
        ArrayList<GETransferContentMap> contentsMetadata = new ArrayList<>();
        metadata.put(GETransferEventKnowStructure.CONTENT_ITEMS_KEY, contentsMetadata);

        for (HashMap contentMap : contents) {
            contentsMetadata.add(GETransferContentMap.createMapForContent(
                    ContentHandler.readIdentifier(contentMap),
                    ContentHandler.readPkgVersion(contentMap),
                    ContentHandler.readTransferCountFromContentMap(contentMap),
                    ContentHandler.readOriginFromContentMap(contentMap)));
        }
        return contentsMetadata;
    }

}
