package org.ekstep.genieservices.profile.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.importexport.bean.ExportProfileContext;
import org.ekstep.genieservices.telemetry.TelemetryLogger;
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
public class AddGeTransferProfileExportEvent implements IChainable<ProfileExportResponse, ExportProfileContext> {

    private static final String TAG = AddGeTransferProfileExportEvent.class.getSimpleName();

    @Override
    public GenieResponse<ProfileExportResponse> execute(AppContext appContext, ExportProfileContext exportContext) {
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

        ProfileExportResponse profileExportResponse = new ProfileExportResponse();
        profileExportResponse.setExportedFilePath(exportContext.getDestinationDBFilePath());

        GenieResponse<ProfileExportResponse> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(profileExportResponse);
        return response;
    }

    @Override
    public IChainable<ProfileExportResponse, ExportProfileContext> then(IChainable<ProfileExportResponse, ExportProfileContext> link) {
        return link;
    }

    private void logGETransferEvent(ExportProfileContext exportContext, List<ImportedMetadataModel> importedMetadataModelList) {
        int aggregateCount = 0;
        GETransfer.Builder geTransfer = new GETransfer.Builder();
        geTransfer.directionExport()
                .dataTypeProfile()
                .size(new File(exportContext.getDestinationDBFilePath()).length());

        for (ImportedMetadataModel importedMetadataModel : importedMetadataModelList) {
            aggregateCount += importedMetadataModel.getCount();

            geTransfer.addContent(importedMetadataModel.getDeviceId(), importedMetadataModel.getImportedId(), importedMetadataModel.getCount());
        }
        aggregateCount += Integer.valueOf(exportContext.getMetadata().get(ServiceConstants.PROFILES_COUNT).toString());

        geTransfer.count(aggregateCount);

        TelemetryLogger.log(geTransfer.build());
    }
}
