package org.ekstep.genieservices.telemetry.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.contract.MetaEntry;
import org.ekstep.genieservices.importexport.db.model.MetadataModel;
import org.ekstep.genieservices.telemetry.model.ProcessedEventsModel;

import java.io.File;
import java.util.UUID;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class CreateMetadata implements IChainable<TelemetryExportResponse> {

    private static final String TAG = CreateMetadata.class.getSimpleName();
    private IChainable<TelemetryExportResponse> nextLink;

    private String destinationDBFilePath;

    public CreateMetadata(String destinationDBFilePath) {
        this.destinationDBFilePath = destinationDBFilePath;
    }

    @Override
    public GenieResponse<TelemetryExportResponse> execute(AppContext appContext, ImportContext importContext) {

        importContext.getDBSession().execute(MetaEntry.getCreateEntry());

        importContext.getMetadata().put(ServiceConstants.EXPORT_ID, UUID.randomUUID().toString());
        importContext.getMetadata().put(GETransferEventKnowStructure.FILE_SIZE, new File(destinationDBFilePath).length());
        importContext.getMetadata().put(ServiceConstants.EVENTS_COUNT, String.valueOf(ProcessedEventsModel.count(importContext.getDBSession())));

        for (String key : importContext.getMetadata().keySet()) {
            MetadataModel metadataModel = MetadataModel.build(importContext.getDBSession(), key, String.valueOf(importContext.getMetadata().get(key)));
            metadataModel.save();
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export telemetry failed", TAG);
        }
    }

    @Override
    public IChainable<TelemetryExportResponse> then(IChainable<TelemetryExportResponse> link) {
        nextLink = link;
        return link;
    }
}
