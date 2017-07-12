package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferMap;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.LongUtil;
import org.ekstep.genieservices.profile.bean.ImportProfileContext;
import org.ekstep.genieservices.telemetry.TelemetryLogger;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataListModel;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

import java.util.ArrayList;

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
            int aggregateCount = 0;
            ArrayList<GETransferMap> contents = new ArrayList<>();
            for (ImportedMetadataModel importMetadata : importedMetadataListModel.getImportedMetadataModelList()) {
                aggregateCount += importMetadata.getCount();
                contents.add(GETransferMap.createMapForTelemetry(importMetadata.getDeviceId(),
                        importMetadata.getImportedId(), importMetadata.getCount()));
            }
            GETransferEventKnowStructure eks = new GETransferEventKnowStructure(
                    GETransferEventKnowStructure.TRANSFER_DIRECTION_IMPORT,
                    GETransferEventKnowStructure.DATATYPE_PROFILE,
                    aggregateCount,
                    LongUtil.tryParseToLong((String) importContext.getMetadata().get(GETransferEventKnowStructure.FILE_SIZE), 0),
                    contents);
            GETransfer geTransfer = new GETransfer(new GameData(appContext.getParams().getString(ServiceConstants.Params.GID), appContext.getParams().getString(ServiceConstants.Params.VERSION_NAME)), eks);
            TelemetryLogger.log(geTransfer);

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
}
