package org.ekstep.genieservices.telemetry.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataListModel;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class AddGeTransferTelemetryImportEvent implements IChainable {

    private static final String TAG = AddGeTransferTelemetryImportEvent.class.getSimpleName();
    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        ImportedMetadataListModel importedMetadataListModel = ImportedMetadataListModel.findAll(appContext.getDBSession());
        if (importedMetadataListModel != null) {
            // TODO: 6/9/2017  
        }

        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import telemetry event failed.", TAG);
    }

    @Override
    public IChainable then(IChainable link) {
        return null;
    }
}
