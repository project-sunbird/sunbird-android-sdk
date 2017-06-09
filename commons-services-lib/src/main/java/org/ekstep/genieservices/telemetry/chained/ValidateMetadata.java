package org.ekstep.genieservices.telemetry.chained;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

import java.util.List;
import java.util.Map;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class ValidateMetadata implements IChainable {

    private static final String TAG = ValidateMetadata.class.getSimpleName();
    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        if (importContext.getMetadata() != null && !importContext.getMetadata().isEmpty()) {
            List<String> importTypes = getImportTypes(importContext.getMetadata());
            if (importTypes != null && importTypes.contains(ServiceConstants.EXPORT_TYPE_PROFILE)) {
                String importId = (String) importContext.getMetadata().get(ServiceConstants.EXPORT_ID);
                String did = (String) importContext.getMetadata().get(ServiceConstants.DID);

                ImportedMetadataModel importedMetadataModel = ImportedMetadataModel.find(appContext.getDBSession(), importId, did);
                if (importedMetadataModel != null) {
                    return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "This data has already been imported.", TAG);
                }
            }
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Profile import failed, metadata validation failed.", TAG);
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

    private List<String> getImportTypes(Map<String, Object> metadata) {
        return (List<String>) metadata.get(ServiceConstants.EXPORT_TYPES);
    }
}
