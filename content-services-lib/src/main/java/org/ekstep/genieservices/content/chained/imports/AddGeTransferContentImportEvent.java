package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.ImportContentContext;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class AddGeTransferContentImportEvent implements IChainable<Void, ImportContentContext> {

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContentContext importContext) {
        logGETransferEvent(importContext);

        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

    @Override
    public IChainable<Void, ImportContentContext> then(IChainable<Void, ImportContentContext> link) {
        return link;
    }

    private void logGETransferEvent(ImportContentContext importContext) {
        Map<String, Object> metadata = importContext.getMetadata();

        GETransfer.Builder geTransfer = new GETransfer.Builder();
        geTransfer.directionImport()
                .count(importContext.getItems().size())     // ((List) metadata.get(ServiceConstants.CONTENT_ITEMS_KEY)).size()
                .size((Long) metadata.get(ServiceConstants.FILE_SIZE));

        String fileType = (String) metadata.get(ServiceConstants.FILE_TYPE);
        if (StringUtil.isNullOrEmpty(fileType)) {
            geTransfer.dataTypeContent();
        } else {
            geTransfer.dataTypeExplodedContent();
        }

        for (Map item : importContext.getItems()) {
            geTransfer.addContent(ContentHandler.readOriginFromContentMap(item), ContentHandler.readIdentifier(item), ContentHandler.readPkgVersion(item), ContentHandler.readTransferCountFromContentMap(item));
        }

        TelemetryLogger.log(geTransfer.build());
    }

}
