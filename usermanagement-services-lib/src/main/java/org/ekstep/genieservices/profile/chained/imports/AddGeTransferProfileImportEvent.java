package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferMap;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.LongUtil;
import org.ekstep.genieservices.telemetry.TelemetryLogger;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

import java.util.ArrayList;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class AddGeTransferProfileImportEvent implements IChainable {

    private static final String TAG = AddGeTransferProfileImportEvent.class.getSimpleName();
    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        try {
            String importId = (String) importContext.getMetadata().get(ServiceConstants.EXPORT_ID);
            String did = (String) importContext.getMetadata().get(ServiceConstants.DID);

            ImportedMetadataModel importedMetadataModel = ImportedMetadataModel.find(appContext.getDBSession(), importId, did);

            if (importedMetadataModel != null) {
                int aggregateCount = 0;
                ArrayList<GETransferMap> transferMapList = new ArrayList<>();
                aggregateCount += importedMetadataModel.getCount();
                transferMapList.add(GETransferMap.createMapForTelemetry(importedMetadataModel.getDeviceId(),
                        importedMetadataModel.getImportedId(), importedMetadataModel.getCount()));

                GETransferEventKnowStructure eks = new GETransferEventKnowStructure(
                        GETransferEventKnowStructure.TRANSFER_DIRECTION_IMPORT,
                        GETransferEventKnowStructure.DATATYPE_PROFILE,
                        aggregateCount,
                        LongUtil.tryParseToLong((String) importContext.getMetadata().get(GETransferEventKnowStructure.FILE_SIZE), 0),
                        transferMapList);
                importedMetadataModel.clear();
                GETransfer geTransfer = new GETransfer(new GameData(appContext.getParams().getGid(), appContext.getParams().getVersionName()), eks);
                TelemetryLogger.log(geTransfer);
                return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            }
        } catch (NumberFormatException e) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, ServiceConstants.ErrorMessage.IMPORT_PROFILE_FAILED, TAG);
        }


        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, ServiceConstants.ErrorMessage.IMPORT_PROFILE_FAILED, TAG);
    }

    @Override
    public IChainable then(IChainable link) {
        return link;
    }
}
