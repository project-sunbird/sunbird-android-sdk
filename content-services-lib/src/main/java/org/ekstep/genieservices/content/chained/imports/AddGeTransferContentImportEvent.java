package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferMap;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.ImportContentContext;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class AddGeTransferContentImportEvent implements IChainable<Void, ImportContentContext> {

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContentContext importContext) {
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

        GETransfer geTransfer = new GETransfer(new GameData(appContext.getParams().getString(ServiceConstants.Params.GID), appContext.getParams().getString(ServiceConstants.Params.VERSION_NAME)), eks);
        TelemetryLogger.log(geTransfer);
        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

    @Override
    public IChainable<Void, ImportContentContext> then(IChainable<Void, ImportContentContext> link) {
        return link;
    }

    private List<GETransferMap> buildContentsMetadata(Map<String, Object> metadata) {
        List<Map> contents = (List<Map>) metadata.get(GETransferEventKnowStructure.CONTENT_ITEMS_KEY);
        ArrayList<GETransferMap> contentsMetadata = new ArrayList<>();
        metadata.put(GETransferEventKnowStructure.CONTENT_ITEMS_KEY, contentsMetadata);

        for (Map contentMap : contents) {
            contentsMetadata.add(GETransferMap.createMapForContent(
                    ContentHandler.readIdentifier(contentMap),
                    ContentHandler.readPkgVersion(contentMap),
                    ContentHandler.readTransferCountFromContentMap(contentMap),
                    ContentHandler.readOriginFromContentMap(contentMap)));
        }
        return contentsMetadata;
    }

}
