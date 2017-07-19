package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.profile.bean.ImportProfileContext;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class UpdateImportedProfileMetadata implements IChainable<ProfileImportResponse, ImportProfileContext> {

    private static final String TAG = UpdateImportedProfileMetadata.class.getSimpleName();
    private IChainable<ProfileImportResponse, ImportProfileContext> nextLink;

    @Override
    public GenieResponse<ProfileImportResponse> execute(AppContext appContext, ImportProfileContext importContext) {
        String importId = (String) importContext.getMetadata().get(ServiceConstants.EXPORT_ID);
        String did = (String) importContext.getMetadata().get(ServiceConstants.DID);
        int count = Integer.valueOf((String) importContext.getMetadata().get(ServiceConstants.PROFILES_COUNT));

        ImportedMetadataModel importedMetadataModel = ImportedMetadataModel.build(appContext.getDBSession(), importId, did, count);
        if (ImportedMetadataModel.find(appContext.getDBSession(), importId, did) == null) {
            importedMetadataModel.save();
        } else {
            importedMetadataModel.update();
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Data has been imported, but failed to update meta data.", TAG);
        }
    }

    @Override
    public IChainable<ProfileImportResponse, ImportProfileContext> then(IChainable<ProfileImportResponse, ImportProfileContext> link) {
        nextLink = link;
        return link;
    }

}
