package org.ekstep.genieservices.telemetry.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.Share;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.importexport.bean.ImportTelemetryContext;
import org.ekstep.genieservices.telemetry.TelemetryLogger;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataListModel;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

import java.util.List;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class AddGeTransferTelemetryImportEvent implements IChainable<Void, ImportTelemetryContext> {

    private static final String TAG = AddGeTransferTelemetryImportEvent.class.getSimpleName();

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportTelemetryContext importContext) {

        try {
            ImportedMetadataListModel importedMetadataListModel = ImportedMetadataListModel.findAll(appContext.getDBSession());

            if (importedMetadataListModel != null) {
                logGETransferEvent(importContext, importedMetadataListModel.getImportedMetadataModelList());

                return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            }
        } catch (NumberFormatException ex) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, ServiceConstants.ErrorMessage.IMPORT_TELEMETRY_FAILED, TAG);
        }

        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, ServiceConstants.ErrorMessage.IMPORT_TELEMETRY_FAILED, TAG);
    }

    @Override
    public IChainable<Void, ImportTelemetryContext> then(IChainable<Void, ImportTelemetryContext> link) {
        return null;
    }

    private void logGETransferEvent(ImportTelemetryContext importContext, List<ImportedMetadataModel> importedMetadataModelList) {

        Share.Builder share = new Share.Builder();
        share.directionImport().dataTypeFile();
        share.environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT);
        for (ImportedMetadataModel importedMetadataModel : importedMetadataModelList) {
            share.addItem(share.itemTypeTelemetry(), importedMetadataModel.getDeviceId(), importedMetadataModel.getImportedId(),
                    0.0, 0, "");
        }

        TelemetryLogger.log(share.build());
    }
}
