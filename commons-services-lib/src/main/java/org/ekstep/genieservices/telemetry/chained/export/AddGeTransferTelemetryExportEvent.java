package org.ekstep.genieservices.telemetry.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferMap;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.telemetry.TelemetryLogger;
import org.ekstep.genieservices.telemetry.bean.ExportTelemetryContext;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataListModel;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class AddGeTransferTelemetryExportEvent implements IChainable<TelemetryExportResponse, ExportTelemetryContext> {

    private static final String TAG = AddGeTransferTelemetryExportEvent.class.getSimpleName();

    @Override
    public GenieResponse<TelemetryExportResponse> execute(AppContext appContext, ExportTelemetryContext exportContext) {
        try {
            int aggregateCount = 0;
            ImportedMetadataListModel importedMetadataListModel = ImportedMetadataListModel.findAll(appContext.getDBSession());

            List<ImportedMetadataModel> importedMetadataModelList;
            if (importedMetadataListModel != null) {
                importedMetadataModelList = importedMetadataListModel.getImportedMetadataModelList();
            } else {
                importedMetadataModelList = new ArrayList<>();
            }

            ArrayList<GETransferMap> contents = new ArrayList<>();
            for (ImportedMetadataModel importedMetadataModel : importedMetadataModelList) {
                aggregateCount += importedMetadataModel.getCount();
                contents.add(GETransferMap.createMapForTelemetry(importedMetadataModel.getDeviceId(),
                        importedMetadataModel.getImportedId(), importedMetadataModel.getCount()));
            }
            aggregateCount += Integer.valueOf(exportContext.getMetadata().get(ServiceConstants.EVENTS_COUNT).toString());
            GETransferEventKnowStructure eks = new GETransferEventKnowStructure(
                    GETransferEventKnowStructure.TRANSFER_DIRECTION_EXPORT,
                    GETransferEventKnowStructure.DATATYPE_TELEMETRY,
                    aggregateCount,
                    new File(exportContext.getDestinationDBFilePath()).length(),
                    contents);
            GETransfer geTransfer = new GETransfer(eks);
            TelemetryLogger.log(geTransfer);

        } catch (NumberFormatException ex) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, ex.getMessage(), TAG);
        }

        TelemetryExportResponse telemetryExportResponse = new TelemetryExportResponse();
        telemetryExportResponse.setExportedFilePath(exportContext.getDestinationDBFilePath());

        GenieResponse<TelemetryExportResponse> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(telemetryExportResponse);
        return response;
    }

    @Override
    public IChainable<TelemetryExportResponse, ExportTelemetryContext> then(IChainable<TelemetryExportResponse, ExportTelemetryContext> link) {
        return link;
    }
}
