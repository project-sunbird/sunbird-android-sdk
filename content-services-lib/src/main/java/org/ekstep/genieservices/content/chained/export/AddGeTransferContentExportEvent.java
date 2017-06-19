package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferMap;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class AddGeTransferContentExportEvent implements IChainable {

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        Map<String, Object> metadata = importContext.getMetadata();

        GETransferEventKnowStructure eks = new GETransferEventKnowStructure(
                GETransferEventKnowStructure.TRANSFER_DIRECTION_EXPORT,
                GETransferEventKnowStructure.DATATYPE_CONTENT,
                ((List) metadata.get(GETransferEventKnowStructure.CONTENT_ITEMS_KEY)).size(),
                (Long) metadata.get(GETransferEventKnowStructure.FILE_SIZE),
                buildContentsMetadata(importContext.getItems()));
        GETransfer geTransfer = new GETransfer(new GameData(appContext.getParams().getGid(), appContext.getParams().getVersionName()), eks);
        TelemetryLogger.log(geTransfer);
        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

    }

    @Override
    public IChainable then(IChainable link) {
        return link;
    }

    private List<GETransferMap> buildContentsMetadata(List<Map<String, Object>> items) {
        List<GETransferMap> contentsMetadata = new ArrayList<>();

        for (Map item : items) {
            contentsMetadata.add(GETransferMap.createMapForContent(
                    ContentHandler.readIdentifier(item),
                    ContentHandler.readPkgVersion(item),
                    ContentHandler.readTransferCountFromContentMap(item),
                    ContentHandler.readOriginFromContentMap(item)));
        }
        return contentsMetadata;
    }
}
