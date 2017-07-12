package org.ekstep.genieservices.telemetry.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.contract.MetaEntry;
import org.ekstep.genieservices.importexport.db.model.MetadataModel;
import org.ekstep.genieservices.telemetry.bean.ExportTelemetryContext;
import org.ekstep.genieservices.telemetry.model.ProcessedEventsModel;

import java.io.File;
import java.util.UUID;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class CreateMetadata implements IChainable<TelemetryExportResponse, ExportTelemetryContext> {

    private static final String TAG = CreateMetadata.class.getSimpleName();
    private IChainable<TelemetryExportResponse, ExportTelemetryContext> nextLink;

    private String destinationDBFilePath;

    public CreateMetadata(String destinationDBFilePath) {
        this.destinationDBFilePath = destinationDBFilePath;
    }

    @Override
    public GenieResponse<TelemetryExportResponse> execute(AppContext appContext, ExportTelemetryContext exportContext) {

        exportContext.getDBSession().execute(MetaEntry.getCreateEntry());

        exportContext.getMetadata().put(ServiceConstants.EXPORT_ID, UUID.randomUUID().toString());
        exportContext.getMetadata().put(GETransferEventKnowStructure.FILE_SIZE, new File(destinationDBFilePath).length());
        exportContext.getMetadata().put(ServiceConstants.EVENTS_COUNT, String.valueOf(ProcessedEventsModel.count(exportContext.getDBSession())));

        for (String key : exportContext.getMetadata().keySet()) {
            MetadataModel metadataModel = MetadataModel.build(exportContext.getDBSession(), key, String.valueOf(exportContext.getMetadata().get(key)));
            metadataModel.save();
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, exportContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export telemetry failed", TAG);
        }
    }

    @Override
    public IChainable<TelemetryExportResponse, ExportTelemetryContext> then(IChainable<TelemetryExportResponse, ExportTelemetryContext> link) {
        nextLink = link;
        return link;
    }
}
