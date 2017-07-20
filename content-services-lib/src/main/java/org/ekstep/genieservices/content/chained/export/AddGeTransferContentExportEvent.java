package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.ExportContentContext;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.List;
import java.util.Map;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class AddGeTransferContentExportEvent implements IChainable<ContentExportResponse, ExportContentContext> {

    @Override
    public GenieResponse<ContentExportResponse> execute(AppContext appContext, ExportContentContext exportContext) {
        logGETransferEvent(exportContext);

        ContentExportResponse contentExportResponse = new ContentExportResponse();
        contentExportResponse.setExportedFilePath(exportContext.getEcarFile().toString());

        GenieResponse<ContentExportResponse> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentExportResponse);
        return response;
    }

    @Override
    public IChainable<ContentExportResponse, ExportContentContext> then(IChainable<ContentExportResponse, ExportContentContext> link) {
        return link;
    }

    private void logGETransferEvent(ExportContentContext exportContext) {
        Map<String, Object> metadata = exportContext.getMetadata();

        GETransfer.Builder geTransfer = new GETransfer.Builder();
        geTransfer.directionExport()
                .dataTypeContent()
                .count(((List) metadata.get(ServiceConstants.CONTENT_ITEMS_KEY)).size())
                .size((Long) metadata.get(ServiceConstants.FILE_SIZE));

        for (Map item : exportContext.getItems()) {
            geTransfer.addContent(ContentHandler.readOriginFromContentMap(item), ContentHandler.readIdentifier(item), ContentHandler.readPkgVersion(item), ContentHandler.readTransferCountFromContentMap(item));
        }

        TelemetryLogger.log(geTransfer.build());
    }

}
