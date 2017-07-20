package org.ekstep.genieservices.telemetry.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.contract.MetaEntry;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.importexport.bean.ExportTelemetryContext;
import org.ekstep.genieservices.importexport.db.model.MetadataModel;
import org.ekstep.genieservices.telemetry.model.ProcessedEventsModel;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class CreateMetadata implements IChainable<TelemetryExportResponse, ExportTelemetryContext> {

    private static final String TAG = CreateMetadata.class.getSimpleName();
    private IChainable<TelemetryExportResponse, ExportTelemetryContext> nextLink;

    @Override
    public GenieResponse<TelemetryExportResponse> execute(AppContext appContext, ExportTelemetryContext exportContext) {
        IDBSession destinationDBSession = exportContext.getDataSource().getReadWriteDataSource(exportContext.getDestinationDBFilePath());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ServiceConstants.VERSION, String.valueOf(exportContext.getSourceDBVersion()));
        metadata.put(ServiceConstants.EXPORT_TYPES, GsonUtil.toJson(Collections.singletonList(ServiceConstants.EXPORT_TYPE_TELEMETRY)));
        metadata.put(ServiceConstants.DID, appContext.getDeviceInfo().getDeviceID());
        metadata.put(ServiceConstants.EXPORT_ID, UUID.randomUUID().toString());
        metadata.put(ServiceConstants.FILE_SIZE, new File(exportContext.getDestinationDBFilePath()).length());
        metadata.put(ServiceConstants.EVENTS_COUNT, String.valueOf(ProcessedEventsModel.count(destinationDBSession)));

        destinationDBSession.execute(MetaEntry.getCreateEntry());
        for (String key : metadata.keySet()) {
            MetadataModel metadataModel = MetadataModel.build(destinationDBSession, key, String.valueOf(metadata.get(key)));
            metadataModel.save();
        }

        exportContext.setMetadata(metadata);

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
