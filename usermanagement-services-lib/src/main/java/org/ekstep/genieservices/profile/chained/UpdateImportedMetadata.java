package org.ekstep.genieservices.profile.chained;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class UpdateImportedMetadata implements IChainable {

    private static final String TAG = UpdateImportedMetadata.class.getSimpleName();
    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        String importId = (String) importContext.getMetadata().get(ServiceConstants.EXPORT_ID);
        String did = (String) importContext.getMetadata().get(ServiceConstants.DID);
        int count = (int) importContext.getMetadata().get(ServiceConstants.PROFILES_COUNT);

        ImportedMetadataModel importedMetadataModel = ImportedMetadataModel.build(appContext.getDBSession(), importId, did, count);
        if (ImportedMetadataModel.find(appContext.getDBSession(), importId, did) == null) {
            importedMetadataModel.save();
        } else {
            importedMetadataModel.update();
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import profile failed.", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }

}
