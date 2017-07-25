package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.LongUtil;
import org.ekstep.genieservices.importexport.bean.ImportProfileContext;
import org.ekstep.genieservices.telemetry.TelemetryLogger;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataListModel;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

import java.util.List;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class AddGeTransferProfileImportEvent implements IChainable<ProfileImportResponse, ImportProfileContext> {

    private static final String TAG = AddGeTransferProfileImportEvent.class.getSimpleName();

    @Override
    public GenieResponse<ProfileImportResponse> execute(AppContext appContext, ImportProfileContext importContext) {

        ImportedMetadataListModel importedMetadataListModel = ImportedMetadataListModel.findAll(appContext.getDBSession());
        if (importedMetadataListModel != null) {
            logGETransferEvent(importContext, importedMetadataListModel.getImportedMetadataModelList());

            ProfileImportResponse profileImportResponse = new ProfileImportResponse();
            profileImportResponse.setImported(importContext.getImported());
            profileImportResponse.setFailed(importContext.getFailed());

            GenieResponse<ProfileImportResponse> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(profileImportResponse);
            return response;
        }

        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, ServiceConstants.ErrorMessage.IMPORT_PROFILE_FAILED, TAG);
    }

    @Override
    public IChainable<ProfileImportResponse, ImportProfileContext> then(IChainable<ProfileImportResponse, ImportProfileContext> link) {
        return link;
    }

    private void logGETransferEvent(ImportProfileContext importContext, List<ImportedMetadataModel> importedMetadataModelList) {
        int aggregateCount = 0;
        GETransfer.Builder geTransfer = new GETransfer.Builder();
        geTransfer.directionImport()
                .dataTypeProfile()
                .size(LongUtil.tryParseToLong((String) importContext.getMetadata().get(ServiceConstants.FILE_SIZE), 0));

        for (ImportedMetadataModel importedMetadataModel : importedMetadataModelList) {
            aggregateCount += importedMetadataModel.getCount();

            geTransfer.addContent(importedMetadataModel.getDeviceId(), importedMetadataModel.getImportedId(), importedMetadataModel.getCount());
        }

        geTransfer.count(aggregateCount);

        TelemetryLogger.log(geTransfer.build());
    }
}
