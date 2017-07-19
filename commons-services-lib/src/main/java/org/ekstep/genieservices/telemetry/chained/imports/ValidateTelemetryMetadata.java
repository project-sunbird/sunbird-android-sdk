package org.ekstep.genieservices.telemetry.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.importexport.bean.ImportTelemetryContext;
import org.ekstep.genieservices.importexport.db.model.MetadataModel;
import org.ekstep.genieservices.telemetry.model.ImportedMetadataModel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class ValidateTelemetryMetadata implements IChainable<Void, ImportTelemetryContext> {

    private static final String TAG = ValidateTelemetryMetadata.class.getSimpleName();
    private IChainable<Void, ImportTelemetryContext> nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportTelemetryContext importContext) {
        IDBSession externalDBSession = importContext.getDataSource().getReadOnlyDataSource(importContext.getSourceDBFilePath());
        Map<String, Object> metadata = getMetadataNeedsToBeImport(externalDBSession);

        if (metadata != null && !metadata.isEmpty()) {
            importContext.setMetadata(metadata);

            List<String> importTypes = getImportTypes(importContext.getMetadata());
            if (importTypes != null && importTypes.contains(ServiceConstants.EXPORT_TYPE_TELEMETRY)) {
                String importId = (String) importContext.getMetadata().get(ServiceConstants.EXPORT_ID);
                String did = (String) importContext.getMetadata().get(ServiceConstants.DID);

                ImportedMetadataModel importedMetadataModel = ImportedMetadataModel.find(appContext.getDBSession(), importId, did);
                if (importedMetadataModel != null) {
                    return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "This data has already been imported.", TAG);
                }
            } else {
                return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Telemetry event import failed, type mismatch.", TAG);
            }
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Telemetry event import failed, metadata validation failed.", TAG);
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import telemetry failed.", TAG);
        }
    }

    @Override
    public IChainable<Void, ImportTelemetryContext> then(IChainable<Void, ImportTelemetryContext> link) {
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
