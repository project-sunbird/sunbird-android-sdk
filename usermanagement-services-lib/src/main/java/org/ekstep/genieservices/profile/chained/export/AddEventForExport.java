package org.ekstep.genieservices.profile.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.telemetry.GETransfer;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferContentMap;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.chained.IChainable;
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
public class AddEventForExport implements IChainable {

    private static final String TAG = AddEventForExport.class.getSimpleName();
    private IChainable nextLink;

    private String destinationDBFilePath;

    public AddEventForExport(String destinationDBFilePath) {
        this.destinationDBFilePath = destinationDBFilePath;
    }

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        try {
            int aggregateCount = 0;
            ImportedMetadataListModel importedMetadataListModel = ImportedMetadataListModel.findAll(appContext.getDBSession());

            List<ImportedMetadataModel> importedMetadataModelList;
            if (importedMetadataListModel != null) {
                importedMetadataModelList = importedMetadataListModel.getImportedMetadataModelList();
            } else {
                importedMetadataModelList = new ArrayList<>();
            }

            ArrayList<GETransferContentMap> contents = new ArrayList<>();
            for (ImportedMetadataModel importedMetadataModel : importedMetadataModelList) {
                aggregateCount += importedMetadataModel.getCount();
                contents.add(
                        GETransferContentMap.createMapForTelemetry(importedMetadataModel.getDeviceId(),
                                importedMetadataModel.getImportedId(), importedMetadataModel.getCount()));
            }
            aggregateCount += (int) importContext.getMetadata().get(ServiceConstants.PROFILES_COUNT);
            GETransferEventKnowStructure eks = new GETransferEventKnowStructure(
                    GETransferEventKnowStructure.TRANSFER_DIRECTION_EXPORT,
                    GETransferEventKnowStructure.DATATYPE_PROFILE,
                    aggregateCount,
                    new File(destinationDBFilePath).length(),
                    contents);
            GETransfer geTransfer = new GETransfer(new GameData(appContext.getParams().getGid(), appContext.getParams().getVersionName()), eks);

            // TODO: 6/12/2017
//            Telemetry telemetry = new Telemetry(context);
//            telemetry.storeEvent(GsonUtil.toJson(geTransfer));
        } catch (NumberFormatException ex) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, ex.getMessage(), TAG);
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export profile failed", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }
}
