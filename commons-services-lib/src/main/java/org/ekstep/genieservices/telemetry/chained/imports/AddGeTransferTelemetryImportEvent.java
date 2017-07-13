package org.ekstep.genieservices.telemetry.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferMap;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.LongUtil;
import org.ekstep.genieservices.telemetry.TelemetryLogger;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataListModel;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

import java.util.ArrayList;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class AddGeTransferTelemetryImportEvent implements IChainable {

    private static final String TAG = AddGeTransferTelemetryImportEvent.class.getSimpleName();

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        try {
            ImportedMetadataListModel importedMetadataListModel = ImportedMetadataListModel.findAll(appContext.getDBSession());

            if (importedMetadataListModel != null) {
                int aggregateCount = 0;
                ArrayList<GETransferMap> transferMapList = new ArrayList<>();
                for (ImportedMetadataModel importMetadata : importedMetadataListModel.getImportedMetadataModelList()) {
                    aggregateCount += importMetadata.getCount();
                    transferMapList.add(GETransferMap.createMapForTelemetry(importMetadata.getDeviceId(),
                            importMetadata.getImportedId(), importMetadata.getCount()));
                }
                GETransferEventKnowStructure eks = new GETransferEventKnowStructure(
                        GETransferEventKnowStructure.TRANSFER_DIRECTION_IMPORT,
                        GETransferEventKnowStructure.DATATYPE_TELEMETRY,
                        aggregateCount,
                        LongUtil.tryParseToLong((String) importContext.getMetadata().get(GETransferEventKnowStructure.FILE_SIZE), 0),
                        transferMapList);

                GETransfer geTransfer = new GETransfer(eks);
                TelemetryLogger.log(geTransfer);
                return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            }
        } catch (NumberFormatException ex) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, ServiceConstants.ErrorMessage.IMPORT_TELEMETRY_FAILED, TAG);
        }

        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, ServiceConstants.ErrorMessage.IMPORT_TELEMETRY_FAILED, TAG);
    }

    @Override
    public IChainable then(IChainable link) {
        return null;
    }
}
