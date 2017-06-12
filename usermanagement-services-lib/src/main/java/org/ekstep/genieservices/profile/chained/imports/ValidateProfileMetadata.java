package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class ValidateProfileMetadata implements IChainable {

    private static final String TAG = ValidateProfileMetadata.class.getSimpleName();
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
            } else {
                return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Profile event import failed, type mismatch.", TAG);
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
        String importedDataType = (String) metadata.get(ServiceConstants.EXPORT_TYPES);
        return Arrays.asList(GsonUtil.fromJson(importedDataType, String[].class));
    }
}
