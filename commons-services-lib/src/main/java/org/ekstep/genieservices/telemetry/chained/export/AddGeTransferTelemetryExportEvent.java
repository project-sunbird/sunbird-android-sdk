package org.ekstep.genieservices.telemetry.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.bean.telemetry.Share;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.importexport.bean.ExportTelemetryContext;
import org.ekstep.genieservices.telemetry.TelemetryLogger;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataListModel;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

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
            ImportedMetadataListModel importedMetadataListModel = ImportedMetadataListModel.findAll(appContext.getDBSession());

            List<ImportedMetadataModel> importedMetadataModelList;
            if (importedMetadataListModel != null) {
                importedMetadataModelList = importedMetadataListModel.getImportedMetadataModelList();
            } else {
                importedMetadataModelList = new ArrayList<>();
            }

            logGETransferEvent(exportContext, importedMetadataModelList);

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

    private void logGETransferEvent(ExportTelemetryContext exportContext, List<ImportedMetadataModel> importedMetadataModelList) {

        Share.Builder share = new Share.Builder();
        share.directionExport().dataTypeFile();
        share.environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT);
        for (ImportedMetadataModel importedMetadataModel : importedMetadataModelList) {
            share.addItem(share.itemTypeTelemetry(), importedMetadataModel.getDeviceId(), importedMetadataModel.getImportedId(),
                    0.0, 0, "");
        }

        TelemetryLogger.log(share.build());
    }
}
