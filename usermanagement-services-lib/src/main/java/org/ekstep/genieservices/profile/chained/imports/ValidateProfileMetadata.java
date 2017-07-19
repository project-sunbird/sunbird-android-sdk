package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.importexport.db.model.MetadataModel;
import org.ekstep.genieservices.profile.bean.ImportProfileContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class ValidateProfileMetadata implements IChainable<ProfileImportResponse, ImportProfileContext> {

    private static final String TAG = ValidateProfileMetadata.class.getSimpleName();
    private IChainable<ProfileImportResponse, ImportProfileContext> nextLink;

    @Override
    public GenieResponse<ProfileImportResponse> execute(AppContext appContext, ImportProfileContext importContext) {
        IDBSession externalDBSession = importContext.getDataSource().getImportDataSource(importContext.getSourceFilePath());
        // Read from imported DB
        Map<String, Object> metadata = getMetadataNeedsToBeImport(externalDBSession);

        if (metadata != null && !metadata.isEmpty()) {
            importContext.setMetadata(metadata);

            List<String> importTypes = getImportTypes(importContext.getMetadata());
            if (importTypes != null && !importTypes.contains(ServiceConstants.EXPORT_TYPE_PROFILE)) {
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
    public IChainable<ProfileImportResponse, ImportProfileContext> then(IChainable<ProfileImportResponse, ImportProfileContext> link) {
        nextLink = link;
        return link;
    }

    private Map<String, Object> getMetadataNeedsToBeImport(IDBSession externalDBSession) {
        MetadataModel metadataModel = MetadataModel.findAll(externalDBSession);
        Map<String, Object> metadata = null;
        if (metadataModel != null) {
            metadata = metadataModel.getMetadata();
        }
        return metadata;
    }

    private List<String> getImportTypes(Map<String, Object> metadata) {
        if (metadata.containsKey(ServiceConstants.EXPORT_TYPES)) {
            String importedDataType = (String) metadata.get(ServiceConstants.EXPORT_TYPES);
            return Arrays.asList(GsonUtil.fromJson(importedDataType, String[].class));
        }
        return null;
    }
}
