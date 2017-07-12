package org.ekstep.genieservices.profile.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferMap;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.profile.bean.ExportProfileContext;
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

    private String destinationDBFilePath;

    public AddGeTransferProfileExportEvent(String destinationDBFilePath) {
        this.destinationDBFilePath = destinationDBFilePath;
    }

    @Override
    public GenieResponse<ProfileExportResponse> execute(AppContext appContext, ExportProfileContext exportContext) {
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
            aggregateCount += Integer.valueOf(exportContext.getMetadata().get(ServiceConstants.PROFILES_COUNT).toString());
            GETransferEventKnowStructure eks = new GETransferEventKnowStructure(
                    GETransferEventKnowStructure.TRANSFER_DIRECTION_EXPORT,
                    GETransferEventKnowStructure.DATATYPE_PROFILE,
                    aggregateCount,
                    new File(destinationDBFilePath).length(),
                    contents);
            GETransfer geTransfer = new GETransfer(new GameData(appContext.getParams().getString(ServiceConstants.Params.GID), appContext.getParams().getString(ServiceConstants.Params.VERSION_NAME)), eks);
            TelemetryLogger.log(geTransfer);

        } catch (NumberFormatException ex) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, ex.getMessage(), TAG);
        }

        ProfileExportResponse profileExportResponse = new ProfileExportResponse();
        profileExportResponse.setExportedFilePath(destinationDBFilePath);

        GenieResponse<ProfileExportResponse> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(profileExportResponse);
        return response;
    }

    @Override
    public IChainable<ProfileExportResponse, ExportProfileContext> then(IChainable<ProfileExportResponse, ExportProfileContext> link) {
        return link;
    }
}
